package sube.interviews.mareoenvios.unit.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.mapper.CustomerMapper;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.exception.ResourceNotFoundException;
import sube.interviews.mareoenvios.exception.RetryableIntegrationException;
import sube.interviews.mareoenvios.repository.CustomerRepository;
import sube.interviews.mareoenvios.strategy.customer.CustomerAlreadyExistsStrategy;
import sube.interviews.mareoenvios.strategy.customer.CustomerBlockStrategy;
import sube.interviews.mareoenvios.strategy.customer.NewCustomerStrategy;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerResolutionStrategiesTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    private CustomerBlockStrategy customerBlockStrategy;
    private CustomerAlreadyExistsStrategy customerAlreadyExistsStrategy;
    private NewCustomerStrategy newCustomerStrategy;

    @BeforeEach
    void setUp() {
        customerBlockStrategy = new CustomerBlockStrategy();
        ReflectionTestUtils.setField(customerBlockStrategy, "chaosCustomers", List.of(666));
        customerAlreadyExistsStrategy = new CustomerAlreadyExistsStrategy(customerRepository);
        newCustomerStrategy = new NewCustomerStrategy(customerRepository, customerMapper);
    }

    @Test
    void testCustomerBlockStrategy_SupportsBlockedId() {
        CreateShippingRequest request = new CreateShippingRequest();

        request.setCustomerId(666);

        assertTrue(customerBlockStrategy.supports(request));
    }

    @Test
    void testCustomerBlockStrategy_DoesNotSupportNormalId() {
        CreateShippingRequest request = new CreateShippingRequest();

        request.setCustomerId(1);

        assertFalse(customerBlockStrategy.supports(request));
    }

    @Test
    void testCustomerBlockStrategy_Resolve_ThrowsRetryableException() {
        CreateShippingRequest request = new CreateShippingRequest();

        request.setCustomerId(666);

        assertThrows(RetryableIntegrationException.class, () -> {
            customerBlockStrategy.resolve(request);
        });
    }

    @Test
    void testCustomerAlreadyExistsStrategy_SupportsNonNullId() {
        CreateShippingRequest request = new CreateShippingRequest();

        request.setCustomerId(1);

        assertTrue(customerAlreadyExistsStrategy.supports(request));
    }

    @Test
    void testCustomerAlreadyExistsStrategy_DoesNotSupportNullId() {
        CreateShippingRequest request = new CreateShippingRequest();

        request.setCustomerId(null);

        assertFalse(customerAlreadyExistsStrategy.supports(request));
    }

    @Test
    void testCustomerAlreadyExistsStrategy_Resolve_WhenFound_ReturnsCustomer() {
        CreateShippingRequest request = new CreateShippingRequest();
        request.setCustomerId(1);
        Customer customer = Customer.builder().id(1).firstName("Marcos").build();

        when(customerRepository.fetchById(1)).thenReturn(Optional.of(customer));

        Customer result = customerAlreadyExistsStrategy.resolve(request);

        assertNotNull(result);
        assertEquals(customer, result);
        verify(customerRepository, times(1)).fetchById(1);
    }

    @Test
    void testCustomerAlreadyExistsStrategy_Resolve_WhenNotFound_ThrowsResourceNotFoundException() {
        CreateShippingRequest request = new CreateShippingRequest();

        request.setCustomerId(999);

        when(customerRepository.fetchById(999)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            customerAlreadyExistsStrategy.resolve(request);
        });
    }

    @Test
    void testNewCustomerStrategy_SupportsNullId() {
        CreateShippingRequest request = new CreateShippingRequest();

        request.setCustomerId(null);

        assertTrue(newCustomerStrategy.supports(request));
    }

    @Test
    void testNewCustomerStrategy_DoesNotSupportNonNullId() {
        CreateShippingRequest request = new CreateShippingRequest();

        request.setCustomerId(1);

        assertFalse(newCustomerStrategy.supports(request));
    }

    @Test
    void testNewCustomerStrategy_Resolve_CreatesAndSavesNewCustomer() {
        CreateShippingRequest request = new CreateShippingRequest();
        request.setCustomerId(null);
        Customer customer = Customer.builder().firstName("Gaston").build();
        Customer savedCustomer = Customer.builder().id(4).firstName("Gaston").build();

        when(customerMapper.toEntity(request)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(savedCustomer);

        Customer result = newCustomerStrategy.resolve(request);

        assertNotNull(result);
        assertEquals(savedCustomer, result);
        verify(customerMapper, times(1)).toEntity(request);
        verify(customerRepository, times(1)).save(customer);
    }
}

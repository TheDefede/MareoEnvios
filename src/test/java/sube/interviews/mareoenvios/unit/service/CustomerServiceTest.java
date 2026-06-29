package sube.interviews.mareoenvios.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import sube.interviews.mareoenvios.dto.CustomerDto;
import sube.interviews.mareoenvios.dto.mapper.CustomerMapper;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.exception.ResourceNotFoundException;
import sube.interviews.mareoenvios.repository.CustomerRepository;
import sube.interviews.mareoenvios.service.CustomerService;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerService customerService;

    @Test
    void testGetById_WhenCustomerExists_ReturnsCustomerDto() {
        Integer customerId = 1;
        Customer customer = Customer.builder().id(customerId).firstName("Marcos").build();
        CustomerDto customerDto = CustomerDto.builder().id(customerId).firstName("Marcos").build();

        when(customerRepository.fetchById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        CustomerDto result = customerService.getById(customerId);

        assertNotNull(result);
        assertSame(customerDto, result);
        verify(customerRepository, times(1)).fetchById(customerId);
    }

    @Test
    void testGetById_WhenCustomerDoesNotExist_ThrowsResourceNotFoundException() {
        Integer customerId = 999;

        when(customerRepository.fetchById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getById(customerId));

        verify(customerRepository, times(1)).fetchById(customerId);
    }

    @Test
    void testGetAllCustomers_ReturnsPageOfCustomerDto() {
        Pageable pageable = mock(Pageable.class);
        Customer customer = Customer.builder().id(1).firstName("Marcos").build();
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customer));
        CustomerDto customerDto = CustomerDto.builder().id(1).firstName("Marcos").build();

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        Page<CustomerDto> result = customerService.getAllCustomers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(customerDto, result.getContent().get(0));
    }

    @Test
    void testCreateCustomer_DelegatesToMapperAndRepository() {
        CustomerDto requestDto = CustomerDto.builder().firstName("Juan").lastName("Perez").build();
        Customer customer = Customer.builder().firstName("Juan").lastName("Perez").build();
        Customer savedCustomer = Customer.builder().id(5).firstName("Juan").lastName("Perez").build();
        CustomerDto responseDto = CustomerDto.builder().id(5).firstName("Juan").lastName("Perez").build();

        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(savedCustomer);
        when(customerMapper.toDto(savedCustomer)).thenReturn(responseDto);

        CustomerDto result = customerService.createCustomer(requestDto);

        assertNotNull(result);
        assertSame(responseDto, result);
        verify(customerMapper, times(1)).toEntity(requestDto);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testUpdateCustomer_WhenCustomerExists_UpdatesAndReturnsDto() {
        Integer customerId = 1;
        CustomerDto updateDto = CustomerDto.builder().firstName("Marcos").address("Nueva calle 123").build();
        Customer customer = Customer.builder().id(customerId).firstName("Marcos").build();
        Customer savedCustomer = Customer.builder().id(customerId).firstName("Marcos").address("Nueva calle 123").build();
        CustomerDto responseDto = CustomerDto.builder().id(customerId).firstName("Marcos").address("Nueva calle 123").build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerMapper).updateEntity(customer, updateDto);
        when(customerRepository.save(customer)).thenReturn(savedCustomer);
        when(customerMapper.toDto(savedCustomer)).thenReturn(responseDto);

        CustomerDto result = customerService.updateCustomer(customerId, updateDto);

        assertNotNull(result);
        assertSame(responseDto, result);
        verify(customerMapper, times(1)).updateEntity(customer, updateDto);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testUpdateCustomer_WhenCustomerDoesNotExist_ThrowsResourceNotFoundException() {
        Integer customerId = 999;
        CustomerDto updateDto = CustomerDto.builder().firstName("Marcos").build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(customerId, updateDto));

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any());
    }
}

package sube.interviews.mareoenvios.unit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sube.interviews.mareoenvios.dto.CustomerDto;
import sube.interviews.mareoenvios.dto.mapper.CustomerMapper;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.exception.BusinessRuleException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerMapperTest {
    private CustomerMapper customerMapper;
    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void testToDto_WithValidEntity_ReturnsDto() {
        Customer entity = Customer.builder()
                .id(1)
                .firstName("Marcos")
                .lastName("Gutierrez")
                .address("la buena direccion 123")
                .city("CABA")
                .build();

        CustomerDto dto = customerMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getAddress(), dto.getAddress());
        assertEquals(entity.getCity(), dto.getCity());
    }

    @Test
    void testToDto_WithNullEntity_ReturnsNull() {
        assertNull(customerMapper.toDto(null));
    }

    @Test
    void testToDtoList_WithValidList_ReturnsDtoList() {
        Customer entity1 = Customer.builder().id(1).firstName("Marcos").build();
        Customer entity2 = Customer.builder().id(2).firstName("Hernan").build();

        List<CustomerDto> dtoList = customerMapper.toDtoList(Arrays.asList(entity1, entity2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("Marcos", dtoList.get(0).getFirstName());
        assertEquals("Hernan", dtoList.get(1).getFirstName());
    }

    @Test
    void testToDtoList_WithNullList_ReturnsEmptyList() {
        List<CustomerDto> result = customerMapper.toDtoList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToEntity_WithValidDto_ReturnsEntity() {
        CustomerDto dto = CustomerDto.builder()
                .firstName("Juan")
                .lastName("Perez")
                .address("Calle Falsa 123")
                .city("Mendoza")
                .build();

        Customer entity = customerMapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Juan", entity.getFirstName());
        assertEquals("Perez", entity.getLastName());
        assertEquals("Calle Falsa 123", entity.getAddress());
        assertEquals("Mendoza", entity.getCity());
    }

    @Test
    void testToEntity_WithMissingFields_ThrowsBusinessRuleException() {
        CustomerDto invalidDto = CustomerDto.builder()
                .firstName("   ")
                .lastName("Perez")
                .address("Calle Falsa 123")
                .build();

        assertThrows(BusinessRuleException.class, () -> {
            customerMapper.toEntity(invalidDto);
        });
    }

    @Test
    void testToEntity_WithCreateShippingRequest_ReturnsEntity() {
        CreateShippingRequest request = new CreateShippingRequest();
        request.setFirstName("Juan");
        request.setLastName("Perez");
        request.setAddress("Calle Falsa 123");
        request.setCity("Mendoza");

        Customer entity = customerMapper.toEntity(request);

        assertNotNull(entity);
        assertEquals("Juan", entity.getFirstName());
    }

    @Test
    void testUpdateEntity_WithValidFields_UpdatesOnlyNonNullFields() {
        Customer customer = Customer.builder()
                .id(1)
                .firstName("Marcos")
                .lastName("Gutierrez")
                .address("Calle vieja 111")
                .city("CABA")
                .build();

        CustomerDto updateDto = CustomerDto.builder()
                .address("Calle nueva 222")
                .city("Mendoza")
                .build();

        customerMapper.updateEntity(customer, updateDto);

        assertEquals("Marcos", customer.getFirstName());
        assertEquals("Gutierrez", customer.getLastName());
        assertEquals("Calle nueva 222", customer.getAddress());
        assertEquals("Mendoza", customer.getCity());
    }

    @Test
    void testUpdateEntity_WithBlankFields_ThrowsBusinessRuleException() {
        Customer customer = Customer.builder().id(1).firstName("Marcos").build();
        CustomerDto invalidUpdateDto = CustomerDto.builder().firstName("   ").build();

        assertThrows(BusinessRuleException.class, () -> {
            customerMapper.updateEntity(customer, invalidUpdateDto);
        });
    }
}

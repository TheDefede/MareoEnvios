package sube.interviews.mareoenvios.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.dto.mapper.CustomerMapper;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.CustomerDto;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.exception.ResourceNotFoundException;
import sube.interviews.mareoenvios.repository.CustomerRepository;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerDto getById(Integer id){
        Customer customer = customerRepository.fetchById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Customer con ID:%d no encontrado", id)));

        return customerMapper.toDto(customer);
    }

    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(customerMapper::toDto);
    }

    public CustomerDto save(CreateShippingRequest request){
        Customer newCustomer = customerMapper.toEntity(request);

        return customerMapper.toDto(customerRepository.save(newCustomer));
    }
}

package pt.santander.customerapi.service;

import pt.santander.customerapi.dto.CustomerRequest;
import pt.santander.customerapi.dto.CustomerResponse;
import java.util.List;

public interface CustomerService {

    public List<CustomerResponse> getCustomers(String nome, String nif, String active, String email, String min, String max) throws Exception;
    public CustomerResponse createCustomer(CustomerRequest request) throws Exception;
    public CustomerResponse updateCustomer( Integer id, CustomerRequest request) throws Exception;
    public void deleteCustomer(Integer id) throws Exception;
    public List<CustomerResponse> findByEmailGroup(String group) throws Exception;
}

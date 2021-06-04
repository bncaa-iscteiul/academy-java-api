package pt.santander.customerapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pt.santander.customerapi.dto.CustomerRequest;
import pt.santander.customerapi.dto.CustomerResponse;
import pt.santander.customerapi.entity.Customer;
import pt.santander.customerapi.repository.CustomerRepository;
import pt.santander.customerapi.service.CustomerService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceDatabaseImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRep;

    /*@PostConstruct
    public void init(){
        //após a construção do objeto CustomerServiceDatabaseImpl
    }

    @PreDestroy
    public void destroy(){
        //antes de destruir o objeto CustomerServiceDatabaseImpl
    }*/

    @Override
    public List<CustomerResponse> getCustomers(String name, String nif, String active, String email, String min, String max) throws Exception {

        List<CustomerResponse> responseList = new ArrayList<>();

        if (name != null) {
            if("true".equalsIgnoreCase(active) || active == null) {
                findByActiveAndNameContaining(responseList, true, name);
            }else if ("false".equalsIgnoreCase(active)){
                findByActiveAndNameContaining(responseList, false, name);
            }else if("all".equalsIgnoreCase(active)){
                findByActiveAndNameContaining(responseList, true, name);
                findByActiveAndNameContaining(responseList, false, name);
            }

        } else if(nif != null){
            if("true".equalsIgnoreCase(active) || active == null) {
                findByActiveAndNifContaining(responseList, true, nif);
            }else if ("false".equalsIgnoreCase(active)){
                findByActiveAndNifContaining(responseList, false, nif);
            }else if("all".equalsIgnoreCase(active)){
                findByActiveAndNifContaining(responseList, true, nif);
                findByActiveAndNifContaining(responseList, false, nif);
            }

        } else if(name != null && nif != null){
            responseList.addAll(
                    customerRep.findByActiveAndNameAndNifContaining(true, name, nif)
                            .stream()
                            .map( customer -> {
                                return CustomerResponse.builder()
                                        .id(customer.getId())
                                        .name(customer.getName())
                                        .nif(customer.getNif())
                                        .email(customer.getEmail())
                                        .active(customer.getActive())
                                        .build();

                            })
                            .collect(Collectors.toList())
            );
        } else if(email != null){
            responseList.addAll(
                    customerRep.findByEmailContaining(email)
                            .stream()
                            .map( customer -> {
                                return CustomerResponse.builder()
                                        .id(customer.getId())
                                        .name(customer.getName())
                                        .nif(customer.getNif())
                                        .email(customer.getEmail())
                                        .active(customer.getActive())
                                        .build();

                            })
                            .collect(Collectors.toList())
            );
        } else if(min!=null){
            responseList.addAll(
                    customerRep.findByNifGreaterThanEqual(min)
                            .stream()
                            .map( customer -> {
                                return CustomerResponse.builder()
                                        .id(customer.getId())
                                        .name(customer.getName())
                                        .nif(customer.getNif())
                                        .email(customer.getEmail())
                                        .active(customer.getActive())
                                        .build();

                            })
                            .collect(Collectors.toList())
            );
        }else if(max!=null){
            responseList.addAll(
                    customerRep.findByNifLessThanEqual(max)
                            .stream()
                            .map( customer -> {
                                return CustomerResponse.builder()
                                        .id(customer.getId())
                                        .name(customer.getName())
                                        .nif(customer.getNif())
                                        .email(customer.getEmail())
                                        .active(customer.getActive())
                                        .build();

                            })
                            .collect(Collectors.toList())
            );
        }

        else if ("true".equalsIgnoreCase(active) || active == null) {
            findByActive(responseList, true);
        }else if ("false".equalsIgnoreCase(active)) {
            findByActive(responseList, false);
        }else{
            findByActive(responseList, true);
            findByActive(responseList, false);
        }
    return responseList;
    }

    private void findByActiveAndNameContaining(
            List<CustomerResponse> responseList,
            boolean active,
            String name
    ){
        responseList.addAll(
                customerRep.findByActiveAndNameContaining(active, name)
                        .stream()
                        .map( customer -> {
                            return CustomerResponse.builder()
                                    .id(customer.getId())
                                    .name(customer.getName())
                                    .nif(customer.getNif())
                                    .email(customer.getEmail())
                                    .active(customer.getActive())
                                    .build();

                        })
                        .collect(Collectors.toList())
        );
    }

    private void findByActiveAndNifContaining(
            List<CustomerResponse> responseList,
            boolean active,
            String nif
    ){
        responseList.addAll(
                customerRep.findByActiveAndNif(active, nif)
                        .stream()
                        .map( customer -> {
                            return CustomerResponse.builder()
                                    .id(customer.getId())
                                    .name(customer.getName())
                                    .nif(customer.getNif())
                                    .email(customer.getEmail())
                                    .active(customer.getActive())
                                    .build();

                        })
                        .collect(Collectors.toList())
        );
    }

    private void findByActive(
            List<CustomerResponse> responseList,
            boolean active
    ){
        responseList.addAll(
                customerRep.findByActive(active)
                        .stream()
                        .map( customer -> {
                            return CustomerResponse.builder()
                                    .id(customer.getId())
                                    .name(customer.getName())
                                    .nif(customer.getNif())
                                    .email(customer.getEmail())
                                    .active(customer.getActive())
                                    .build();

                        })
                        .collect(Collectors.toList())
        );
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 5)
    public CustomerResponse createCustomer(CustomerRequest request) throws Exception {
        List<CustomerResponse> lista = new ArrayList<>();
        if(!request.getList().isEmpty() && request.getList() != null){
            request.getList().forEach(
                    customerRequestItem -> {
                        Customer customer = Customer.builder()
                                .name(customerRequestItem.getName())
                                .nif(customerRequestItem.getNif())
                                .email(customerRequestItem.getEmail())
                                .active(true)
                                .build();
                        Customer customerNew = customerRep.save(customer);
                        lista.add(CustomerResponse.builder()
                                .id(customerNew.getId())
                                .name(customerNew.getName())
                                .nif(customerNew.getNif())
                                .email(customerNew.getEmail())
                                .active(customerNew.getActive())
                                .build()
                        );
                    }
            );
        }
        Customer customerNew = customerRep.save(
                Customer.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .nif(request.getNif())
                        .active(true)
                        .build()
        );
        // Convert entity to dto and return
        return CustomerResponse.builder()
                .id(customerNew.getId())
                .name(customerNew.getName())
                .nif(customerNew.getNif())
                .email(customerNew.getEmail())
                .active(customerNew.getActive())
                .list(lista)
                .build();
    }

    public CustomerResponse updateCustomer(Integer id, CustomerRequest request) throws Exception {
        return customerRep.findById(id)
                .map(customer -> {
                    customer.setName(request.getName());
                    customer.setNif(request.getNif());
                    customer.setEmail(request.getEmail());
                    Customer customerUpdate =  customerRep.save(customer);
                    return CustomerResponse.builder()
                            .id(customerUpdate.getId())
                            .name(customerUpdate.getName())
                            .nif(customerUpdate.getNif())
                            .email(customerUpdate.getEmail())
                            .active(customerUpdate.getActive())
                            .build();
                })
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
    }

    public void deleteCustomer(Integer id) throws Exception {
        customerRep.findById(id)
                .map( customer -> {
                    customer.setActive(false);
                    return customerRep.save(customer);
                })
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não encontrado"));
    }

    @Override
    public List<CustomerResponse>  findByEmailGroup(String group) throws Exception {
        List<CustomerResponse> list = new ArrayList<>();
        group = "%" + group + "%";
        customerRep.findByEmailGroup(group)
                .forEach(customer -> {
                            list.add(
                                    CustomerResponse.builder()
                                    .id(customer.getId())
                                    .name(customer.getName())
                                    .nif(customer.getNif())
                                    .email(customer.getEmail())
                                    .active(customer.getActive())
                                    .build()
                            );
                        });
        return list;
    }

}

package pt.santander.customerapi.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.santander.customerapi.dto.CustomerRequest;
import pt.santander.customerapi.dto.CustomerResponse;
import pt.santander.customerapi.service.CustomerService;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerServ;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponse>> getCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false)  String nif,
            @RequestParam(required = false) String active,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String min,
            @RequestParam(required = false) String max) throws Exception{

        List<CustomerResponse> list = customerServ.getCustomers(name, nif, active, email, min, max);
        if(list.isEmpty()){
            return ResponseEntity.status(404).body(list);
        }
        return ResponseEntity.status(200).body(list);
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) throws Exception {
        return ResponseEntity.ok(customerServ.createCustomer(request));
    }


    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Integer id,
            @Valid @RequestBody CustomerRequest request) throws Exception {
                return ResponseEntity.ok(customerServ.updateCustomer(id, request));
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) throws Exception {
        customerServ.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customers/emailGroup")
    public ResponseEntity<List<CustomerResponse>> findByEmailGroup(@RequestParam String group) throws Exception {
        return ResponseEntity.ok(customerServ.findByEmailGroup(group));
    }

}


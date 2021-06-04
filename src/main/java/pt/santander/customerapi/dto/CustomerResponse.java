package pt.santander.customerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.santander.customerapi.entity.Customer;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerResponse {

    private Integer id;
    private String name;
    private String nif;
    private String email;
    private boolean active;
    private List<CustomerResponse> list;



}

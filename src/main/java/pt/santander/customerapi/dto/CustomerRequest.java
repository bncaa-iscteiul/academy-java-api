package pt.santander.customerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String nif;

    @NotEmpty
    @Email
    private String email;

    private List<CustomerRequest> list;


}

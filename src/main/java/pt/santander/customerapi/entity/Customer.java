package pt.santander.customerapi.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "customers")
@SequenceGenerator(sequenceName = "seq_customers", name = "seq_customers")
public class Customer {

    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    //@Column(name = "idCustomer") // se o nome na tabela não fosse "id", mas sim "idCustomer"
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "seq_customers")
    private Integer id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 9)
    private String nif;

   // @NotNull
    @Size(max = 100)
    @Email
    private String email;

    @Type(type="true_false")
    @Column(columnDefinition = "char(1)")
    private Boolean active;

}

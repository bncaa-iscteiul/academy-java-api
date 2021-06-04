package pt.santander.customerapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.santander.customerapi.entity.Customer;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    public List<Customer> findByActiveAndNameContaining(boolean active, String name);
    public List<Customer> findByActiveAndNif(boolean active, String nif);
    public List<Customer> findByActiveAndNameAndNifContaining(boolean active, String name, String nif);
    public List<Customer> findByActive (boolean active);
    public List<Customer> findByEmailContaining (String email);
    public List<Customer> findByNifGreaterThanEqual (String min);
    public List<Customer> findByNifLessThanEqual (String max);

    @Query(nativeQuery = true, value = "select * from customers where email like ?1 ")
    public List<Customer> findByEmailGroup (String group);
}

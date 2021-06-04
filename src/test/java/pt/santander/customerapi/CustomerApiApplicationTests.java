package pt.santander.customerapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pt.santander.customerapi.dto.CustomerResponse;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = CustomerApiApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class CustomerApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnListOfCustomers() throws Exception {

		//make request
		MvcResult mvcResult = mockMvc.perform(get("/customers"))
				.andExpect(status().isOk())
				.andReturn();

		//mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		List<CustomerResponse> customers = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<CustomerResponse>>() {});
		assertThat(customers).isNotEmpty();
	}

	@Test
	void shouldReturnListOfCustomersByName() throws Exception {
		//make request
		MvcResult mvcResult = mockMvc.perform(
				get("/customers").param("name", "barbara"))
				.andExpect(status().isOk())
				.andReturn();

		//mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		List<CustomerResponse> customers = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<CustomerResponse>>() {});
		assertThat(customers).isNotEmpty();
		assertThat(customers.get(0).getName().contains("barbara"));
	}

	@Test
	void shouldReturnListOfCustomersByNif() throws Exception {
		//make request
		MvcResult mvcResult = mockMvc.perform(
				get("/customers").param("nif", "298973534"))
				.andExpect(status().isOk())
				.andReturn();

		//mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		List<CustomerResponse> customers = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<CustomerResponse>>() {});
		assertThat(customers).isNotEmpty();
		assertThat(customers.size()).isEqualTo(1);
		assertThat(customers.get(0).getNif().equals("298973534"));
	}

	@Test
	void shouldReturnListOfCustomersByNameAndNif() throws Exception {

		// Make request
		MvcResult mvcResult = mockMvc.perform(get("/customers")
				.param("name", "barbara")
				.param("nif", "298973534"))
				.andExpect(status().isOk())
				.andReturn();

		// Mapping json to class
		ObjectMapper mapper = new ObjectMapper();
		List<CustomerResponse> customers = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<CustomerResponse>>(){});
		assertThat(customers).isNotEmpty();
		assertThat(customers.get(0).getName()).contains("barbara");
		assertThat(customers.get(0).getNif().equals("298973534"));
	}

	@Test
	void shouldReturnListOfCustomersNotFound() throws Exception {
		//make request
		mockMvc.perform(
				get("/customers").param("name", "fernando"))
				.andExpect(status().isNotFound());
	}

	@Test
	void shouldCreateNewCustomer() throws Exception {

		final String body = "{\n" +
				"    \"name\" : \"alexandra costa\",\n" +
				"    \"nif\" : \"987654321\",\n" +
				"    \"email\" : \"alexandra@hotmail.com\",\n" +
				"    \"list\" : [		]\n" +
				"}";

		mockMvc.perform(post("/customers")
				.header("Content-Type", "application/json")
				.content(body))
				.andExpect(status().isOk());
	}

	@Test
	void shouldNotPermitCreateNewCustomerWithExistentNif() throws Exception {

		final String body = "{\n" +
				"    \"name\" : \"maria costa\",\n" +
				"    \"nif\" : \"237451628\",\n" +
				"    \"email\" : \"maria@hotmail.com\",\n" +
				"    \"list\" : [		]\n" +
				"}";
		mockMvc.perform(post("/customers")
				.header("Content-Type", "application/json")
				.content(body))
				.andExpect(status().isOk());
		try {
			mockMvc.perform(post("/customers")
					.header("Content-Type", "application/json")
					.content(body))
					.andExpect(status().isInternalServerError());
		} catch (Exception e) {
			assertThat(e).hasCauseInstanceOf(DataIntegrityViolationException.class);
		}
	}

	@Test
	void shouldCreateNewCustomerInvalidNameBadRequest() throws Exception {

		final String body = "{\n" +
				"    \"name\" : \"\",\n" +
				"    \"nif\" : \"524361782\",\n" +
				"    \"email\" : \"joao@gmail.com\",\n" +
				"    \"list\" : [		]\n" +
				"}";

		mockMvc.perform(post("/customers")
				.header("Content-Type", "application/json")
				.content(body))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldCreateNewCustomerInvalidNifBadRequest() throws Exception {

		final String body = "{\n" +
				"    \"name\" : \"diogo\",\n" +
				"    \"nif\" : \"\",\n" +
				"    \"email\" : \"diogo@gmail.com\",\n" +
				"    \"list\" : [		]\n" +
				"}";

		mockMvc.perform(post("/customers")
				.header("Content-Type", "application/json")
				.content(body))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldCreateNewCustomerInvalidEmailBadRequest() throws Exception {

		final String body = "{\n" +
				"    \"name\" : \"ana\",\n" +
				"    \"nif\" : \"235637189\",\n" +
				"    \"email\" : \"ana.com\",\n" +
				"    \"list\" : [		]\n" +
				"}";

		mockMvc.perform(post("/customers")
				.header("Content-Type", "application/json")
				.content(body))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldUpdateCustomer() throws Exception {
		final String body = "{\n" +
				"    \"name\" : \"diogo\",\n" +
				"    \"nif\" : \"172635445\",\n" +
				"    \"email\" : \"diogo@gmail.com\",\n" +
				"    \"list\" : [		]\n" +
				"}";

		MvcResult mvcResult = mockMvc.perform(post("/customers")
				.header("Content-Type", "application/json")
				.content(body))
				.andExpect(status().isOk())
				.andReturn();

		ObjectMapper mapper = new ObjectMapper();
		CustomerResponse customer = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				CustomerResponse.class);

		assertThat(customer.getName()).isEqualTo("diogo");
		assertThat(customer.getNif()).isEqualTo("172635445");
		assertThat(customer.getEmail()).isEqualTo("diogo@gmail.com");

		final String updateBody = "{\n" +
				"    \"name\" : \"diogo costa\",\n" +
				"    \"nif\" : \"172635445\",\n" +
				"    \"email\" : \"diogo@gmail.com\",\n" +
				"    \"list\" : [		]\n" +
				"}";

		MvcResult mvcResultUpdate = mockMvc.perform(put("/customers/" + customer.getId())
				.header("Content-Type", "application/json")
				.content(updateBody))
				.andExpect(status().isOk())
				.andReturn();

		ObjectMapper mapperUpdate = new ObjectMapper();
		CustomerResponse customerUpdate = mapperUpdate.readValue(mvcResultUpdate.getResponse().getContentAsString(),
				CustomerResponse.class);

		assertThat(customerUpdate).isNotNull();
		assertThat(customerUpdate.getId()).isPositive();
		assertThat(customerUpdate.getName()).isEqualTo("diogo costa");
		assertThat(customerUpdate.getNif()).isEqualTo("172635445");
		assertThat(customerUpdate.getEmail()).isEqualTo("diogo@gmail.com");
	}

	@Test
	void shouldDeleteCustomer() throws Exception {
		final String body = "{\n" +
				"    \"name\" : \"manuel\",\n" +
				"    \"nif\" : \"623451786\",\n" +
				"    \"email\" : \"manuel@hotmail.com\",\n" +
				"    \"list\" : [		]\n" +
				"}";

		MvcResult mvcResult = mockMvc.perform(post("/customers")
				.header("Content-Type", "application/json")
				.content(body))
				.andExpect(status().isOk())
				.andReturn();

		ObjectMapper mapper = new ObjectMapper();
		CustomerResponse customer = mapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerResponse.class);
		assertThat(customer).isNotNull();
		assertThat(customer.getId()).isPositive();
		assertThat(customer.getName()).isEqualTo("manuel");
		assertThat(customer.getNif()).isEqualTo("623451786");
		assertThat(customer.getEmail()).isEqualTo("manuel@hotmail.com");

		// Make request
		mockMvc.perform(delete("/customers/" + customer.getId())
				.header("Content-Type", "application/json"))
				.andExpect(status().isOk());

	}


}

package digit.repository;

import digit.web.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return The Customer object if found; otherwise, null.
     */
    public Customer getCustomerById(String customerId) {
        String sql = "SELECT * FROM customer WHERE id = ?";

        try {
            // Use queryForObject to retrieve a single customer
            return jdbcTemplate.queryForObject(sql, new Object[]{customerId}, new BeanPropertyRowMapper<>(Customer.class));
        } catch (EmptyResultDataAccessException e) {
            // Return null if no customer is found with the given ID
            return null;
        }
    }
}

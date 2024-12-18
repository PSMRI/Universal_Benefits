package digit.service;

import digit.repository.CustomerRepository;
import digit.web.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return The Customer object if found; otherwise, null.
     */
    public Customer getCustomerById(String customerId) {
        Customer customer = null;
        try {
            customer = customerRepository.getCustomerById(customerId);
        } catch (Exception e) {
            // Log error details
            System.err.println("Error fetching customer by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return customer;
    }

}

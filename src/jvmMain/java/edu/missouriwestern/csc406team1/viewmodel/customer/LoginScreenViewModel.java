package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.CustomerRepository;
import org.jetbrains.annotations.NotNull;

public class LoginScreenViewModel {

    private CustomerRepository customerRepository;

    public LoginScreenViewModel(
            CustomerRepository customerRepository
    ) {
        this.customerRepository = customerRepository;
    }

    @NotNull
    public Boolean doesCustomerExist(String ssn) {
        return customerRepository.getCustomer(ssn) != null;
    }

    @NotNull
    public String filterSSN(String ssn) {
        // remove junk and keep value less than 10
        String newString = ssn.replaceAll("\\D", "");
        if (newString.length() > 9) {
            newString = newString.substring(0, 9);
        }
        return newString;
    }
}

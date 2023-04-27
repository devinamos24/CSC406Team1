package edu.missouriwester.csc406team1;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.database.AccountRepositoryImpl;
import edu.missouriwestern.csc406team1.database.CustomerRepositoryImpl;
import edu.missouriwestern.csc406team1.database.TransactionRepository;
import edu.missouriwestern.csc406team1.database.model.Customer;
import edu.missouriwestern.csc406team1.database.model.account.Account;
import edu.missouriwestern.csc406team1.database.model.account.TMBAccount;
import edu.missouriwestern.csc406team1.viewmodel.customer.DepositMoneyScreenViewModel;
import kotlin.Unit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

public class DepositMoneyScreenViewModelTest {

    @Mock
    private CustomerRepositoryImpl customerRepository;

    @Mock
    private AccountRepositoryImpl accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private DepositMoneyScreenViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new DepositMoneyScreenViewModel(customerRepository, accountRepository, transactionRepository, "423453245", "6", () -> Unit.INSTANCE);
    }

    @Test
    public void testGetCustomers() {
        ArrayListFlow<Customer> customers = new ArrayListFlow<>();

        Customer customer = new Customer(
                "423453245",
                "114 North 4th",
                "Clarksdale",
                "MO",
                "64493",
                "Ronald",
                "Jones"
        );

        customers.add(customer);

        given(customerRepository.getCustomers()).willReturn(customers);
        assertEquals(customers.getList(), viewModel.getCustomers().getValue());
    }

    @Test
    public void testGetAccounts() {
        ArrayListFlow<Account> accounts = new ArrayListFlow<>();

        Account account = new TMBAccount(
                "6",
                "423453245",
                843.83,
                LocalDate.now(),
                true,
                false,
                0
        );

        accounts.add(account);

        given(accountRepository.getAccounts()).willReturn(accounts);
        assertEquals(accounts.getList(), viewModel.getAccounts().getValue());
    }

    @Test
    public void testGetSsn() {
        assertEquals("423453245", viewModel.getSsn());
    }

    @Test
    public void testGetId() {
        assertEquals("6", viewModel.getId());
    }

    @Test
    public void givenLetter_whenOnAmountChange_thenReturnUnchangedAmount() {
        viewModel.onAmountChange("abc");
        assertEquals("", viewModel.getAmount().getValue().getValue());
    }

    @Test
    public void givenNumber_whenOnAmountChange_thenReturnChangedAmount() {
        viewModel.onAmountChange("123");
        assertEquals("123", viewModel.getAmount().getValue().getValue());
    }

    @Test
    public void testOnDeposit() {
        //TODO: finish this test
    }

}

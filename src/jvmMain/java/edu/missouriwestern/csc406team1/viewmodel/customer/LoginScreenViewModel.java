package edu.missouriwestern.csc406team1.viewmodel.customer;

import edu.missouriwestern.csc406team1.database.CustomerRepository;
import edu.missouriwestern.csc406team1.util.InputValidator;
import edu.missouriwestern.csc406team1.util.InputWrapper;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
import org.jetbrains.annotations.NotNull;

public class LoginScreenViewModel {

    /**
     * customerRepository is used to check and see if a customer exists in the database
     */
    private final CustomerRepository customerRepository;
    /**
     * hasFailed is used to keep track of if the user has tried to log in with bad data
     * <p>
     * The reason we wrap it in a mutable state flow is because kotlin flows are observables.
     * What that means is that they will let whoever is observing them know if they changed.
     * This means that our UI will always know when the data changes, so it can update accordingly.
     * That is why our app always shows up-to-date data (except for that fail I did while presenting lol).
     */
    private final MutableStateFlow<Boolean> hasFailed = StateFlowKt.MutableStateFlow(false);
    /**
     * ssn is used to keep track of the ssn entered into the screen.
     * <p>
     * Every time the user enters a number we will update this value so that the UI shows what they entered.
     * This means that the ssn on the screen is not actually stored in the screen code, it is stored here.
     * This allows us to test if the screen is working without even testing the screen, we can just test the view-model.
     * I would still recommend writing instrumented tests for the screen but this is a good start!
     * <p>
     * You might also notice that we are using an InputWrapper.
     * All that does it contain the input as well as the error message if an error occurred.
     */
    private final MutableStateFlow<InputWrapper> ssn = StateFlowKt.MutableStateFlow(new InputWrapper());
    /**
     * onClickLogin is a functional interface that we call when the user clicks the login button
     * <p>
     * A functional interface is one that has only one function in it.
     * The cool thing about these in Kotlin is that you can use lambda notation to represent them.
     * The downside to this notation is that Java actually does not understand it.
     * We get around this by using the FunctionX interfaces they provide for us.
     * These represent kotlin lambdas in a form that Java can digest.
     * The types you see on it are like this: (P1, R).
     * <p>
     * P1 means the first parameter to the function
     * <p>
     * R means the return value of the function
     * <p>
     * This functional interface's lambda can then be called using its invoke(P1) method.
     * Also, the Unit return value is the equivalent to Java's void return value, it means we return nothing.
     * This might seem confusing at first but if you have any questions please ask me, I am always available.
     */
    private final Function1<String, Unit> onClickLogin;
    /**
     * onBack is a functional interface that we call when the user clicks the back button
     * <p>
     * This function returns nothing because all it is supposed to do it pop this screen off the screen stack.
     * Every view-models will have this because every screen will have a back button.
     * The first screen you see will not need a view-model because it does not have any state.
     * You could technically still give it a view-model, but I think it is overkill because we
     * really do not need to test that screen.
     */
    private final Function0<Unit> onBack;

    /**
     * Our constructor will only instantiate our variables.
     * The constructor of a viewmodel can do other things as well, but we only need these for now.
     * @param customerRepository repository with all of our customers in it
     * @param onClickLogin callback for the login button
     * @param onBack callback for the back button
     */
    public LoginScreenViewModel(
            CustomerRepository customerRepository,
            Function1<String, Unit> onClickLogin,
            Function0<Unit> onBack
    ) {
        this.customerRepository = customerRepository;
        this.onClickLogin = onClickLogin;
        this.onBack = onBack;
    }

    /**
     * This function simply invokes the onBack functional interface.
     */
    public void onBack() {
        onBack.invoke();
    }

    /**
     * This function is called when the user clicks the login button.
     * If the entered ssn is not in the repository we set the failed value to true.
     * If the entered ssn is in the repository we invoke our onClickLogin callback with the given ssn.
     * There is technically a problem with the way this is set up, if you can figure it out let me know!
     */
    public void onClickLogin() {
        if (customerRepository.getCustomer(ssn.getValue().getValue()) != null) {
            onClickLogin.invoke(ssn.getValue().getValue());
        } else {
            hasFailed.setValue(true);
        }
    }

    /**
     * This function is called when the user types anything into the ssn box.
     * The purpose of this function is to filter anything that is not a number.
     * This function also cuts the excess off if they enter more than 9 numbers.
     * @param ssn the new string after each keystroke
     */
    public void onSSNChange(String ssn) {
        String newString = ssn.replaceAll("\\D", "");
        if (newString.length() > 9) {
            newString = newString.substring(0, 9);
        }
        // Don't forget to set the error message using my InputValidator functions!
        this.ssn.setValue(new InputWrapper(newString, InputValidator.INSTANCE.getSSNErrorOrNull(newString)));
    }

    /**
     * This function is used by our UI to get access to the ssn flow.
     * You will notice that it returns a StateFlow instead of a MutableStateFlow.
     * This is because the UI should not have the ability to edit this data, only the view-model should.
     * @return StateFlow with our ssn in it
     */
    @NotNull
    public StateFlow<InputWrapper> getSSN() {
        return FlowKt.asStateFlow(ssn);
    }

    /**
     * This function is used by our UI to know if the user has entered an invalid ssn before.
     * @return StateFlow with our hasFailed boolean
     */
    @NotNull
    public StateFlow<Boolean> getHasFailed() {
        return FlowKt.asStateFlow(hasFailed);
    }

}

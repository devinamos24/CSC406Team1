package edu.missouriwestern.csc406team1.database;

import edu.missouriwestern.csc406team1.ArrayListFlow;
import edu.missouriwestern.csc406team1.util.CSVWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountDaoImpl implements AccountDao{

    //modified arrayList of account objects
    @NotNull
    private final ArrayListFlow<Account> accounts = new ArrayListFlow<>();

    //The filename for the starting data,
    private final String basefilename = "/account_base.csv";
    //the filename for the new data to be saved to
    private final String filename = "/account.csv";

    /*
        This constructor attempts to populate the modified arrayList of accounts from disk
     */
    public AccountDaoImpl(){
        //Create a list of string arrays to hold each piece of account data
        List<String[]> collect = new ArrayList<>();

        //Try to open a stream of data from the saved account file
        //TODO finish the rest of this DaoImpl
    }

}

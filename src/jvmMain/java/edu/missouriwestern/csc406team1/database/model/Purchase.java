package edu.missouriwestern.csc406team1.database.model;

import edu.missouriwestern.csc406team1.util.CSV;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

// Purchase.java
public class Purchase extends Transaction{
    private String merchant;
    private String category;

    public Purchase(@NotNull String transactionID, @NotNull Boolean credit, @NotNull Boolean debit, @NotNull String transactionType,
                    @NotNull Double amount, @NotNull Double newTotal, @NotNull String accID, @NotNull LocalDate date,
                    @NotNull LocalTime time, String merchant, String category) {
        super(transactionID, credit, debit, transactionType, amount, newTotal, accID, date, time);
        this.merchant = merchant;
        this.category = category;
    }

    // Getters
    public String getMerchant() {
        return merchant;
    }

    public String getCategory() {
        return category;
    }

    // Setters
    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

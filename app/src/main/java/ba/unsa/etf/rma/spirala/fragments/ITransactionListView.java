package ba.unsa.etf.rma.spirala.fragments;

import java.util.ArrayList;

import ba.unsa.etf.rma.spirala.models.Transaction;

public interface ITransactionListView {

    void setTransactions(ArrayList<Transaction> transactions);
    void notifyTransactionListDataSetChanged();
    void clearListOfTransactions();
    void sort(String string);
}

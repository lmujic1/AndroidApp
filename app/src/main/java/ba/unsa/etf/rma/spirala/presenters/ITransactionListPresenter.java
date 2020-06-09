package ba.unsa.etf.rma.spirala.presenters;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;

import ba.unsa.etf.rma.spirala.models.Transaction;

public interface ITransactionListPresenter {
    public void getTransactions(String query);
    public void getTransactionOnDate(Date date);
    public void getSortTransaction(String string);
    void getFilteredTransacion(String selectedItemText);
    void addTransaction(Transaction nova);
    void deleteTransaction(Transaction izabranaTransakcija);
    void editTransaction(Transaction nova, Transaction izabranaTransakcija);

    void transactionFromDB(ArrayList<Transaction> getTransactionFromDatabase);
    void addedTransactionDB(Transaction nova);
    void deleteTransactionDB(Transaction izabranaTransakcija);
    void editTransactionDB(Transaction stara,Transaction nova);

    void getTransactionCursor();

    void deleteTransactionOff(Transaction t);
}

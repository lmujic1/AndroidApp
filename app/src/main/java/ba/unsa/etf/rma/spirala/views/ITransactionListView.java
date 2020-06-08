package ba.unsa.etf.rma.spirala.views;

import android.database.Cursor;

import java.util.ArrayList;

import ba.unsa.etf.rma.spirala.models.Transaction;

public interface ITransactionListView {

    void setTransactions(ArrayList<Transaction> transactions);
    void notifyTransactionListDataSetChanged();
    void clearListOfTransactions();

    void sort(String string);

    void removeTransaction(Transaction transaction);
    void addTransaction(Transaction trans);

    void editingTransaction(Transaction stara, Transaction nova);
}

package ba.unsa.etf.rma.spirala.interactors;


import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import ba.unsa.etf.rma.spirala.models.Transaction;

public interface ITransactionListInteractor {
    ArrayList<Transaction> getTransactionCursor(Context context);
    void deleteTransaction(Context context, int idTransasakcije);
    void saveTransaction(Transaction transaction, Context context);
    void editTransaction(Context context, Transaction transaction);
    Transaction getTransaction(Context context, int id);

    void editTransactionACD(Context context, Transaction izabranaTransakcija);
}

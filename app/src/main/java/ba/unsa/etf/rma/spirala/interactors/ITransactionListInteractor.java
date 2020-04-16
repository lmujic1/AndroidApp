package ba.unsa.etf.rma.spirala.interactors;

import java.util.ArrayList;
import java.util.Date;

import ba.unsa.etf.rma.spirala.models.Transaction;

public interface ITransactionListInteractor {
    ArrayList<Transaction> get();
    ArrayList<Transaction> getOnMonth(Date date);
}

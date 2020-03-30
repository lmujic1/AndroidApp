package ba.unsa.etf.rma.spirala.interactors;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.spirala.models.Transaction;
import ba.unsa.etf.rma.spirala.models.TransactionModel;

public class TransactionListInteractor {
    public ArrayList<Transaction> get() {
        return TransactionModel.getTransactions();
    }
}

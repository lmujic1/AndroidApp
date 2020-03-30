package ba.unsa.etf.rma.spirala.presenters;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.spirala.interactors.TransactionListInteractor;
import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionListPresenter {

    private TransactionListInteractor transactionListInteractor;

    public TransactionListPresenter() {
        transactionListInteractor = new TransactionListInteractor();
    }

    public ArrayList<Transaction> getTransactions() {
        return transactionListInteractor.get();
    }

}

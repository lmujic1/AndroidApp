package ba.unsa.etf.rma.spirala.presenters;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ba.unsa.etf.rma.spirala.fragments.ITransactionListView;
import ba.unsa.etf.rma.spirala.interactors.ITransactionListInteractor;
import ba.unsa.etf.rma.spirala.interactors.TransactionListInteractor;
import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionListPresenter implements ITransactionListPresenter {

    private ITransactionListView view;
    private ITransactionListInteractor interactor;
    private Context context;

    public TransactionListPresenter(ITransactionListView view, Context context) {
        this.view = view;
        this.interactor = new TransactionListInteractor();
        this.context = context;
    }


    @Override
    public void refreshTransaction() {
        view.setTransactions(interactor.get());
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void refreshTransactionOnDate(Date date) {
        view.clearListOfTransactions();
        view.setTransactions(interactor.getOnMonth(date));
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void refreshTransactionSort(String string) {
        view.sort(string);
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void refreshTransactionFilter(Date date, String string) {
        view.clearListOfTransactions();
        ArrayList<Transaction> transactions = new ArrayList<>(), pom = new ArrayList<>();
        transactions.addAll(interactor.getOnMonth(date));
        pom.addAll(filter(string, transactions));
        view.setTransactions(pom);
        view.notifyTransactionListDataSetChanged();
    }

    public ArrayList<Transaction> filter(String filter, ArrayList<Transaction> transactions) {
        ArrayList<Transaction> filtrirani = new ArrayList<>();
        for (Transaction t : transactions) {
            if (!t.getType().toString().equals(filter)) filtrirani.add(t);
        }
        transactions.removeAll(filtrirani);
        return transactions;
    }

}

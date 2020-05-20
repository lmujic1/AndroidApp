package ba.unsa.etf.rma.spirala.presenters;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Date;

import ba.unsa.etf.rma.spirala.views.ITransactionListView;
import ba.unsa.etf.rma.spirala.activities.PocetnaAktivnost;
import ba.unsa.etf.rma.spirala.interactors.TransactionListInteractor;
import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionListPresenter implements ITransactionListPresenter, TransactionListInteractor.OnTransactionGetDone {

    private ITransactionListView view;
    private Context context;

    public TransactionListPresenter(ITransactionListView view, Context context) {
        this.view = view;
        this.context = context;
    }


    @Override
    public void getTransactions(String query) {
        new TransactionListInteractor((TransactionListInteractor.OnTransactionGetDone) this).execute(query);
    }

    @Override
    public void getTransactionOnDate(Date date) {
        String monthString = (String) DateFormat.format("MM", date);
        System.out.println(monthString + "TREBA MI OVAJ MJESEC ");
        String yearString = (String) DateFormat.format("yyyy", date);
        String query = "/filter?month=" + monthString + "&year=" + yearString;
        new TransactionListInteractor((TransactionListInteractor.OnTransactionGetDone) this).execute(query);
    }

    @Override
    public void getSortTransaction(String string) {
        String monthString = (String) DateFormat.format("MM", PocetnaAktivnost.defaultDate);
        String yearString = (String) DateFormat.format("yyyy", PocetnaAktivnost.defaultDate);
        String sortType;
        switch (string) {
            case "Price - Ascending":
                sortType = "&sort=amount.asc";
                break;
            case "Price - Descending":
                sortType = "&sort=amount.desc";
                break;
            case "Title - Ascending":
                sortType = "&sort=title.asc";
                break;
            case "Title - Descending":
                sortType = "&sort=title.desc";
                break;
            case "Date - Ascending":
                sortType = "&sort=date.asc";
                break;
            case "Date - Descending":
                sortType = "&sort=date.desc";
                break;
            default:
                sortType = "";
                break;
        }

        String query = "/filter?month=" + monthString + "&year=" + yearString + sortType;

        new TransactionListInteractor((TransactionListInteractor.OnTransactionGetDone) this).execute(query);

    }

    @Override
    public void getFilteredTransacion(String filterBy) {
        String monthString = (String) DateFormat.format("MM", PocetnaAktivnost.defaultDate);
        String yearString = (String) DateFormat.format("yyyy", PocetnaAktivnost.defaultDate);
        String filterType;
        switch (filterBy) {
            case "Individual payment":
                filterType = "&typeId=5";
                break;
            case "Regular payment":
                filterType = "&typeId=1";
                break;
            case "Purchase":
                filterType = "&typeId=3";
                break;
            case "Individual income":
                filterType = "&typeId=4";
                break;
            case "Regular income":
                filterType = "&typeId=2";
                break;
            default:
                filterType = "";
                break;
        }
        String query = "/filter?month=" + monthString + "&year=" + yearString + filterType;

        new TransactionListInteractor((TransactionListInteractor.OnTransactionGetDone) this).execute(query);

    }


    @Override
    public void onGetDone(ArrayList<Transaction> results) {
        view.clearListOfTransactions();
        view.setTransactions(results);
        view.notifyTransactionListDataSetChanged();
    }




   /*

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

    @Override
    public void refreshTransactionDelete(Transaction transaction) {
        view.removeTransaction(transaction);
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void refreshTransactionAdd(Transaction trans) {
        view.addTransaction(trans);
        view.notifyTransactionListDataSetChanged();
    }

 *//*   @Override
    public void refreshTransactionDelete(Date date, ArrayList<Transaction> transactions) {
        //view.clearListOfTransactions();
        ArrayList<Transaction> transactions1 = new ArrayList<>();
        transactions1.addAll(interactor.getOnMonthAndDeleteTransactions(date,transactions));
        view.setTransactions(transactions1);
        view.notifyTransactionListDataSetChanged();
    }*//*



    public ArrayList<Transaction> filter(String filter, ArrayList<Transaction> transactions) {
        ArrayList<Transaction> filtrirani = new ArrayList<>();
        for (Transaction t : transactions) {
            if (!t.getType().toString().equals(filter)) filtrirani.add(t);
        }
        transactions.removeAll(filtrirani);
        return transactions;
    }*/

}

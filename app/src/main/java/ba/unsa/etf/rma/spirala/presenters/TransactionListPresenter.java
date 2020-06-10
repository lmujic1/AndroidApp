package ba.unsa.etf.rma.spirala.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ba.unsa.etf.rma.spirala.adapters.TransactionListAdapter;
import ba.unsa.etf.rma.spirala.interactors.AccountInteractor;
import ba.unsa.etf.rma.spirala.interactors.TransactionDeleteInteractor;
import ba.unsa.etf.rma.spirala.interactors.TransactionAddInteractor;
import ba.unsa.etf.rma.spirala.interactors.TransactionEditInteractor;
import ba.unsa.etf.rma.spirala.interactors.TransactionListInteractor;
import ba.unsa.etf.rma.spirala.interactors.UpdateAccountInteractor;
import ba.unsa.etf.rma.spirala.views.ITransactionListView;
import ba.unsa.etf.rma.spirala.activities.PocetnaAktivnost;
import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionListPresenter implements ITransactionListPresenter, TransactionListInteractor.OnTransactionGetDone {

    private ITransactionListView view;
    private Context context;
    private TransactionListInteractor transactionListInteractor = new TransactionListInteractor();

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
    public void addTransaction(Transaction nova) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String date = dateFormat.format(nova.getDate());
        Transaction.Type type = nova.getType();
        int typeId = nova.getTypeId(type);
        String query = "{\n" +
                "\"title\" : " + "\"" + nova.getTitle() + "\"" + "," + "\n" +
                "\"date\" : " + "\"" + date + "\"" + "," + "\n" +
                "\"typeId\" : " + typeId + "," + "\n" +
                "\"amount\" : " + nova.getAmount();

        if (typeId == 1 || typeId == 2) {
            String endDate = dateFormat.format(nova.getEndDate());
            query += "," + "\n" + "\"endDate\" : " + "\"" + endDate + "\"" + "\n" +
                    "\"transactionInterval\" : " + nova.getTransactionInterval();
        }
        if (typeId != 2 && typeId != 4) {
            query += "," + "\n" + "\"itemDescription\" : " + "\"" + nova.getItemDescription() + "\"" + "\n";
        }
        query += "\n}";
        System.out.println(query);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, TransactionAddInteractor.class);
        intent.putExtra("query", query);
        context.getApplicationContext().startService(intent);
    }

    @Override
    public void deleteTransaction(Transaction izabranaTransakcija) {
        String query = "/" + izabranaTransakcija.getIdTransaction();
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, TransactionDeleteInteractor.class);
        intent.putExtra("query", query);
        context.getApplicationContext().startService(intent);
    }

    @Override
    public void editTransaction(Transaction nova, Transaction izabranaTransakcija) {
        String query = "{\n";
        if (!nova.getTitle().equals(izabranaTransakcija.getTitle())) {
            query += "\"title\" : " + "\"" + nova.getTitle() + "\"";
        }
        if (nova.getDate() != izabranaTransakcija.getDate()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String date = dateFormat.format(nova.getDate());
            String dat = "\"date\" : " + "\"" + date + "\"";
            if (query != "{\n") query += "," + "\n" + dat;
            else query += dat;
        }
        if (nova.getAmount() != izabranaTransakcija.getAmount()) {
            double v = nova.getAmount();
            String amount = "\"amount\" : " + v;
            if (query != "{\n") query += "," + "\n" + amount;
            else query += amount;
        }
        Integer interval = nova.getTransactionInterval();
        Integer interval2 = izabranaTransakcija.getTransactionInterval();
        if (interval != null && interval != interval2) {
            String interv = "\"transactionInterval\" : " + interval;
            if (query != "{\n") query += "," + "\n" + interv;
            else query += interv;
        }
        if (nova.getItemDescription() != null) {
            if (!nova.getItemDescription().equals(izabranaTransakcija.getItemDescription())) {
                String descrip = nova.getItemDescription();
                if (descrip != null) {
                    String description = "\"itemDescription\" : " + "\"" + descrip + "\"";
                    if (query != "{\n") query += "," + "\n" + description;
                    else query += description;
                }
            }
        }
        if (nova.getEndDate() != null) {
            if (nova.getDate() != izabranaTransakcija.getDate()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String date = dateFormat.format(nova.getEndDate());
                String dat = "\"endDate\" : " + "\"" + date + "\"";
                if (query != "{\n") query += "," + "\n" + dat;
                else query += dat;
            }
        }
        if (nova.getType() != izabranaTransakcija.getType()) {
            Transaction.Type tip = nova.getType();
            int type = nova.getTypeId(tip);
            String typeS = "\"TransactionTypeId\" : " + type;
            if (query != "{\n") query += "," + "\n" + typeS;
            else query += typeS;
        }
        query += "\n}";
        System.out.println(query);
        if (query != "{\n\n}") {
            new TransactionEditInteractor(izabranaTransakcija).execute(query);
        }
    }

    @Override
    public void transactionFromDB(ArrayList<Transaction> getTransactionFromDatabase) {
        view.clearListOfTransactions();
        view.setTransactions(getTransactionFromDatabase);
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void addedTransactionDB(Transaction nova) {
        transactionListInteractor.saveTransaction(nova, context);
        view.addTransaction(nova);
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void deleteTransactionDB(Transaction izabranaTransakcija) {
        transactionListInteractor.deleteTransaction(context, izabranaTransakcija.getIdTransaction());
        TransactionListAdapter.offlineMode.setText(izabranaTransakcija.getOffMode());
    }

    @Override
    public void editTransactionDB(Transaction stara, Transaction nova) {
        transactionListInteractor.editTransaction(context, nova);
        view.editingTransaction(stara, nova);
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void onGetDone(ArrayList<Transaction> results) {
        view.clearListOfTransactions();
        view.setTransactions(results);
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void getTransactionCursor() {
        view.clearListOfTransactions();
        ArrayList<Transaction> transactions = transactionListInteractor.getTransactionCursor(context);
        findTransactionForDeleteOff(transactions);
        view.setTransactions(transactions);
        view.notifyTransactionListDataSetChanged();
    }

    @Override
    public void editTransactionAfterClickDelete(Transaction izabranaTransakcija) {
        transactionListInteractor.editTransactionACD(context, izabranaTransakcija);
    }

    private void findTransactionForDeleteOff(ArrayList<Transaction> transactions) {
        if (PocetnaAktivnost.offlineBrisanje.size() != 0) {
            for (Transaction t : transactions) {
                for (Transaction t1 : PocetnaAktivnost.offlineBrisanje) {
                    if (t.getAmount() == t1.getAmount()
                            && t.getTitle().equals(t1.getTitle())
                            && t.getDate().equals(t1.getDate())
                            && t.getType().equals(t1.getType())) {
                        t1.setIdTransaction(t.getIdTransaction());
                        System.out.println(t.getIdTransaction() + "  cd  " + t1.getIdTransaction());
                    }
                }
            }
        }
    }



}

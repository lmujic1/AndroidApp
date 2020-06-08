package ba.unsa.etf.rma.spirala.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ba.unsa.etf.rma.spirala.R;
import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {
    int resource;
    public static TextView offlineMode;

    public TransactionListAdapter(@NonNull Context context, int resource, ArrayList<Transaction> transactions) {
        super(context, resource, transactions);
        this.resource = resource;
    }

    public Transaction getTransaction(int position) {
        return this.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LinearLayout view;
        if (convertView == null) {
            view = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, view, true);
        } else {
            view = (LinearLayout) convertView;
        }

        Transaction transaction = getItem(position);
        offlineMode = view.findViewById(R.id.offlineMode);
        TextView nTransakcije = view.findViewById(R.id.titleTransaction);
        TextView iTransakcije = view.findViewById(R.id.iznosTransakcije);
        ImageView ikonaTransakcije = view.findViewById(R.id.ikonaTransakcije);
        offlineMode.setText(transaction.getOffMode());
        nTransakcije.setText(transaction.getTitle());
        iTransakcije.setText(String.format("%.2f", transaction.getAmount()));
        String tipTransakcije = transaction.getType().toString();
        switch (tipTransakcije) {
            case "Individual payment":
                ikonaTransakcije.setImageResource(R.drawable.individual_payment);
                break;
            case "Regular payment":
                ikonaTransakcije.setImageResource(R.drawable.regular_payment);
                break;
            case "Purchase":
                ikonaTransakcije.setImageResource(R.drawable.purchase);
                break;
            case "Individual income":
                ikonaTransakcije.setImageResource(R.drawable.individual_income);
                break;
            case "Regular income":
                ikonaTransakcije.setImageResource(R.drawable.regular_income);
                break;
            default:
                ikonaTransakcije.setImageResource(R.drawable.transaction);
        }

        return view;
    }

    public void sortiraj(String sortitajPo) {

        switch (sortitajPo) {
            case "Title - Ascending":
                this.sort(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
                break;
            case "Title - Descending":
                this.sort(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return -a.getTitle().compareTo(b.getTitle());
                    }
                });
                break;
            case "Price - Ascending":
                this.sort(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return Double.compare(a.getAmount(), b.getAmount());
                    }
                });
                break;
            case "Price - Descending":
                this.sort(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return -Double.compare(a.getAmount(), b.getAmount());
                    }
                });
                break;
            case "Date - Ascending":
                this.sort(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return a.getDate().compareTo(b.getDate());
                    }
                });
                break;
            case "Date - Descending":
                this.sort(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return -a.getDate().compareTo(b.getDate());
                    }
                });
                break;
        }
    }


    public void dodajTransakciju(Transaction nova) {
        this.add(nova);
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.addAll(transactions);
    }

    public void clearList() {
        this.clear();
    }

    public void delete(Transaction transaction) {
        this.remove(transaction);
    }

    public void edituj(Transaction stara, Transaction nova) {
        this.remove(stara);
        this.add(nova);
    }
}

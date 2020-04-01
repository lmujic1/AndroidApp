package ba.unsa.etf.rma.spirala.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
    private Context context;
    int resource;
    private ArrayList<Transaction> transakcije;
    private static ArrayList<Transaction> pomocnalista;

    public TransactionListAdapter(@NonNull Context context, int resource, ArrayList<Transaction> transactions) {

        super(context, resource, transactions);
        this.context = context;
        this.resource = resource;
        this.transakcije = transactions;
        this.pomocnalista = transactions;
    }

   /* @Override
    public int getCount() {
        return transakcije.size();
    }
*/

    public Transaction getTransaction(int position) {
        return transakcije.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater li;
            li = LayoutInflater.from(context);
            view = li.inflate(R.layout.lista_transakcija, null);
        }

        Transaction transaction = getItem(position);
        if (transaction != null) {
            TextView nTransakcije = view.findViewById(R.id.titleTransaction);
            TextView iTransakcije = view.findViewById(R.id.iznosTransakcije);
            ImageView ikonaTransakcije = view.findViewById(R.id.ikonaTransakcije);
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
        }

        return view;
    }

    public void sortiraj(String sortitajPo) {
        switch (sortitajPo) {
            case "Title - Ascending":
                Collections.sort(transakcije, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });
                break;
            case "Title - Descending":
                Collections.sort(transakcije, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return -a.getTitle().compareTo(b.getTitle());
                    }
                });
                break;
            case "Price - Ascending":
                Collections.sort(transakcije, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return Double.compare(a.getAmount(), b.getAmount());
                    }
                });
                break;
            case "Price - Descending":
                Collections.sort(transakcije, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return -Double.compare(a.getAmount(), b.getAmount());
                    }
                });
                break;
            case "Date - Ascending":
                Collections.sort(transakcije, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return a.getDate().compareTo(b.getDate());
                    }
                });
                break;
            case "Date - Descending":
                Collections.sort(transakcije, new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return -a.getDate().compareTo(b.getDate());
                    }
                });
                break;
        }
        notifyDataSetChanged();
    }

    public void filtriraj(String filter) {
        ArrayList<Transaction> filtrirani = new ArrayList<>();

        if(transakcije.isEmpty()) {
            System.out.println("prazna");
            transakcije.addAll(pomocnalista);}
        for (Transaction t : transakcije) {
            if (!t.getType().toString().equals(filter)) filtrirani.add(t);
        }
        transakcije.removeAll(filtrirani);
        notifyDataSetChanged();
    }

    public void izbrisiTransakciju(Transaction izabranaTransakcija) {
        transakcije.remove(izabranaTransakcija);
        notifyDataSetChanged();
    }

    public void dodajTransakciju(Transaction nova) {
        transakcije.add(nova);
        notifyDataSetChanged();
    }
}

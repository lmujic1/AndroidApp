package ba.unsa.etf.rma.spirala.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ba.unsa.etf.rma.spirala.R;
import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {
    private Context context;
   // private ArrayList<Transaction> transakcije;

    public TransactionListAdapter(@NonNull Context context, int resource, ArrayList<Transaction> transactions) {
        super(context, resource,transactions);
        this.context = context;
      //  this.transakcije = transactions;
    }

   /* @Override
    public int getCount() {
        return transakcije.size();
    }

    @Nullable
    @Override
    public Transaction getItem(int position) {
        return transakcije.get(position);
    }*/

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater li;
            li = LayoutInflater.from(context);
            view=li.inflate(R.layout.just_text,null);
        }

        Transaction transaction = getItem(position);
        if (transaction != null) {
            TextView nTransakcija = view.findViewById(R.id.text);
            nTransakcija.setText(transaction.getTitle());
        }
        return view;
    }
}

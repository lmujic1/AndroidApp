package ba.unsa.etf.rma.spirala.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import ba.unsa.etf.rma.spirala.R;
import ba.unsa.etf.rma.spirala.models.Transaction;

public class FiltrirajAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> listaTipovaTransakcija;

    public FiltrirajAdapter(Context context) {
        this.context = context;
        this.listaTipovaTransakcija = new ArrayList<>(Arrays.asList("Filter by", "All transactions", "Individual payment", "Regular payment", "Purchase", "Individual income", "Regular income"));
    }

    @Override
    public int getCount() {
        return listaTipovaTransakcija.size();
    }

    @Override
    public Object getItem(int position) {
        return listaTipovaTransakcija.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li;
            li = LayoutInflater.from(context);
            view = li.inflate(R.layout.custum_spinner, parent, false);
        }
        TextView nazivKatergorijeZaFiltriranje = view.findViewById(R.id.textSpinner);
        ImageView ikonaKategorije = view.findViewById(R.id.ikonaSpinner);
        nazivKatergorijeZaFiltriranje.setText(listaTipovaTransakcija.get(position));
        String tipTransakcije = listaTipovaTransakcija.get(position);
        switch (tipTransakcije) {
            case "Individual payment":
                ikonaKategorije.setImageResource(R.drawable.individual_payment);
                break;
            case "Regular payment":
                ikonaKategorije.setImageResource(R.drawable.regular_payment);
                break;
            case "Purchase":
                ikonaKategorije.setImageResource(R.drawable.purchase);
                break;
            case "Individual income":
                ikonaKategorije.setImageResource(R.drawable.individual_income);
                break;
            case "Regular income":
                ikonaKategorije.setImageResource(R.drawable.regular_income);
                break;
            case "All transactions":
                ikonaKategorije.setImageResource(R.drawable.transaction);
                break;
        }
        return view;
    }


}

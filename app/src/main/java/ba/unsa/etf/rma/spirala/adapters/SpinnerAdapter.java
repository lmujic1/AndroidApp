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

public class SpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> listaTipovaTransakcija;

    public SpinnerAdapter(Context context) {
        this.context = context;
    }

    public void dodajFiltereZaSortiranje() {
        this.listaTipovaTransakcija = new ArrayList<String>(Arrays.asList("Sort by", "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending", "Date - Ascending", "Date - Descending"));
    }

    public void dodajTipoveTransakcija() {
        this.listaTipovaTransakcija = new ArrayList<>(Arrays.asList("Select type","Regular payment","Regular income","Purchase","Individual income","Individual payment"));
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
        if(view == null) {
            LayoutInflater li;
            li=LayoutInflater.from(context);
            view=li.inflate(R.layout.just_text,parent,false);
        }
        TextView nazivKatergorijeZaFiltriranje = view.findViewById(R.id.text);
        nazivKatergorijeZaFiltriranje.setText(listaTipovaTransakcija.get(position));
        return view;
    }



}

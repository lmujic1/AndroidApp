package ba.unsa.etf.rma.spirala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.stream.Collectors;

import ba.unsa.etf.rma.spirala.adapters.FiltrirajAdapter;
import ba.unsa.etf.rma.spirala.adapters.SpinnerAdapter;
import ba.unsa.etf.rma.spirala.adapters.TransactionListAdapter;
import ba.unsa.etf.rma.spirala.models.Transaction;
import ba.unsa.etf.rma.spirala.presenters.TransactionListPresenter;

public class MainActivity extends AppCompatActivity {

    private TextView tVAmount;
    private TextView tVLimit;
    private Spinner sFilriraj;
    private ImageButton datumPrije;
    private TextView tVDatum;
    private ImageButton datumPoslije;
    private Spinner sSortiraj;
    private ListView lVTransakcije;
    private Button dodajTransakciju;

    private ArrayList<String> listaFiltera, listaSortiraj;
    private FiltrirajAdapter filtrirajAdapter;
    private SpinnerAdapter sortirajAdapter;

    public TransactionListAdapter transactionListAdapter;
    private TransactionListPresenter transactionListPresenter = new TransactionListPresenter();
    private ArrayList<Transaction> listaTransakcija= new ArrayList<>();

    private Date datum = new Date();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");

    private AdapterView.OnItemClickListener listaTransakcijaCLickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent transactionDetailIntent = new Intent(MainActivity.this, TransactionActivitiy.class);
                    Transaction transaction = transactionListAdapter.getTransaction(position);

                    transactionDetailIntent.putExtra("title", transaction.getTitle());
                    transactionDetailIntent.putExtra("Transaction", transaction);
                    MainActivity.this.startActivity(transactionDetailIntent);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listaTransakcija.addAll(transakcijeZaMjesec(transactionListPresenter.getTransactions(),datum));

        tVAmount = (TextView) findViewById(R.id.tVAmount);
        tVLimit = (TextView) findViewById(R.id.tVLimit);
        sFilriraj = (Spinner) findViewById(R.id.sFilterBy);
        datumPrije = (ImageButton) findViewById(R.id.iVLijevo);
        tVDatum = (TextView) findViewById(R.id.tVDatum);
        datumPoslije = (ImageButton) findViewById(R.id.iVDesno);
        sSortiraj = (Spinner) findViewById(R.id.sSortBy);
        lVTransakcije = (ListView) findViewById(R.id.lVTransakcije);
        dodajTransakciju = (Button) findViewById(R.id.bDodajTransakciju);

        tVAmount.setText("Global amount: ");
        tVLimit.setText("Limit: ");


        transactionListAdapter = new TransactionListAdapter(this, R.layout.lista_transakcija,listaTransakcija);
        lVTransakcije.setAdapter(transactionListAdapter);

        listaFiltera = new ArrayList<>(Arrays.asList("Filter by", "All transactions", "Individual payment", "Regular payment", "Purchase", "Individual income", "Regular income"));
        filtrirajAdapter = new FiltrirajAdapter(this, listaFiltera);
        sFilriraj.setAdapter(filtrirajAdapter);
        sFilriraj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listaTransakcija = transakcijeZaMjesec(transactionListPresenter.getTransactions(),datum);
                transactionListAdapter = new TransactionListAdapter (context,R.layout.lista_transakcija,listaTransakcija);
                lVTransakcije.setAdapter(transactionListAdapter);
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(!selectedItemText.equals("All transactions")) transactionListAdapter.filtriraj(selectedItemText);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        listaSortiraj = new ArrayList<String>(Arrays.asList("Sort by", "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending", "Date - Ascending", "Date - Descending"));
        sortirajAdapter = new SpinnerAdapter(this, listaSortiraj);
        sSortiraj.setAdapter(sortirajAdapter);
        sSortiraj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItemText = (String) parentView.getItemAtPosition(position);
                transactionListAdapter.sortiraj(selectedItemText);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        lVTransakcije.setOnItemClickListener(listaTransakcijaCLickListener);

        String date = dateFormat.format(datum);
        tVDatum.setText(date);

        datumPrije.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Date dat = getDatum((String) tVDatum.getText());
                                              Calendar calendar = Calendar.getInstance();
                                              calendar.setTime(dat);
                                              calendar.add(Calendar.MONTH, -1);
                                              Date monthBefore = calendar.getTime();
                                              datum=monthBefore;
                                              tVDatum.setText(dateFormat.format(monthBefore));
                                              listaTransakcija.clear();
                                              listaTransakcija.addAll(transakcijeZaMjesec(transactionListPresenter.getTransactions(),monthBefore));
                                              transactionListAdapter = new TransactionListAdapter(context, R.layout.lista_transakcija,listaTransakcija);
                                              lVTransakcije.setAdapter(transactionListAdapter);
                                          }
                                      }
        );
        datumPoslije.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Date dat = getDatum((String) tVDatum.getText());
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(dat);
                                                calendar.add(Calendar.MONTH, 1);
                                                Date monthAfter = calendar.getTime();
                                                datum=monthAfter;
                                                tVDatum.setText(dateFormat.format(monthAfter));
                                                listaTransakcija.clear();
                                                listaTransakcija.addAll(transakcijeZaMjesec(transactionListPresenter.getTransactions(),monthAfter));
                                                transactionListAdapter = new TransactionListAdapter(context, R.layout.lista_transakcija,listaTransakcija);
                                                lVTransakcije.setAdapter(transactionListAdapter);
                                            }
                                        }
        );

        dodajTransakciju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private Date getDatum(String text) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");
        Date datum = null;
        try {
            datum = dateFormat.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datum;
    }


    private Context context = this;



    public ArrayList<Transaction> transakcijeZaMjesec(ArrayList<Transaction> transactions, Date date) {
        ArrayList<Transaction> trans = new ArrayList<>();

        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        long mjesec = cal.get(Calendar.MONTH) + 1, godina = cal.get(Calendar.YEAR);
        for (Transaction t : transactions) {
            Calendar c1 = new GregorianCalendar();
            c1.setTime(t.getDate());
            int tmj = c1.get(Calendar.MONTH) + 1, tgod = c1.get(Calendar.YEAR);
            if (tmj == mjesec && tgod == godina)
                trans.add(t);
        }

        return trans;
    }
}

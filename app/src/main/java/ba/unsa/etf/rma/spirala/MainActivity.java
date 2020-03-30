package ba.unsa.etf.rma.spirala;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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

import ba.unsa.etf.rma.spirala.adapters.FiltrirajAdapter;
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
    private FiltrirajAdapter filtrirajAdapter, sortirajAdapter;

    private TransactionListAdapter transactionListAdapter;
    private TransactionListPresenter transactionListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tVAmount = (TextView) findViewById(R.id.tVAmount);
        tVLimit = (TextView) findViewById(R.id.tVLimit);
        sFilriraj = (Spinner) findViewById(R.id.sFilterBy);
        datumPrije = (ImageButton) findViewById(R.id.iVLijevo);
        tVDatum = (TextView) findViewById(R.id.tVDatum);
        datumPoslije = (ImageButton) findViewById(R.id.iVDesno);
        sSortiraj = (Spinner) findViewById(R.id.sSortBy);
        lVTransakcije = (ListView) findViewById(R.id.lVTransakcije);
        dodajTransakciju = (Button) findViewById(R.id.bDodajTransakciju);


        listaFiltera = new ArrayList<>(Arrays.asList("Filter by", "Individual payment", "Reguler payment", "Purchase", "Individual income", "Regular income"));
        filtrirajAdapter = new FiltrirajAdapter(this, listaFiltera);
        sFilriraj.setAdapter(filtrirajAdapter);

        listaSortiraj = new ArrayList<String>(Arrays.asList("Sort by", "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending", "Date - Ascending", "Date - Descending"));
        sortirajAdapter = new FiltrirajAdapter(this, listaSortiraj);
        sSortiraj.setAdapter(sortirajAdapter);

        transactionListPresenter = new TransactionListPresenter();
        transactionListAdapter = new TransactionListAdapter(this, R.layout.just_text, transactionListPresenter.getTransactions());
        lVTransakcije.setAdapter(transactionListAdapter);


        //String date = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");
        String date = dateFormat.format(new Date());
        tVDatum.setText(date);

        datumPrije.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");
                                              Date datum = null;
                                              try {
                                                  datum = dateFormat.parse((String) tVDatum.getText());
                                              } catch (ParseException e) {
                                                  e.printStackTrace();
                                              }
                                              Calendar calendar = Calendar.getInstance();
                                              calendar.setTime(datum);
                                              // System.out.println(datum);
                                              calendar.add(Calendar.MONTH, -1);
                                              Date tomorrow = calendar.getTime();
                                              tVDatum.setText(dateFormat.format(tomorrow));

                                          }
                                      }
        );
        datumPoslije.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");
                                                Date datum = null;
                                                try {
                                                    datum = dateFormat.parse((String) tVDatum.getText());
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(datum);
                                                calendar.add(Calendar.MONTH, 1);
                                                Date tomorrow = calendar.getTime();
                                                tVDatum.setText(dateFormat.format(tomorrow));

                                            }
                                        }
        );



    }
}

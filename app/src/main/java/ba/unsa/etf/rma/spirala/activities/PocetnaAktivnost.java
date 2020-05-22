package ba.unsa.etf.rma.spirala.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.spirala.R;
import ba.unsa.etf.rma.spirala.adapters.FiltrirajAdapter;
import ba.unsa.etf.rma.spirala.adapters.SpinnerAdapter;
import ba.unsa.etf.rma.spirala.adapters.TransactionListAdapter;
import ba.unsa.etf.rma.spirala.interactors.AccountInteractor;
import ba.unsa.etf.rma.spirala.views.ITransactionListView;
import ba.unsa.etf.rma.spirala.models.Account;
import ba.unsa.etf.rma.spirala.models.Transaction;
import ba.unsa.etf.rma.spirala.presenters.ITransactionListPresenter;
import ba.unsa.etf.rma.spirala.presenters.TransactionListPresenter;

public class PocetnaAktivnost extends AppCompatActivity implements ITransactionListView {
    public static TextView tVAmount;
    public static TextView tVLimit;
    private Spinner spinnerFilter;
    private ImageButton prevMonth;
    private TextView tDefaultDate;
    private ImageButton nextMonth;
    private Spinner spinnerSort;
    private ListView lVTransakcije;
    private Button dodajTransakciju;
    private ITransactionListPresenter trasactionListPresenter;
    public static Date defaultDate = new Date();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");
    public static Handler handler = new Handler();


    public ITransactionListPresenter getPresenter() {
        if (trasactionListPresenter == null) {
            trasactionListPresenter = new TransactionListPresenter(this, PocetnaAktivnost.this);
        }
        return trasactionListPresenter;
    }

    public static TransactionListAdapter transactionListAdapter;
    private FiltrirajAdapter filtrirajAdapter;
    private SpinnerAdapter sortirajAdapter;
    private ArrayList<Transaction> listaTransakcija = new ArrayList<>();

    private Transaction izabranaTransakcija;

    public static Account account;

    private OnItemClick onItemClick;


    public interface OnItemClick {
        public void onItemClicked(Transaction transaction);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocetna);


        transactionListAdapter = new TransactionListAdapter(this, R.layout.lista_transakcija, listaTransakcija);
        filtrirajAdapter = new FiltrirajAdapter(this);
        sortirajAdapter = new SpinnerAdapter(this);
        sortirajAdapter.dodajFiltereZaSortiranje();

        tVAmount = (TextView) findViewById(R.id.tVAmount);
        tVLimit = (TextView) findViewById(R.id.tVLimit);
        spinnerFilter = (Spinner) findViewById(R.id.sFilterBy);
        prevMonth = (ImageButton) findViewById(R.id.iVLijevo);
        tDefaultDate = (TextView) findViewById(R.id.tVDatum);
        nextMonth = (ImageButton) findViewById(R.id.iVDesno);
        spinnerSort = (Spinner) findViewById(R.id.sSortBy);
        lVTransakcije = (ListView) findViewById(R.id.lVTransakcije);
        dodajTransakciju = (Button) findViewById(R.id.bDodajTransakciju);


        tDefaultDate.setText(dateFormat.format(defaultDate));

        spinnerSort.setAdapter(sortirajAdapter);
        spinnerFilter.setAdapter(filtrirajAdapter);
        spinnerFilter.setOnItemSelectedListener(filterItemClickListener);
        spinnerSort.setOnItemSelectedListener(sortItemClickListener);

        lVTransakcije.setAdapter(transactionListAdapter);
        lVTransakcije.setOnItemClickListener(listaTransakcijaCLickListener);
        lVTransakcije.setOnItemLongClickListener(editTransactionClickListener);

        getPresenter().getTransactionOnDate(defaultDate);

        prevMonth.setOnClickListener(prevMonthOnClickListener);
        nextMonth.setOnClickListener(nextMonthClickListener);

        dodajTransakciju.setOnClickListener(addTransactionOnClickListener);

        getInfoAboutAccount();
    }

    private void getInfoAboutAccount() {
        new AccountInteractor().execute();
    }


    private View.OnClickListener addTransactionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PocetnaAktivnost.this, AddTransaction.class);
            startActivityForResult(intent, 3);
        }
    };

    private AdapterView.OnItemClickListener listaTransakcijaCLickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Transaction transaction = transactionListAdapter.getTransaction(position);
                    izabranaTransakcija = transactionListAdapter.getTransaction(position);
                    showTheTransaction(transaction);
                }
            };

    private AdapterView.OnItemLongClickListener editTransactionClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Transaction transaction = transactionListAdapter.getTransaction(position);
            izabranaTransakcija = transactionListAdapter.getTransaction(position);
            editTransaction(transaction);
            return true;
        }
    };

    private void editTransaction(Transaction transaction) {
        Intent transactionDetailIntent = new Intent(PocetnaAktivnost.this, AddTransaction.class);
        transactionDetailIntent.putExtra("transaction", transaction);
        startActivityForResult(transactionDetailIntent, 20);
    }

    private void showTheTransaction(Transaction transaction) {
        Intent transactionDetailIntent = new Intent(PocetnaAktivnost.this, TransactionDetail.class);
        transactionDetailIntent.putExtra("transaction", transaction);
        startActivityForResult(transactionDetailIntent, 10);
    }

    private View.OnClickListener prevMonthOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date dat = getDatum((String) tDefaultDate.getText());
            defaultDate = izracunajMjesec(dat, -1);
            tDefaultDate.setText(dateFormat.format(defaultDate));
            getPresenter().getTransactionOnDate(defaultDate);


        }
    };

    private View.OnClickListener nextMonthClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date dat = getDatum((String) tDefaultDate.getText());
            defaultDate = izracunajMjesec(dat, 1);
            tDefaultDate.setText(dateFormat.format(defaultDate));
            getPresenter().getTransactionOnDate(defaultDate);
        }
    };


    private AdapterView.OnItemSelectedListener sortItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItemText = (String) parent.getItemAtPosition(position);
            getPresenter().getSortTransaction(selectedItemText);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private AdapterView.OnItemSelectedListener filterItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItemText = (String) parent.getItemAtPosition(position);
            if (selectedItemText.equals("All transactions") || selectedItemText.equals("Filter by")) {
                getPresenter().getTransactionOnDate(defaultDate);
            } else {
                getPresenter().getFilteredTransacion(selectedItemText);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //  getPresenter().refreshTransactionOnDate(defaultDate);
        }
    };

    private AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for (int i = 0; i < lVTransakcije.getChildCount(); i++) {
                if (i == position)
                    lVTransakcije.getChildAt(i).setBackgroundColor(Color.GRAY);
                else lVTransakcije.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
            Transaction transaction = transactionListAdapter.getTransaction(position);
            onItemClick.onItemClicked(transaction);
        }
    };

    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        transactionListAdapter.setTransactions(transactions);
    }

    @Override
    public void notifyTransactionListDataSetChanged() {
        transactionListAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearListOfTransactions() {
        transactionListAdapter.clearList();
    }

    @Override
    public void sort(String string) {
        transactionListAdapter.sortiraj(string);
    }

    @Override
    public void removeTransaction(Transaction transaction) {
        transactionListAdapter.delete(transaction);
    }

    @Override
    public void addTransaction(Transaction trans) {
        transactionListAdapter.dodajTransakciju(trans);
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

    private Date izracunajMjesec(Date dat, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dat);
        calendar.add(Calendar.MONTH, i);
        return calendar.getTime();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            if (data != null) {
                getPresenter().deleteTransaction(izabranaTransakcija);
                getPresenter().getTransactionOnDate(defaultDate);
            }
        } else if (requestCode == 3) {
            if (data != null
                    && data.getExtras().containsKey("title")
                    && data.getExtras().containsKey("type")
                    && data.getExtras().containsKey("itemDescription")
                    && data.getExtras().containsKey("date")
                    && data.getExtras().containsKey("interval")
                    && data.getExtras().containsKey("endDate")
                    && data.getExtras().containsKey("amount")) {

                String title;
                Transaction.Type type;
                String description;
                Date date;
                Date endDate;
                Integer interval;
                double amount;
                Transaction nova = new Transaction();


                title = data.getStringExtra("title");
                description = data.getStringExtra("itemDescription");
                date = (Date) data.getSerializableExtra("date");
                interval = (Integer) data.getSerializableExtra("interval");
                endDate = (Date) data.getSerializableExtra("endDate");
                amount = data.getDoubleExtra("amount", 0);
                type = (Transaction.Type) data.getSerializableExtra("type");

                nova.setType(type);
                nova.setTitle(title);
                nova.setItemDescription(description);
                nova.setTransactionInterval(interval);
                nova.setAmount(amount);
                nova.setDate(date);
                nova.setEndDate(endDate);

                getPresenter().addTransaction(nova);
                getPresenter().getTransactionOnDate(defaultDate);
            }
        } else if (resultCode == 4) {
            if (data != null
                    && data.getExtras().containsKey("title")
                    && data.getExtras().containsKey("type")
                    && data.getExtras().containsKey("itemDescription")
                    && data.getExtras().containsKey("date")
                    && data.getExtras().containsKey("interval")
                    && data.getExtras().containsKey("endDate")
                    && data.getExtras().containsKey("amount")) {

                String title;
                Transaction.Type type;
                String description;
                Date date;
                Date endDate;
                Integer interval;
                double amount;
                Transaction nova = new Transaction();


                title = data.getStringExtra("title");
                description = data.getStringExtra("itemDescription");
                date = (Date) data.getSerializableExtra("date");
                interval = (Integer) data.getSerializableExtra("interval");
                endDate = (Date) data.getSerializableExtra("endDate");
                amount = data.getDoubleExtra("amount", 0);
                type = (Transaction.Type) data.getSerializableExtra("type");

                nova.setType(type);
                nova.setTitle(title);
                nova.setItemDescription(description);
                nova.setTransactionInterval(interval);
                nova.setAmount(amount);
                nova.setDate(date);
                nova.setEndDate(endDate);

                getPresenter().editTransaction(nova, izabranaTransakcija);
                getPresenter().getTransactionOnDate(defaultDate);
            }
        }
    }


}

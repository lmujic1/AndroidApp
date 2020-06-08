package ba.unsa.etf.rma.spirala.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import ba.unsa.etf.rma.spirala.interactors.UpdateAccountInteractor;
import ba.unsa.etf.rma.spirala.util.TransactionDBOpenHelper;
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


    TransactionDBOpenHelper transactionDBOpenHelper;
    ArrayList<Transaction> getTransactionFromDatabase = new ArrayList<>();
    private boolean firstTimeOpen = true;
    private ArrayList<Transaction> offlineBrisanje = new ArrayList<>(),
            offlineDodavanje = new ArrayList<>(),
            offlineEditovanje = new ArrayList<>();

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



    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable()) {
            getPresenter().getTransactionOnDate(defaultDate);
            for (Transaction t : offlineDodavanje) {
                getPresenter().addTransaction(t);
            }
            offlineDodavanje.clear();
            for (Transaction t : offlineBrisanje) {
                getPresenter().deleteTransaction(t);
            }
            offlineBrisanje.clear();
            getPresenter().getTransactionOnDate(defaultDate);

        }
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


        prevMonth.setOnClickListener(prevMonthOnClickListener);
        nextMonth.setOnClickListener(nextMonthClickListener);

        dodajTransakciju.setOnClickListener(addTransactionOnClickListener);

        getPresenter().getTransactionCursor();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
            if (isNetworkAvailable()) {
                getPresenter().getSortTransaction(selectedItemText);
            }
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
                if (isNetworkAvailable()) {
                    getPresenter().getTransactionOnDate(defaultDate);
                }
            } else {
                if (isNetworkAvailable()) {
                    getPresenter().getFilteredTransacion(selectedItemText);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //  getPresenter().refreshTransactionOnDate(defaultDate);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            if (data != null) {
                if (isNetworkAvailable()) {
                    getPresenter().deleteTransaction(izabranaTransakcija);
                    getPresenter().getTransactionOnDate(defaultDate);
                    getPresenter().deleteTransactionDB(izabranaTransakcija);
                    //editAccount(izabranaTransakcija, 2);
                    //getInfoAboutAccount();
                } else {
                    Toast.makeText(getApplicationContext(), "Offline brisanje", Toast.LENGTH_SHORT).show();
                    offlineBrisanje.add(izabranaTransakcija);
                    izabranaTransakcija.setOffMode("Offline brisanje");
                    getPresenter().deleteTransactionDB(izabranaTransakcija);

                }
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

                if (isNetworkAvailable()) {
                    getPresenter().addTransaction(nova);
                    getPresenter().getTransactionOnDate(defaultDate);
                    //editAccount(nova, 3);
                    //getInfoAboutAccount();
                } else {
                    Toast.makeText(getApplicationContext(), "Offline dodavanje", Toast.LENGTH_SHORT).show();
                    nova.setOffMode("Offline dodavanje");

                    offlineDodavanje.add(nova);
                    getPresenter().addedTransactionDB(nova);
                }
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

                nova.setIdTransaction(izabranaTransakcija.getIdTransaction());
                nova.setType(type);
                nova.setTitle(title);
                nova.setItemDescription(description);
                nova.setTransactionInterval(interval);
                nova.setAmount(amount);
                nova.setDate(date);
                nova.setEndDate(endDate);

                if (isNetworkAvailable()) {
                    getPresenter().editTransaction(nova, izabranaTransakcija);
                    getPresenter().getTransactionOnDate(defaultDate);
                   // editAccount(nova, izabranaTransakcija, 3);
                   // getInfoAboutAccount();
                } else {
                    Toast.makeText(getApplicationContext(), "Offline izmjena", Toast.LENGTH_SHORT).show();
                    izabranaTransakcija.setOffMode("Offline izmjena");
                    nova.setOffMode("Offline izmjena");
                    getPresenter().editTransactionDB(izabranaTransakcija, nova);
                    offlineEditovanje.add(nova);
                }
            }
        }
    }

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

    @Override
    public void editingTransaction(Transaction stara, Transaction nova) {
        transactionListAdapter.edituj(stara,nova);
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

    private void editAccount(Transaction nova, Transaction izabranaTransakcija, int vrsta) {
        String query = "{\n";
        Transaction.Type tip = nova.getType();
        int type = nova.getTypeId(tip);
        double newBudget = account.getBudget();
        double oldBudget = izabranaTransakcija.getAmount();
        if (vrsta == 3) {
            if (type == 2 || type == 4) {
                newBudget -= oldBudget;
                newBudget += nova.getAmount();
            } else {
                newBudget += oldBudget;
                newBudget -= nova.getAmount();
            }
        }
        query += "\"budget\" : " + newBudget + "\n}";
        new UpdateAccountInteractor().execute(query);
    }

    private void editAccount(Transaction nova, int vrsta) {

        String query = "{\n";
        Transaction.Type tip = nova.getType();
        int type = nova.getTypeId(tip);
        double newBudget = account.getBudget();
        if (vrsta == 3) {
            if (type == 2 || type == 4) {
                newBudget += nova.getAmount();
            } else newBudget -= nova.getAmount();
        } else if (vrsta == 2) {
            if (type == 2 || type == 4) {
                newBudget -= nova.getAmount();
            } else newBudget += nova.getAmount();
        }
        query += "\"budget\" : " + newBudget + "\n}";
        new UpdateAccountInteractor().execute(query);
    }

}

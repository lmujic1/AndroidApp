package ba.unsa.etf.rma.spirala.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.spirala.R;
import ba.unsa.etf.rma.spirala.adapters.FiltrirajAdapter;
import ba.unsa.etf.rma.spirala.adapters.SpinnerAdapter;
import ba.unsa.etf.rma.spirala.adapters.TransactionListAdapter;
import ba.unsa.etf.rma.spirala.models.Transaction;
import ba.unsa.etf.rma.spirala.presenters.ITransactionListPresenter;
import ba.unsa.etf.rma.spirala.presenters.TransactionListPresenter;

public class TransactionListFragment extends Fragment implements ITransactionListView {
    private TextView tVAmount;
    private TextView tVLimit;
    private Spinner spinnerFilter;
    private ImageButton prevMonth;
    private TextView tDefaultDate;
    private ImageButton nextMonth;
    private Spinner spinnerSort;
    private ListView lVTransakcije;
    private Button dodajTransakciju;
    private ITransactionListPresenter trasactionListPresenter;

    private Date defaultDate = new Date();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");

    private ITransactionListPresenter getPresenter() {
        if (trasactionListPresenter == null) {
            trasactionListPresenter = new TransactionListPresenter(this, getActivity());
        }
        return trasactionListPresenter;
    }

    private TransactionListAdapter transactionListAdapter;
    private FiltrirajAdapter filtrirajAdapter;
    private SpinnerAdapter sortirajAdapter;


    private OnItemClick onItemClick;

    public interface OnItemClick {
        public void onItemClicked(Transaction transaction);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_pocetna, container, false);
        transactionListAdapter = new TransactionListAdapter(getActivity(), R.layout.lista_transakcija, new ArrayList<Transaction>());
        filtrirajAdapter = new FiltrirajAdapter(getContext());
        sortirajAdapter = new SpinnerAdapter(getContext());

        tVAmount = (TextView) fragmentView.findViewById(R.id.tVAmount);
        tVLimit = (TextView) fragmentView.findViewById(R.id.tVLimit);
        spinnerFilter = (Spinner) fragmentView.findViewById(R.id.sFilterBy);
        prevMonth = (ImageButton) fragmentView.findViewById(R.id.iVLijevo);
        tDefaultDate = (TextView) fragmentView.findViewById(R.id.tVDatum);
        nextMonth = (ImageButton) fragmentView.findViewById(R.id.iVDesno);
        spinnerSort = (Spinner) fragmentView.findViewById(R.id.sSortBy);
        lVTransakcije = (ListView) fragmentView.findViewById(R.id.lVTransakcije);
        dodajTransakciju = (Button) fragmentView.findViewById(R.id.bDodajTransakciju);

        tDefaultDate.setText(dateFormat.format(defaultDate));

        spinnerSort.setAdapter(sortirajAdapter);
        spinnerFilter.setAdapter(filtrirajAdapter);
        spinnerFilter.setOnItemSelectedListener(filterItemClickListener);
        spinnerSort.setOnItemSelectedListener(sortItemClickListener);

        lVTransakcije.setAdapter(transactionListAdapter);
        lVTransakcije.setOnItemClickListener(listItemClickListener);
        getPresenter().refreshTransactionOnDate(defaultDate);
        onItemClick = (OnItemClick) getActivity();

        prevMonth.setOnClickListener(prevMonthOnClickListener);
        nextMonth.setOnClickListener(nextMonthClickListener);


        return fragmentView;
    }

    private View.OnClickListener addTransactionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener prevMonthOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date dat = getDatum((String) tDefaultDate.getText());
            defaultDate = izracunajMjesec(dat, -1);
            tDefaultDate.setText(dateFormat.format(defaultDate));
            getPresenter().refreshTransactionOnDate(defaultDate);
        }
    };

    private View.OnClickListener nextMonthClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date dat = getDatum((String) tDefaultDate.getText());
            defaultDate = izracunajMjesec(dat, 1);
            tDefaultDate.setText(dateFormat.format(defaultDate));
            getPresenter().refreshTransactionOnDate(defaultDate);
        }
    };

    private AdapterView.OnItemSelectedListener sortItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItemText = (String) parent.getItemAtPosition(position);
            getPresenter().refreshTransactionSort(selectedItemText);
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
                getPresenter().refreshTransactionOnDate(defaultDate);
            } else {
                getPresenter().refreshTransactionFilter(defaultDate, selectedItemText);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            getPresenter().refreshTransactionOnDate(defaultDate);
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

}

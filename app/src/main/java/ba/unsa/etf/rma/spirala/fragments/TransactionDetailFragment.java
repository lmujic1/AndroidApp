package ba.unsa.etf.rma.spirala.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ba.unsa.etf.rma.spirala.R;
import ba.unsa.etf.rma.spirala.models.Transaction;
import ba.unsa.etf.rma.spirala.presenters.ITransactionDetailPresenter;
import ba.unsa.etf.rma.spirala.presenters.TransactionDetailPresenter;

public class TransactionDetailFragment extends Fragment {
    private TextView titleTransakcije;
    private TextView typeTransakcije;
    private TextView opisTransakcije;
    private TextView dateTransakcije;
    private TextView intervalTransakcije;
    private TextView endTransakcije;
    private TextView iznosTransakcije;
    private Button saveButton;
    private Button deleteButton;

    private ITransactionDetailPresenter presenter;
    public ITransactionDetailPresenter getPresenter() {
        if(presenter == null) {
            presenter = new TransactionDetailPresenter(getActivity());
        }
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detalji_transakcije, container,false);
        if(getArguments() != null && getArguments().containsKey("transaction")) {

            getPresenter().setTransaction(getArguments().getParcelable("transaction"));

            titleTransakcije = (TextView) view.findViewById(R.id.title);
            typeTransakcije = (TextView) view.findViewById(R.id.typeOftransaction);
            opisTransakcije = (TextView) view.findViewById(R.id.itemDescription);
            dateTransakcije = (TextView) view.findViewById(R.id.date);
            intervalTransakcije = (TextView) view.findViewById(R.id.transactionInterval);
            endTransakcije = (TextView) view.findViewById(R.id.endDate);
            iznosTransakcije = (TextView) view.findViewById(R.id.amount);
            saveButton = (Button) view.findViewById(R.id.saveButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            Transaction transaction=getPresenter().getTransaction();
            titleTransakcije.setText(transaction.getTitle());
            iznosTransakcije.setText(String.format("%.2f",transaction.getAmount()) + " BAM");
            dateTransakcije.setText(transaction.getDate1(transaction.getDate()));
            typeTransakcije.setText(transaction.getType().toString());
            opisTransakcije.setText(transaction.getItemDescription());

            intervalTransakcije.setText(String.valueOf(transaction.getTransactionInterval()) + " days");
            if(transaction.getEndDate() == null) endTransakcije.setText(transaction.getDate1(transaction.getDate()));
            else endTransakcije.setText(transaction.getDate1(transaction.getEndDate()));
        }
        return view;
    }
}

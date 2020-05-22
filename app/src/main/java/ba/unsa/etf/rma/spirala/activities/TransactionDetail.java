package ba.unsa.etf.rma.spirala.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import ba.unsa.etf.rma.spirala.R;
import ba.unsa.etf.rma.spirala.models.Transaction;
import ba.unsa.etf.rma.spirala.presenters.ITransactionDetailPresenter;
import ba.unsa.etf.rma.spirala.presenters.TransactionDetailPresenter;

public class TransactionDetail extends AppCompatActivity{
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

    public ITransactionDetailPresenter getPresenterDetail() {
        if (presenter == null) {
            presenter = new TransactionDetailPresenter(this);
        }
        return presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalji_transakcije);

        titleTransakcije = (TextView) findViewById(R.id.title);
        typeTransakcije = (TextView) findViewById(R.id.typeOftransaction);
        opisTransakcije = (TextView) findViewById(R.id.itemDescription);
        dateTransakcije = (TextView) findViewById(R.id.date);
        intervalTransakcije = (TextView) findViewById(R.id.transactionInterval);
        endTransakcije = (TextView) findViewById(R.id.endDate);
        iznosTransakcije = (TextView) findViewById(R.id.amount);
        saveButton = (Button) findViewById(R.id.saveButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);


        transaction = (Transaction) getIntent().getSerializableExtra("transaction");

        titleTransakcije.setText(transaction.getTitle());
        iznosTransakcije.setText(String.format("%.2f", transaction.getAmount()) + " BAM");

        dateTransakcije.setText(transaction.getDate1(transaction.getDate()));
        typeTransakcije.setText(transaction.getType().toString());
        opisTransakcije.setText(transaction.getItemDescription());

        intervalTransakcije.setText(String.valueOf(transaction.getTransactionInterval()) + " days");
        if (transaction.getEndDate() == null)
            endTransakcije.setText(transaction.getDate1(transaction.getDate()));
        else endTransakcije.setText(transaction.getDate1(transaction.getEndDate()));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        deleteButton.setOnClickListener(deleteTransactionOnClickListener);
    }

    private Transaction transaction;

    private View.OnClickListener deleteTransactionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(TransactionDetail.this);
            builder.setTitle("Select your answer.");
            builder.setMessage("Want to delete transaction?");
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent intent = new Intent();
                            setResult(2,intent);
                            finish();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            builder.setPositiveButton("Yes", dialogClickListener);
            builder.setNegativeButton("No", dialogClickListener);
            AlertDialog dialog = builder.create();
            dialog.show();


        }
    };


}

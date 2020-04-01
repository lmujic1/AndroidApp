package ba.unsa.etf.rma.spirala;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionMakeAChangeActivity extends AppCompatActivity {

    private EditText titleTransakcije;
    private EditText typeTransakcije;
    private EditText opisTransakcije;
    private EditText dateTransakcije;
    private EditText intervalTransakcije;
    private EditText endTransakcije;
    private EditText iznosTransakcije;
    private ImageView iconTransakcije;
    private Button saveButton;
    private Button deleteButton;

    private Transaction transaction;


    private String title, type, description, date,endDate;
    private int interval;
    private double amount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_make_a_change);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null
                && bundle.containsKey("title")
                && bundle.containsKey("type")
                && bundle.containsKey("itemDescription")
                && bundle.containsKey("date")
                && bundle.containsKey("interval")
                && bundle.containsKey("endDate")
                && bundle.containsKey("amount")) {

            title = bundle.getString("title");
            type = bundle.getString("type");
            description = bundle.getString("itemDescription");
            date = bundle.getString("date");
            interval = bundle.getInt("interval");
            endDate = bundle.getString("endDate");
            amount = bundle.getDouble("amount");
        }

        titleTransakcije = (EditText) findViewById(R.id.iTitleTransakcije);
        typeTransakcije = (EditText) findViewById(R.id.iTypeTransakcije);
        opisTransakcije = (EditText) findViewById(R.id.iOpisTransakcije);
        dateTransakcije = (EditText) findViewById(R.id.iDateTransakcije);
        intervalTransakcije = (EditText) findViewById(R.id.iIntervalTransakcije);
        endTransakcije = (EditText) findViewById(R.id.iEndTransakcije);
        iznosTransakcije = (EditText) findViewById(R.id.iIznosTransakcije);
        iconTransakcije = (ImageView) findViewById(R.id.iIconTransakcije);
        saveButton = (Button) findViewById(R.id.iSaveButton);
        deleteButton = (Button) findViewById(R.id.iDeleteButton);


        titleTransakcije.setText(title);
        typeTransakcije.setText(type);
        opisTransakcije.setText(description);
        dateTransakcije.setText(date);
        intervalTransakcije.setText(String.format("%d",interval));
        iznosTransakcije.setText(String.format("%.2f",amount));
        endTransakcije.setText(endDate);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!String.valueOf(titleTransakcije.getText()).equals(title)) {
                    if(!validirajTitle(titleTransakcije.getText())) {
                        titleTransakcije.setBackgroundColor(Color.RED);
                    }
                    else
                        titleTransakcije.setBackgroundColor(Color.GREEN);
                }
               if(!String.valueOf(typeTransakcije.getText()).equals(type)) {
                    if(!validirajType(typeTransakcije.getText()))
                        typeTransakcije.setBackgroundColor(Color.RED);
                    else
                        typeTransakcije.setBackgroundColor(Color.GREEN);
                }
                if(!String.valueOf(opisTransakcije.getText()).equals(description))
                    opisTransakcije.setBackgroundColor(Color.GREEN);
                if(!String.valueOf(dateTransakcije.getText()).equals(date))
                    dateTransakcije.setBackgroundColor(Color.GREEN);
                if(!String.valueOf(intervalTransakcije.getText()).equals(String.valueOf(interval)))
                    intervalTransakcije.setBackgroundColor(Color.GREEN);
                if(!String.valueOf(endTransakcije.getText()).equals(endDate))
                    endTransakcije.setBackgroundColor(Color.GREEN);
                if(!String.valueOf(iznosTransakcije.getText()).equals(String.valueOf(amount)))
                    iznosTransakcije.setBackgroundColor(Color.GREEN);
            }
        });
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(TransactionMakeAChangeActivity.this);
                builder.setTitle("Select your answer.");
                builder.setMessage("Want to delete transaction?");
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent();
                                setResult(10,intent);
                                finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No",dialogClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private boolean validirajType(Editable text) {
        String s = String.valueOf(text);
        return s.equals("Individual payment") || s.equals("Regular payment") || s.equals("Purchase") || s.equals("Individual income") || s.equals("Regular income");
    }

    private boolean validirajTitle(Editable text) {
        String s = String.valueOf(text);
        return s.length() > 3 && s.length() < 15;
    }
}

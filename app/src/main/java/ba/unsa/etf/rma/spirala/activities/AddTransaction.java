package ba.unsa.etf.rma.spirala.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.spirala.R;
import ba.unsa.etf.rma.spirala.adapters.SpinnerAdapter;
import ba.unsa.etf.rma.spirala.models.Transaction;
import ba.unsa.etf.rma.spirala.models.Transaction.Type;

import static ba.unsa.etf.rma.spirala.models.Transaction.Type.INDIVIDUALINCOME;
import static ba.unsa.etf.rma.spirala.models.Transaction.Type.REGULARPAYMENT;

public class AddTransaction extends AppCompatActivity {
    private EditText title;
    private Spinner typeSpinner;
    private DatePicker choseDate;
    private EditText amountEdit;
    private EditText descriptionTransaction;
    private DatePicker endDateTransaction;
    private Button saveButton;
    private Button deleteButton;
    private EditText intervalOfTransaction;

    private SpinnerAdapter spinnerAdapter;


    private String titleTransaction;
    private String typeTransaction;
    private Date dateTransaction;
    private double amountTransaction;
    private Date endDateTransaction1;
    private String descriptionOfTransaction;
    private Integer intervalTransaction;
    private Transaction.Type type;

    private boolean edit = false;
    private Transaction editTransaction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_transakciju);

        spinnerAdapter = new SpinnerAdapter(this);
        spinnerAdapter.dodajTipoveTransakcija();

        title = (EditText) findViewById(R.id.title);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        choseDate = (DatePicker) findViewById(R.id.choseDate);
        amountEdit = (EditText) findViewById(R.id.amountEdit);
        intervalOfTransaction = (EditText) findViewById(R.id.intervalOfTransaction);
        descriptionTransaction = (EditText) findViewById(R.id.descriptionTransaction);
        endDateTransaction = (DatePicker) findViewById(R.id.endDateTransaction);
        saveButton = (Button) findViewById(R.id.saveButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);


        typeSpinner.setAdapter(spinnerAdapter);
        typeSpinner.setOnItemSelectedListener(onItemSelectedClickListener);

        titleTransaction = String.valueOf(title.getText());

        saveButton.setOnClickListener(onSaveButtonClickListener);

        Transaction izmijeni = (Transaction) getIntent().getSerializableExtra("transaction");
        if (izmijeni != null) {
            editTransaction = izmijeni;
            edit = true;

            Type type = izmijeni.getType();
            Date date = izmijeni.getDate();
            title.setText(izmijeni.getTitle());
            amountEdit.setText(String.format("%.2f", izmijeni.getAmount()));
            typeSpinner.setSelection(izmijeni.getTypeId(type));

            String dayString = (String) DateFormat.format("dd", date);
            String monthString = (String) DateFormat.format("MM", date);
            String yearString = (String) DateFormat.format("yyyy", date);
            int day = Integer.parseInt(dayString);
            int month = Integer.parseInt(monthString) - 1;
            int year = Integer.parseInt(yearString);
            choseDate.init(year, month, day, null);

            String description = izmijeni.getItemDescription();
            if (description != null) descriptionTransaction.setText(description);

            Date endDate = izmijeni.getEndDate();
            if (endDate != null) {
                String edayString = (String) DateFormat.format("dd", endDate);
                String emonthString = (String) DateFormat.format("MM", endDate);
                String eyearString = (String) DateFormat.format("yyyy", endDate);
                int eday = Integer.parseInt(edayString);
                int emonth = Integer.parseInt(emonthString) - 1;
                int eyear = Integer.parseInt(eyearString);
                endDateTransaction.init(eyear, emonth, eday, null);
            }

            Integer interval = izmijeni.getTransactionInterval();
            if (interval != null) {
                intervalOfTransaction.setText(interval);
            }
            deleteButton.setEnabled(true);
            deleteButton.setOnClickListener(deleteTransactionOnClickListener);
        } else {
            deleteButton.setEnabled(false);
        }

    }


    private AdapterView.OnItemSelectedListener onItemSelectedClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItemText = (String) parent.getItemAtPosition(position);
            typeTransaction = selectedItemText;
            switch (typeTransaction) {
                case "Individual payment":
                    type = Type.INDIVIDUALPAYMENT;
                    intervalOfTransaction.setEnabled(false);
                    descriptionTransaction.setEnabled(true);
                    endDateTransaction.setEnabled(false);
                    break;
                case "Regular payment":
                    type = REGULARPAYMENT;
                    intervalOfTransaction.setEnabled(true);
                    descriptionTransaction.setEnabled(true);
                    endDateTransaction.setEnabled(true);
                    break;
                case "Purchase":
                    type = Type.PURCHASE;
                    intervalOfTransaction.setEnabled(false);
                    descriptionTransaction.setEnabled(true);
                    endDateTransaction.setEnabled(false);
                    break;
                case "Individual income":
                    type = INDIVIDUALINCOME;
                    intervalOfTransaction.setEnabled(false);
                    descriptionTransaction.setEnabled(false);
                    endDateTransaction.setEnabled(false);
                    break;
                case "Regular income":
                    type = Type.REGULARINCOME;
                    intervalOfTransaction.setEnabled(true);
                    descriptionTransaction.setEnabled(false);
                    endDateTransaction.setEnabled(true);
                    break;
                case "Select type":
                    //typeSpinner.setBackgroundColor(Color.RED);
                    intervalOfTransaction.setEnabled(true);
                    descriptionTransaction.setEnabled(true);
                    endDateTransaction.setEnabled(true);
                    break;
            }
            if (position == 0) {
                //alert
                //typeSpinner.setBackgroundColor(Color.RED);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener onSaveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean correctTtitle = true, correctAmmount = true;

            titleTransaction = String.valueOf(title.getText());
            if (!validirajTitle(title.getText())) {
                title.setBackgroundColor(Color.RED);
                correctTtitle = false;
            } else title.setBackgroundColor(Color.GREEN);
            String am = String.valueOf(amountEdit.getText());
            if (am.equals("")) {
                System.out.println("DA LI JE OVDJE");
                amountEdit.setBackgroundColor(Color.RED);
                correctAmmount = false;
            } else if (!am.equals("")) {
                amountTransaction = Double.valueOf(String.valueOf(amountEdit.getText()));
            }

            //date
            int day, month, year;
            day = choseDate.getDayOfMonth();
            month = choseDate.getMonth();
            year = choseDate.getYear();
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(year, month, day);
            dateTransaction = calendar1.getTime();
            int d1, m1, y1;
            d1 = endDateTransaction.getDayOfMonth();
            m1 = endDateTransaction.getMonth();
            y1 = endDateTransaction.getYear();
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(y1, m1, d1);
            endDateTransaction1 = calendar2.getTime();
            //descripton
            descriptionOfTransaction = String.valueOf(descriptionTransaction.getText());
            //interval
            switch (type) {
                case INDIVIDUALPAYMENT:
                    descriptionOfTransaction = null;
                    intervalTransaction = null;
                    endDateTransaction1 = null;
                    break;
                case REGULARPAYMENT:
                    // descriptionOfTransaction = null;
                    intervalTransaction = Integer.parseInt(String.valueOf(intervalOfTransaction.getText()));
                    break;
                case PURCHASE:
                    type = Type.PURCHASE;
                    intervalTransaction = null;
                    endDateTransaction1 = null;
                    break;
                case INDIVIDUALINCOME:
                    type = INDIVIDUALINCOME;
                    descriptionOfTransaction = null;
                    intervalTransaction = null;
                    endDateTransaction1 = null;
                    break;
                case REGULARINCOME:
                    type = Type.REGULARINCOME;
                    descriptionOfTransaction = null;
                    intervalTransaction = Integer.parseInt(String.valueOf(intervalOfTransaction.getText()));
                    break;
            }

            //Transaction nova = new Transaction(dateTransaction, amountTransaction, titleTransaction, type, descriptionOfTransaction, intervalTransaction, endDateTransaction1);
            if (editTransaction != null) {

                Date date = editTransaction.getDate();
                String dayString = (String) DateFormat.format("dd", date);
                String monthString = (String) DateFormat.format("MM", date);
                String yearString = (String) DateFormat.format("yyyy", date);
                int day1 = Integer.parseInt(dayString);
                int month1 = Integer.parseInt(monthString) - 1;
                int year1 = Integer.parseInt(yearString);

                if (!titleTransaction.equals(editTransaction.getTitle()) && correctTtitle) {
                    title.setBackgroundColor(Color.GREEN);
                }
                if (type != editTransaction.getType()) typeSpinner.setBackgroundColor(Color.GREEN);
                if (day != day1 || month != month1 || year != year1) {
                    choseDate.setBackgroundColor(Color.GREEN);
                }
                if (endDateTransaction1 != null) {
                    Date endDate = editTransaction.getEndDate();
                    String edayString = (String) DateFormat.format("dd", endDate);
                    String emonthString = (String) DateFormat.format("MM", endDate);
                    String eyearString = (String) DateFormat.format("yyyy", endDate);
                    int eday = Integer.parseInt(edayString);
                    int emonth = Integer.parseInt(emonthString) - 1;
                    int eyear = Integer.parseInt(eyearString);
                    if (d1 != eday || m1 != emonth || y1 != eyear)
                        endDateTransaction.setBackgroundColor(Color.GREEN);
                }
                if (correctAmmount) {
                    if (amountTransaction != editTransaction.getAmount()) {
                        amountEdit.setBackgroundColor(Color.GREEN);
                    }
                } else amountEdit.setBackgroundColor(Color.RED);
                if (descriptionOfTransaction != null && !descriptionOfTransaction.equals(editTransaction.getItemDescription()))
                    descriptionTransaction.setBackgroundColor(Color.GREEN);
                if (intervalTransaction != null && intervalTransaction != editTransaction.getTransactionInterval())
                    intervalOfTransaction.setBackgroundColor(Color.GREEN);
            }
            Intent i = new Intent();
            Bundle b = new Bundle();
            b.putString("title", titleTransaction);
            b.putSerializable("type", type);
            b.putString("itemDescription", descriptionOfTransaction);
            b.putDouble("amount", amountTransaction);
            b.putSerializable("interval", intervalTransaction);
            b.putSerializable("date", dateTransaction);
            b.putSerializable("endDate", endDateTransaction1);
            i.putExtras(b);
            if (!edit && correctTtitle && correctAmmount) {
                setResult(3, i);
                finish();
            } else if (edit && correctTtitle && correctAmmount) {
                setResult(4, i);
                finish();
            }
        }
    };

    private View.OnClickListener deleteTransactionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(AddTransaction.this);
            builder.setTitle("Select your answer.");
            builder.setMessage("Want to delete transaction?");
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent intent = new Intent();
                            setResult(2, intent);
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

    private boolean validirajTitle(Editable text) {
        String s = String.valueOf(text);
        return s.length() > 3 && s.length() < 15;
    }

   /* @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.dodaj_transakciju, container, false);
       */
   /* titleTransakcije = (EditText) fragmentView.findViewById(R.id.iTitleTransakcije);
        typeTransakcije = (EditText) fragmentView.findViewById(R.id.iTypeTransakcije);
        opisTransakcije = (EditText) fragmentView.findViewById(R.id.iOpisTransakcije);
        dateTransakcije = (EditText) fragmentView.findViewById(R.id.iDateTransakcije);
        intervalTransakcije = (EditText) fragmentView.findViewById(R.id.iIntervalTransakcije);
        endTransakcije = (EditText) fragmentView.findViewById(R.id.iEndTransakcije);
        iznosTransakcije = (EditText) fragmentView.findViewById(R.id.iIznosTransakcije);
        iconTransakcije = (ImageView) fragmentView.findViewById(R.id.iIconTransakcije);
        saveButton = (Button) fragmentView.findViewById(R.id.iSaveButton);
        deleteButton = (Button) fragmentView.findViewById(R.id.iDeleteButton);*//*
        return fragmentView;
    }*/

    /*
    private String title, type, description, date,endDate;
    private int interval;
    private double amount;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_transakciju);

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
        format = (TextView) findViewById(R.id.format);

        format.setText("Date of transaction (format: dd. MMMM, yyyy)");

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

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("title", String.valueOf(titleTransakcije.getText()));
                bundle.putString("type", String.valueOf(typeTransakcije.getText()));
                bundle.putString("itemDescription", String.valueOf(opisTransakcije.getText()));

                bundle.putString("date", String.valueOf(dateTransakcije.getText()));
                int interv = 0;
                if(String.valueOf(intervalTransakcije.getText()).equals("") || String.valueOf(intervalTransakcije.getText())==null)  bundle.putInt("interval", interv);
                else bundle.putInt("interval", Integer.parseInt(String.valueOf(intervalTransakcije.getText())));
                bundle.putString("endDate", String.valueOf(endTransakcije.getText()));
                bundle.putDouble("amount", Double.parseDouble(String.valueOf(iznosTransakcije.getText())));

                intent.putExtras(bundle);
                setResult(20,intent);
                finish();
            }
        });
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddTransactionFragment.this);
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
    }*/
}

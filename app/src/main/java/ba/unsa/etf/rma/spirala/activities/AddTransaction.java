package ba.unsa.etf.rma.spirala.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
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

    //public static Transaction transaction;

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
            titleTransaction = String.valueOf(title.getText());
            if (!validirajTitle(title.getText())) {
                title.setBackgroundColor(Color.RED);
            } else title.setBackgroundColor(Color.GREEN);

            amountTransaction = Double.parseDouble(String.valueOf(amountEdit.getText()));
            //date
            int day, month, year;
            day = choseDate.getDayOfMonth();
            month = choseDate.getMonth();
            year = choseDate.getYear();
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(year, month, day);
            dateTransaction = calendar1.getTime();

            //end date
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

            Transaction nova = new Transaction(dateTransaction, amountTransaction, titleTransaction, type, descriptionOfTransaction, intervalTransaction, endDateTransaction1);
            //dodajTransakciju(nova);
            Intent i = new Intent();
            Bundle b = new Bundle();
            b.putString("title", titleTransaction);
            b.putSerializable("type", type);
            b.putString("itemDescription", descriptionOfTransaction);
            b.putDouble("amount", amountTransaction);
            b.putSerializable("interval", intervalTransaction);
            b.putSerializable("date", dateTransaction);
            b.putSerializable("endDate", endDateTransaction1);
            //System.out.println("ime novododane transakcije" + transaction.getTitle()+" " + transaction.getDate());
            i.putExtras(b);
            setResult(3, i);

            finish();
        }
    };

    private void dodajTransakciju(Transaction nova) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String date = dateFormat.format(nova.getDate());
        Type type = nova.getType();
        int typeId = nova.getTypeId(type);
        String query = "{\n" +
                "\"title\" : " + "\"" + nova.getTitle() + "\"" + "\n" +
                "\"date\" : " + "\"" + date + "\"" + "\n" +
                "\"TransactionTypeId\" : " + typeId + "\n" +
                "\"amount\" : " + nova.getAmount() + "\n";

        if (typeId == 1 || typeId == 2) {
            String endDate = dateFormat.format(nova.getEndDate());
            query += "\"endDate\" : " + "\"" + endDate + "\"" + "\n" +
                    "\"transactionInterval\" : " + nova.getTransactionInterval() + "\n";
        }
        if (typeId != 2 && typeId != 4) {
            query += "\"itemDescription\" : " + "\"" + nova.getItemDescription() + "\"" + "\n";
        }

        query += "\n}";
        System.out.println(query);
    }

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

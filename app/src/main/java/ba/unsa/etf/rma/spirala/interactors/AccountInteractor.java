package ba.unsa.etf.rma.spirala.interactors;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import ba.unsa.etf.rma.spirala.activities.PocetnaAktivnost;
import ba.unsa.etf.rma.spirala.models.Account;
import ba.unsa.etf.rma.spirala.util.TransactionDBOpenHelper;


public class AccountInteractor extends AsyncTask<String, Integer, Void> implements IAccountInteractor {
    private String mainURL = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/";
    private String api_id = "7a4c053e-81fb-42ec-847b-b356864911dc";
    private Account account;


    public AccountInteractor() {
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while (((line = reader.readLine()) != null))
                sb.append(line + "\n");
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        return sb.toString();
    }

    @Override
    protected Void doInBackground(String... strings) {
        URL url = null;
        String url1 = mainURL + api_id;
        //System.out.println(url1 + " u accountI");
        try {
            url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(is);
            JSONObject jsonObject = new JSONObject(result);

            int id = jsonObject.getInt("id");
            double budget = jsonObject.getDouble("budget");
            double totalLimit = jsonObject.getDouble("totalLimit");
            double monthLimit = jsonObject.getDouble("monthLimit");

            account = new Account(id, budget, totalLimit, monthLimit);
            PocetnaAktivnost.account = account;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        PocetnaAktivnost.account = account;
        PocetnaAktivnost.handler.post(new Runnable() {
            @Override
            public void run() {
              /*  String budget = String.valueOf(account.getBudget());
                String mlimit = String.valueOf(account.getMonthLimit());
                PocetnaAktivnost.tVAmount.setText("Budget: " + budget);
                PocetnaAktivnost.tVLimit.setText("Month limit: " + mlimit);*/
            }
        });
    }


    @Override
    public Account getAccountDetail(Context context) {
        return null;
    }

    @Override
    public void editAccount(Context context, double budget, double monthLimit, double totalLimit) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri adresa = Uri.parse("content://rma.provider.accounts/elements");
        ContentValues contentValues = new ContentValues();
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_BUDGET, budget);
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT, monthLimit);
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, totalLimit);
        String where = TransactionDBOpenHelper.ACCOUNT_ID + "=" + TransactionDBOpenHelper.accountID;
        String[] whereArgs = null;
        contentResolver.update(adresa,contentValues,where,whereArgs);
    }

    @Override
    public void insertDetailAccount(Context context, double budget, double monthLimit, double totalLimit) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri adresa = Uri.parse("content://rma.provider.accounts/elements");
        ContentValues contentValues = new ContentValues();
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_ID, TransactionDBOpenHelper.accountID);
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_BUDGET, budget);
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT, monthLimit);
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, totalLimit);
        contentResolver.insert(adresa, contentValues);
    }
}

package ba.unsa.etf.rma.spirala.interactors;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

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


public class AccountInteractor extends IntentService implements IAccountInteractor {
    private String mainURL = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/";
    private String api_id = "7a4c053e-81fb-42ec-847b-b356864911dc";
    private Account account;


    public AccountInteractor() {
         super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        URL url = null;
        String url1 = mainURL + api_id;
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

            PocetnaAktivnost.handler.post(new Runnable() {
                @Override
                public void run() {
                    String budget = String.valueOf(account.getBudget());
                    String mlimit = String.valueOf(account.getMonthLimit());
                    PocetnaAktivnost.tVAmount.setText("Budget: " + budget);
                    PocetnaAktivnost.tVLimit.setText("Month limit: " + mlimit);
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public Account getAccountDetail(Context context) {
        Account account = null;
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        String[] kolone = null;
        Uri adresa = Uri.parse("content://rma.provider.accounts/elements/#");
        String where = TransactionDBOpenHelper.ACCOUNT_ID + "=" + 0;
        String[] whereArgs = null;
        String order = null;
        Cursor cursor = contentResolver.query(adresa, kolone, where, whereArgs, order);
        if (cursor != null) {
            cursor.moveToFirst();
            //int accountid = cursor.getInt(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.ACCOUNT_ID));
            double budget = cursor.getDouble(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.ACCOUNT_BUDGET));
            double monthLimit = cursor.getDouble(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT));
            double totalLimit = cursor.getDouble(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT));
            account = new Account(0, budget, totalLimit, monthLimit);
        }
        return account;
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
        contentResolver.update(adresa, contentValues, where, whereArgs);
    }

    @Override
    public void insertDetailAccount(Context context, double budget, double monthLimit, double totalLimit) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri adresa = Uri.parse("content://rma.provider.accounts/elements/#");
        ContentValues contentValues = new ContentValues();
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_ID, 0);
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_BUDGET, budget);
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT, monthLimit);
        contentValues.put(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, totalLimit);
        contentResolver.insert(adresa, contentValues);
    }


}

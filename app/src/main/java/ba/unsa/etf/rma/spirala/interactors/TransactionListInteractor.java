package ba.unsa.etf.rma.spirala.interactors;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ba.unsa.etf.rma.spirala.models.Transaction;
import ba.unsa.etf.rma.spirala.util.TransactionDBOpenHelper;

public class TransactionListInteractor extends AsyncTask<String, Integer, Void> implements ITransactionListInteractor {

    private String glavniURL = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/";
    private String api_id = "7a4c053e-81fb-42ec-847b-b356864911dc";
    ArrayList<Transaction> transactions;
    private OnTransactionGetDone caller;

    public TransactionListInteractor() {
    }

    public TransactionListInteractor(OnTransactionGetDone p) {
        caller = p;
        transactions = new ArrayList<Transaction>();
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
        String query = "";
        query = strings[0];
        String url2 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/" + api_id + "/transactions" + query;
        try {
            URL pomUrl = new URL(url2);
            HttpURLConnection pomUrlConnection = (HttpURLConnection) pomUrl.openConnection();
            InputStream pomIS = new BufferedInputStream(pomUrlConnection.getInputStream());
            String pomResult = convertStreamToString(pomIS);
            JSONObject pomJsonObject = new JSONObject(pomResult);
            JSONArray pomResults = pomJsonObject.getJSONArray("transactions");
            int page = 0;
            while (pomResults.length() >= 0) {
                String url1 = url2 + "&page=" + page;
                //System.out.println(url1);
                URL url = new URL(url1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = convertStreamToString(in);
                JSONObject jsonObject = new JSONObject(result);
                JSONArray results = jsonObject.getJSONArray("transactions");
                if (results.length() == 0) break;
                for (int i = 0; i < results.length(); i++) {
                    JSONObject transaction = results.getJSONObject(i);
                    //datum
                    int idTransaction = transaction.getInt("id");
                    String date = transaction.getString("date");
                    String tite = transaction.getString("title");
                    double amount = transaction.getDouble("amount");
                    String itemDescription = transaction.getString("itemDescription");
                    String transactionInterval = transaction.getString("transactionInterval");
                    String endDate = transaction.getString("endDate");
                    int transactionTypeId = transaction.getInt("TransactionTypeId");

                    Transaction transaction1 = new Transaction(idTransaction, date, amount, tite, transactionTypeId, itemDescription, transactionInterval, endDate);

                    transactions.add(transaction1);
                }
                pomResults = results;
                page++;
            }
            //AddRegularTransactions(query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Void AddRegularTransactions(String query) {
        ArrayList<Transaction> regularTransactions = new ArrayList<>();
        for (int typeId = 1; typeId <= 2; typeId++) {
            String url3 = glavniURL + api_id + query + "&typeId=" + typeId;
            try {
                URL pomUrl = new URL(url3);
                HttpURLConnection pomUrlConnection = (HttpURLConnection) pomUrl.openConnection();
                InputStream pomIS = new BufferedInputStream(pomUrlConnection.getInputStream());
                String pomResult = convertStreamToString(pomIS);
                JSONObject pomJsonObject = new JSONObject(pomResult);
                JSONArray pomResults = pomJsonObject.getJSONArray("transactions");
                int page = 0;
                while (pomResults.length() >= 0) {
                    String url1 = url3 + "&page=" + page;
                    //System.out.println(url1);
                    URL url = new URL(url1);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String result = convertStreamToString(in);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray results = jsonObject.getJSONArray("transactions");
                    // if (results.length() == 0) break;
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject transaction = results.getJSONObject(i);
                        //datum
                        int idTransaction = transaction.getInt("id");
                        String date = transaction.getString("date");
                        String tite = transaction.getString("title");
                        double amount = transaction.getDouble("amount");
                        String itemDescription = transaction.getString("itemDescription");
                        String transactionInterval = transaction.getString("transactionInterval");
                        String endDate = transaction.getString("endDate");
                        int transactionTypeId = transaction.getInt("TransactionTypeId");

                        Transaction transaction1 = new Transaction(idTransaction, date, amount, tite, transactionTypeId, itemDescription, transactionInterval, endDate);
                        regularTransactions.add(transaction1);
                    }
                    pomResults = results;
                    page++;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //     izbaciRegularne(regularTransactions);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        caller.onGetDone(transactions);
    }

    @Override
    public ArrayList<Transaction> getTransactionCursor(Context context) {
        ArrayList<Transaction> trans = new ArrayList<>();
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = new String[]{
                TransactionDBOpenHelper.TRANSACTION_ID,
                TransactionDBOpenHelper.TRANSACTION_TITLE,
                TransactionDBOpenHelper.TRANSACTION_DATE,
                TransactionDBOpenHelper.TRANSACTION_AMOUNT,
                TransactionDBOpenHelper.TRANSACTION_TYPE,
                TransactionDBOpenHelper.TRANSACTION_DESCRIPTION,
                TransactionDBOpenHelper.TRANSACTION_INTERVAL,
                TransactionDBOpenHelper.TRANSACTION_ENDDATE

        };
        Uri adresa = Uri.parse("content://rma.provider.transactions/elements");
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cursor = cr.query(adresa, kolone, where, whereArgs, order);
        while (cursor.moveToNext()) {
            int idTransaction = cursor.getInt(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_ID));
            System.out.println(idTransaction + "ID TRANSAKCIJEEEEE");
            String title = cursor.getString(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_TITLE));
            String date = cursor.getString(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_DATE));
            double amount = cursor.getDouble(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_AMOUNT));
            int typeId = cursor.getInt(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_TYPE));
            String trensactionDescription = cursor.getString(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_DESCRIPTION));
            String transactionInterval = cursor.getString(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_INTERVAL));
            String enddate = cursor.getString(cursor.getColumnIndex(TransactionDBOpenHelper.TRANSACTION_ENDDATE));
            Transaction t = new Transaction(idTransaction, date, amount, title, typeId, trensactionDescription, transactionInterval, enddate);
            trans.add(t);
        }
        if (trans.size() != 0) {
            TransactionDBOpenHelper.idTransaction = trans.get(trans.size() - 1).getIdTransaction();
        }
        return trans;
    }

    @Override
    public void deleteTransaction(Context context, int idTransasakcije) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri adresa = ContentUris.withAppendedId(Uri.parse("content://rma.provider.transactions/elements"), idTransasakcije);
        String where = TransactionDBOpenHelper.TRANSACTION_ID + "=" + idTransasakcije;
        String[] whereArgs = null;

        cr.delete(adresa, where, whereArgs);

    }

    @Override
    public Transaction getTransaction(Context context, int id) {
        Transaction t = null;
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        String[] kolone = null;
        Uri adresa = ContentUris.withAppendedId(Uri.parse("content://rma.provider.transactions/elements"), id);
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cursor = contentResolver.query(adresa, kolone, where, whereArgs, order);
        if (cursor != null) {
            cursor.moveToFirst();
            int idTransaction = cursor.getInt(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ID));

            String title = cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_DATE));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_AMOUNT));
            int typeId = cursor.getInt(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE));
            String trensactionDescription = cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_DESCRIPTION));
            String transactionInterval = cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_INTERVAL));
            String enddate = cursor.getString(cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ENDDATE));
            t = new Transaction(idTransaction, date, amount, title, typeId, trensactionDescription, transactionInterval, enddate);
        }
        cursor.close();
        return t;
    }

    @Override
    public void saveTransaction(Transaction transaction, Context context) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri adresa = Uri.parse("content://rma.provider.transactions/elements");
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        TransactionDBOpenHelper.idTransaction++;
        contentValues.put(TransactionDBOpenHelper.TRANSACTION_ID, TransactionDBOpenHelper.idTransaction);

        contentValues.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transaction.getTitle());
        contentValues.put(TransactionDBOpenHelper.TRANSACTION_DATE, dateFormat.format(transaction.getDate()));
        contentValues.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transaction.getAmount());
        contentValues.put(TransactionDBOpenHelper.TRANSACTION_TYPE, transaction.getTypeId(transaction.getType()));
        Transaction.Type tip = transaction.getType();
        if (tip == Transaction.Type.PURCHASE || tip == Transaction.Type.REGULARPAYMENT) {
            contentValues.put(TransactionDBOpenHelper.TRANSACTION_DESCRIPTION, transaction.getItemDescription());
        }
        if (tip == Transaction.Type.REGULARINCOME || tip == Transaction.Type.REGULARPAYMENT) {
            contentValues.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, transaction.getTransactionInterval());
            contentValues.put(TransactionDBOpenHelper.TRANSACTION_ENDDATE, dateFormat.format(transaction.getEndDate()));
        }
        contentResolver.insert(adresa, contentValues);
    }

    @Override
    public void editTransaction(Context context, Transaction transaction) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Uri adresa = Uri.parse("content://rma.provider.transactions/elements");
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        contentValues.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transaction.getTitle());
        contentValues.put(TransactionDBOpenHelper.TRANSACTION_DATE, dateFormat.format(transaction.getDate()));
        contentValues.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transaction.getAmount());
        contentValues.put(TransactionDBOpenHelper.TRANSACTION_TYPE, transaction.getTypeId(transaction.getType()));
        Transaction.Type tip = transaction.getType();
        if (tip != Transaction.Type.INDIVIDUALPAYMENT || tip != Transaction.Type.INDIVIDUALINCOME || tip != Transaction.Type.REGULARINCOME) {
            contentValues.put(TransactionDBOpenHelper.TRANSACTION_DESCRIPTION, transaction.getItemDescription());
        }
        if (tip == Transaction.Type.REGULARINCOME || tip == Transaction.Type.REGULARPAYMENT) {
            contentValues.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, transaction.getTransactionInterval());
            contentValues.put(TransactionDBOpenHelper.TRANSACTION_ENDDATE, dateFormat.format(transaction.getEndDate()));
        }
        String where = TransactionDBOpenHelper.TRANSACTION_ID + "=" + transaction.getIdTransaction();
        String[] whereArgs = null;
        contentResolver.update(adresa, contentValues, where, whereArgs);
    }


    public interface OnTransactionGetDone {
        public void onGetDone(ArrayList<Transaction> results);
    }

}

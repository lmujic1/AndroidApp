package ba.unsa.etf.rma.spirala.interactors;

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
import java.util.ArrayList;

import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionListInteractor extends AsyncTask<String, Integer, Void> implements ITransactionListInteractor {

    private String glavniURL = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/";
    private String api_id = "7a4c053e-81fb-42ec-847b-b356864911dc";
    ArrayList<Transaction> transactions;
    private OnTransactionGetDone caller;


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
                System.out.println(url1);
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

   /* protected Void AddInformationAboutAccount(String... params) {
        URL url = null;
        String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/" + api_id;
        try {
            url = new URL(url1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            String result = convertStreamToString(is);
            JSONObject jsonObject = new JSONObject(result);

            int id = jsonObject.getInt("id");
            double budget = jsonObject.getDouble("budget");
            double totalLimit = jsonObject.getDouble("totalLimit");
            double monthLimit = jsonObject.getDouble("monthLimit");
            PocetnaAktivnost.account = new Account(id, budget, totalLimit, monthLimit);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        caller.onGetDone(transactions);
    }

    public interface OnTransactionGetDone {
        public void onGetDone(ArrayList<Transaction> results);
    }


   /* @Override
    public ArrayList<Transaction> getOnMonthAndDeleteTransactions(Date date, ArrayList<Transaction> transactions) {
        ArrayList<Transaction> transactions1 = getOnMonth(date);
        transactions1.removeAll(transactions);
        return  transactions1;
    }*/


}

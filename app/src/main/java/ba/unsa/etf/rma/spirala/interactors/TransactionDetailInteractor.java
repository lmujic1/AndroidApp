package ba.unsa.etf.rma.spirala.interactors;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import ba.unsa.etf.rma.spirala.activities.PocetnaAktivnost;
import ba.unsa.etf.rma.spirala.activities.TransactionDetail;
import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionDetailInteractor extends AsyncTask<String, Integer, Void> {
    private String mainURL = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/";
    private String api_id = "7a4c053e-81fb-42ec-847b-b356864911dc";
    private Transaction transaction;
    private OnTransactionGetDone caller;


    public TransactionDetailInteractor(OnTransactionGetDone p,Transaction transaction) {
        this.transaction =transaction;
        caller = p;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String url1 = mainURL + api_id + "/transactions";
        //System.out.println("U transaction detail interactoru " + url1);
        String forAdd = strings[0];
        //System.out.println(forAdd + "za dodati");
        try {
            URL url = new URL(url1);
            HttpURLConnection postConnection = (HttpURLConnection) url.openConnection();
            postConnection.setDoInput(true);
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Content-Type", "application/json");
            postConnection.setRequestProperty("Accept", "application/json");
            postConnection.setDoOutput(true);

            try (OutputStream os = postConnection.getOutputStream()) {
                byte[] input = forAdd.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode=postConnection.getResponseCode();
            System.out.println("RESPONSE CODE " + responseCode);
            //InputStream in = postConnection.getInputStream();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(postConnection.getInputStream(),"utf-8"))) {

                StringBuilder response = new StringBuilder();
                String inputLine=null;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine.trim());
                }

                Log.d("RESPONSE", response.toString());
                System.out.println("Transakcija je dodana");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        PocetnaAktivnost.transactionListAdapter.dodajTransakciju(transaction);
        //caller.onAddDone(transaction);
    }

    public interface OnTransactionGetDone {
        public void onAddDone(Transaction results);
    }
}

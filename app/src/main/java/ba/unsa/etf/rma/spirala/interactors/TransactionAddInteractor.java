package ba.unsa.etf.rma.spirala.interactors;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionAddInteractor extends IntentService {
    private String mainURL = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/";
    private String api_id = "7a4c053e-81fb-42ec-847b-b356864911dc";
    private Transaction transaction;

    public TransactionAddInteractor(String name) {
        super(name);
    }

    public TransactionAddInteractor() {
        super(null);
    }

    public TransactionAddInteractor(Transaction transaction) {
        super(null);
        this.transaction = transaction;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url1 = mainURL + api_id + "/transactions";
        String forAdd = intent.getStringExtra("query");
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

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(postConnection.getInputStream(), "utf-8"))) {

                StringBuilder response = new StringBuilder();
                String inputLine = null;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine.trim());
                }

                Log.d("RESPONSE", response.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

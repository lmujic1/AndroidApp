package ba.unsa.etf.rma.spirala.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import ba.unsa.etf.rma.spirala.activities.PocetnaAktivnost;
import ba.unsa.etf.rma.spirala.interactors.AccountInteractor;
import ba.unsa.etf.rma.spirala.interactors.UpdateAccountInteractor;
import ba.unsa.etf.rma.spirala.models.Transaction;
import ba.unsa.etf.rma.spirala.presenters.ITransactionListPresenter;
import ba.unsa.etf.rma.spirala.presenters.TransactionListPresenter;
import ba.unsa.etf.rma.spirala.views.ITransactionListView;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {
    Context context;
    private ITransactionListPresenter trasactionListPresenter;

    ITransactionListPresenter getPresenter() {
        if (trasactionListPresenter == null) {
            trasactionListPresenter = new TransactionListPresenter((ITransactionListView) context, context);
        }
        return trasactionListPresenter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            Toast toast = Toast.makeText(context, "Offline Mode", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(context, "Online Mode", Toast.LENGTH_SHORT);
            toast.show();
            if (PocetnaAktivnost.account != null && PocetnaAktivnost.offlineAccount.getBudget() != 0) {
                String query = PocetnaAktivnost.editAccountAfterOfflineMode();
                Intent i = new Intent(Intent.ACTION_SYNC, null, context, UpdateAccountInteractor.class);
                i.putExtra("query", query);
                context.startService(i);
            }
        }
    }
}


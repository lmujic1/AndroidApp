package ba.unsa.etf.rma.spirala.presenters;

import android.content.Context;
import android.os.Parcelable;

import java.util.Date;

import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionDetailPresenter implements
        ITransactionDetailPresenter {

    private Context context;
    private Transaction transaction;

    public TransactionDetailPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void create(Date date, double amount, String title, Transaction.Type type, String itemDescription, int transactionInterval, Date endDate) {
        this.transaction = new  Transaction(date,amount,title,type,itemDescription,transactionInterval,endDate);
    }

    @Override
    public void setTransaction(Parcelable transaction) {
        this.transaction = (Transaction)transaction;
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }
}

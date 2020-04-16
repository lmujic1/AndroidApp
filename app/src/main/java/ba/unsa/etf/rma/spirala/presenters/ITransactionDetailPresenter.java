package ba.unsa.etf.rma.spirala.presenters;

import android.os.Parcelable;

import java.util.Date;

import ba.unsa.etf.rma.spirala.models.Transaction;

public interface ITransactionDetailPresenter {
    void create(Date date, double amount, String title, Transaction.Type type, String itemDescription, int transactionInterval, Date endDate);
    void setTransaction(Parcelable transaction);
    Transaction getTransaction();
}

package ba.unsa.etf.rma.spirala.interactors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ba.unsa.etf.rma.spirala.models.Transaction;
import ba.unsa.etf.rma.spirala.models.TransactionModel;

public class TransactionListInteractor implements  ITransactionListInteractor{
    public ArrayList<Transaction> get() {
        return TransactionModel.transactions;
    }

    public ArrayList<Transaction> getOnMonth(Date date) {
        ArrayList<Transaction> trans = new ArrayList<>();
        ArrayList<Transaction> transactions = new ArrayList<>();
        trans.addAll(TransactionModel.transactions);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        long mjesec = cal.get(Calendar.MONTH) + 1, godina = cal.get(Calendar.YEAR);
        for (Transaction t : trans) {
            Calendar c1 = new GregorianCalendar();
            c1.setTime(t.getDate());
            int tmj = c1.get(Calendar.MONTH) + 1, tgod = c1.get(Calendar.YEAR);
            if (tmj == mjesec && tgod == godina)
                transactions.add(t);
        }

        return transactions;
    }

    public void deleteTransaction(Transaction transaction) {
        TransactionModel.deleteTransaction(transaction);
    }
}

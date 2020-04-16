package ba.unsa.etf.rma.spirala.presenters;

import java.util.Date;

public interface ITransactionListPresenter {
    void refreshTransaction();
    void refreshTransactionOnDate(Date date);
    void refreshTransactionSort(String string);
    void refreshTransactionFilter(Date date, String string);
}

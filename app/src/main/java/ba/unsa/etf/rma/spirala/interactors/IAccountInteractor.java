package ba.unsa.etf.rma.spirala.interactors;

import android.content.Context;

import ba.unsa.etf.rma.spirala.models.Account;

public interface IAccountInteractor {
    Account getAccountDetail(Context context);
    void editAccount(Context context, double budget, double monthLimit, double totalLimit);
    void insertDetailAccount(Context context, double budget, double monthLimit, double totalLimit);
}

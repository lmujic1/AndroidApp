/*
package ba.unsa.etf.rma.spirala.presenters;

import ba.unsa.etf.rma.spirala.fragments.IAccountView;
import ba.unsa.etf.rma.spirala.activities.PocetnaAktivnost;
import ba.unsa.etf.rma.spirala.interactors.AccountInteractor;
import ba.unsa.etf.rma.spirala.models.Account;

public class AccountPresenter implements IAccountPresenter, AccountInteractor.GetAccountInfo {
    private IAccountView accountView;

    public AccountPresenter(IAccountView accountView) {
        this.accountView = accountView;
    }

    @Override
    public void getInfoAboutAccount() {
        new AccountInteractor((AccountInteractor.GetAccountInfo) this).execute();
    }

    @Override
    public void getInfo(Account account) {
        PocetnaAktivnost.account = account;
    }
}
*/

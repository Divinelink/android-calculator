package handreolas.divinelink.calculator.currency;

import android.content.Context;

public class CurrencyPresenterImpl implements ICurrencyPresenter, ICurrencyInteractor.OnGetCurrencyResultFinishListener {

    private final ICurrencyView currencyView;
    private final ICurrencyInteractor interactor;

    public CurrencyPresenterImpl(ICurrencyView currencyView) {
        this.currencyView = currencyView;
        interactor = new CurrencyInteractorImpl();
    }

    @Override
    public void getCurrencyRatios(Context ctx) {

    }

    @Override
    public void onShowResult(String currencyOne, String currencyTwo, String currencyThree) {

    }

    @Override
    public void onClear() {

    }

    @Override
    public void onTooManyDigits() {

    }

    @Override
    public void onError() {

    }
}

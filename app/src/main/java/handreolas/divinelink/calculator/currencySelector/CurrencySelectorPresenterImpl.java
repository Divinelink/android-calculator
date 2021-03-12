package handreolas.divinelink.calculator.currencySelector;

import android.content.Context;

import java.util.ArrayList;

import handreolas.divinelink.calculator.currency.CurrencyInteractorImpl;
import handreolas.divinelink.calculator.currency.ICurrencyInteractor;
import handreolas.divinelink.calculator.currency.CurrencyDomain;

public class CurrencySelectorPresenterImpl implements ICurrencySelectorPresenter, ICurrencyInteractor.OnGetCurrencyResultListener{

    private final ICurrencyInteractor interactor;
    private final ICurrencySelectorView currencySelectorView;

    public CurrencySelectorPresenterImpl(ICurrencySelectorView currencySelectorView) {
        this.currencySelectorView = currencySelectorView;
        interactor = new CurrencyInteractorImpl();
    }


    @Override
    public void onShowSymbols(ArrayList<CurrencyDomain> symbols, int position) {
        currencySelectorView.showCurrencyListOnSelector(symbols, position);
    }

    @Override
    public void getCurrencyList(int position, Context ctx) {
        interactor.onTextViewClick(this, ctx, position);
    }

    @Override
    public void onShowResult(String currencyOne, String currencyTwo, String currencyThree) {

    }

    @Override
    public void onUpdateTime(Long date) {

    }

    @Override
    public void onUpdateCurrencyRates(ArrayList<String> rates, int selectedPosition) {

    }

    @Override
    public void onBeforeUpdateTime(String updating) {

    }

    @Override
    public void onShowSymbols(ArrayList<CurrencyDomain> symbols) {

    }

    @Override
    public void onAddSingleCharOnCurrentRate(String currentRate, int position) {

    }

    @Override
    public void onAddSelectorFragment(int position) {

    }

    @Override
    public void onClear() {

    }

    @Override
    public void doNothing() {

    }

    @Override
    public void onError(Context ctx, int errorCode) {

    }
}

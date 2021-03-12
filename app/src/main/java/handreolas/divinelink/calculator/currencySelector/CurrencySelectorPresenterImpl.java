package handreolas.divinelink.calculator.currencySelector;

import android.content.Context;

import java.util.ArrayList;

import handreolas.divinelink.calculator.currency.CurrencyInteractorImpl;
import handreolas.divinelink.calculator.currency.ICurrencyInteractor;
import handreolas.divinelink.calculator.currency.SymbolsDomain;

public class CurrencySelectorPresenterImpl implements ICurrencySelectorPresenter, ICurrencyInteractor.OnGetCurrencyResultListener {

    private final ICurrencyInteractor interactor;
    private final ICurrencySelectorView currencySelectorView;

    public CurrencySelectorPresenterImpl(ICurrencySelectorView currencySelectorView) {
        this.currencySelectorView = currencySelectorView;
        interactor = new CurrencyInteractorImpl();
    }


    @Override
    public void onShowSymbols(ArrayList<SymbolsDomain> symbols, int position) {
        currencySelectorView.showCurrencyListOnSelector(symbols, position);
    }

    @Override
    public void onUpdateTime(Long date) {

    }

    @Override
    public void onShowSymbols(ArrayList<SymbolsDomain> symbols) {

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

    @Override
    public void onBeforeUpdateTime(String updating) {

    }

    @Override
    public void getCurrencyList(int position, Context ctx) {
        interactor.onTextViewClick(this, ctx, position);
    }

    @Override
    public void onShowResult(String currencyOne, String currencyTwo, String currencyThree) {

    }


    @Override
    public void onUpdateCurrencyRates(ArrayList<Double> rates, int selectedPosition) {

    }
}

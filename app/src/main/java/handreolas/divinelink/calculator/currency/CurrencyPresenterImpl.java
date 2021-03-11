package handreolas.divinelink.calculator.currency;

import android.content.Context;

import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;

public class CurrencyPresenterImpl implements ICurrencyPresenter, ICurrencyInteractor.OnGetCurrencyResultFinishListener {

    private final ICurrencyView currencyView;
    private final ICurrencyInteractor interactor;

    public CurrencyPresenterImpl(ICurrencyView currencyView) {
        this.currencyView = currencyView;
        interactor = new CurrencyInteractorImpl();
    }

    @Override
    public void getCurrencyRatios(Context ctx) {
        interactor.getSymbols(this, ctx);
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

    @Override
    public void getCurrencyList(int position, Context ctx) {
        interactor.onTextViewClick(this, ctx, position);
    }

    @Override
    public void onShowSymbols(ArrayList<SymbolsDomain> symbols, int position) {
        currencyView.showCurrencyList(symbols, position);
    }

    @Override
    public void onShowSymbols(ArrayList<SymbolsDomain> symbols) {

//        symbols.get(0).getSymbols().get(0);
//        symbols.get(0).getSymbols().values();
//
//
//        symbols.get(1).getSymbols().get(0);
//        symbols.get(1).getSymbols().values();
//
//        symbols.get(2).getSymbols().get(0);
//        symbols.get(2).getSymbols().values();
//
//        currencyView.showSymbols(  symbols.get(0).getSymbols().get(0),   symbols.get(1).getSymbols().get(0),   symbols.get(1).getSymbols().get(0));
    }
}

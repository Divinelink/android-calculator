package handreolas.divinelink.calculator.currency;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CurrencyPresenterImpl implements ICurrencyPresenter, ICurrencyInteractor.OnGetCurrencyResultListener {

    private final ICurrencyInteractor interactor;
    private final ICurrencyView currencyView;

    public CurrencyPresenterImpl(ICurrencyView currencyView) {
        this.currencyView = currencyView;
        interactor = new CurrencyInteractorImpl();
    }



    @Override
    public void getCurrencyRatios(Context ctx) {
        interactor.getSymbols(this, ctx);
        interactor.getRates(this, ctx);
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
    public void onUpdateTime(Long timestamp) {

        String currentDate = String.format("%s", new SimpleDateFormat("dd MMMM yyyy HH:mm").format(new Date(timestamp*1000)));
        currencyView.updateTime(currentDate);

    }

    @Override
    public void onBeforeUpdateTime(String updating) {
        currencyView.updateTimeBeforeCall(updating);
    }

    @Override
    public void getCurrencyList(int position, Context ctx) {

    }

    @Override
    public void onShowSymbols(ArrayList<SymbolsDomain> symbols, int position) {
        currencyView.showCurrencyList(symbols, position);
    }


    @Override
    public void onShowSymbols(ArrayList<SymbolsDomain> symbols) {

    }
    @Override
    public void onUpdateCurrencyRates(ArrayList<Double> rates, int selectedPosition)  {
        currencyView.updateCurrencyRates(
                rates.get(0) / rates.get(selectedPosition),
                rates.get(1) / rates.get(selectedPosition),
                rates.get(2) / rates.get(selectedPosition));
    }


    @Override
    public void calculateRates(int position, long value, Context ctx) {
        interactor.calculateRates(this, ctx, position);
    }
}

package handreolas.divinelink.calculator.currency;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import handreolas.divinelink.calculator.features.CalculatorHelper;

public class CurrencyPresenterImpl implements ICurrencyPresenter, ICurrencyInteractor.OnGetCurrencyResultListener {

    private final ICurrencyInteractor interactor;
    private final ICurrencyView currencyView;
    private final CalculatorHelper calculatorHelper = new CalculatorHelper();

    public CurrencyPresenterImpl(ICurrencyView currencyView) {
        this.currencyView = currencyView;
        interactor = new CurrencyInteractorImpl();
    }


    @Override
    public void getCurrencySymbols(Context ctx) {
        interactor.getSymbols(this, ctx);

    }

    @Override
    public void getCurrencySelectorFragment(Context ctx, int position) {
        interactor.getSymbolSelectorFragment(this, ctx, position);
    }

    @Override
    public void getCurrencyRates(Context ctx, boolean refresh) {
        interactor.getRates(this, ctx, refresh);
    }

    @Override
    public void onShowResult(String currencyOne, String currencyTwo, String currencyThree) {

    }

    @Override
    public void onClear() {
        currencyView.updateCurrencyRates("0", "0", "0");
    }

    @Override
    public void doNothing() {

    }

    @Override
    public void onError(Context ctx, int errorCode) {
        currencyView.onError(errorCode);
    }

    @Override
    public void onUpdateTime(Long timestamp) {

        String currentDate = String.format("%s", new SimpleDateFormat("dd MMMM yyyy HH:mm").format(new Date(timestamp * 1000)));
        currencyView.updateTime(currentDate);

    }

    @Override
    public void onBeforeUpdateTime(String updating) {
        currencyView.updateTimeBeforeCall(updating);
    }


    @Override
    public void onShowSymbols(ArrayList<CurrencyDomain> symbols, int position) {

    }


    @Override
    public void onShowSymbols(ArrayList<CurrencyDomain> symbols) {

    }

    @Override
    public void onUpdateCurrencyRates(ArrayList<String> rates, int selectedPosition) {

        currencyView.updateCurrencyRates(
                calculatorHelper.formatCurrencyNumber(rates.get(0),  selectedPosition != 0),
                calculatorHelper.formatCurrencyNumber(rates.get(1),  selectedPosition != 1),
                calculatorHelper.formatCurrencyNumber(rates.get(2), selectedPosition != 2));

    }

    @Override
    public void insertNumber(String value, String insertedNumber, Context ctx) {
        interactor.insertNumber(this, ctx, insertedNumber, value);
    }

    @Override
    public void clearValues(Context ctx) {
        interactor.clearValues(this, ctx);
    }

    @Override
    public void calculateRates(String value, Context ctx) {
        interactor.calculateRates(this, ctx, value);
    }

    @Override
    public void insertComma(Context ctx, String currencyValue) {
        interactor.insertComma(this, ctx, currencyValue);
    }

    @Override
    public void onAddSingleCharOnCurrentRate(String currentRate, int position) {
        currencyView.addCommaOnCurrentRate(calculatorHelper.formatCurrencyNumber(currentRate, false), position);
    }

    @Override
    public void backspace(Context ctx, String currentValue) {
        interactor.backspace(this, ctx, currentValue);
    }

    @Override
    public void onAddSelectorFragment(int position) {
        currencyView.addCurrencySelectorFragment(position);
    }
}

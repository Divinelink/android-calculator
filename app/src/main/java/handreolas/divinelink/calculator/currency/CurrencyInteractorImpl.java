package handreolas.divinelink.calculator.currency;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import handreolas.divinelink.calculator.R;
import handreolas.divinelink.calculator.base.HomeDatabase;
import handreolas.divinelink.calculator.features.CalculatorHelper;
import handreolas.divinelink.calculator.features.SharedPreferenceManager;
import handreolas.divinelink.calculator.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static handreolas.divinelink.calculator.features.CalculatorHelper.DIVISION;
import static handreolas.divinelink.calculator.features.CalculatorHelper.MULTIPLICATION;

public class CurrencyInteractorImpl implements ICurrencyInteractor {

    private final SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager();
    private final CalculatorHelper calculatorHelper = new CalculatorHelper();

    @Override
    public void getSymbols(OnGetCurrencyResultListener listener, Context ctx) {
        AsyncTask.execute(() -> {
            final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();

            List<CurrencyDomain> currenciesFromDB = currencyDao.getCurrencySymbols();
            ArrayList<CurrencyDomain> arrayListCurrencies = new ArrayList<>(currenciesFromDB);

            if (currenciesFromDB.isEmpty()) { // If Symbols don't exist on DB, retrieve them through an API call

                Call<CurrencySymbolsModel> call = RestClient.call().fetchSymbols();
                call.enqueue(new Callback<CurrencySymbolsModel>() {
                    @Override
                    public void onResponse(Call<CurrencySymbolsModel> call, Response<CurrencySymbolsModel> response) {
                        AsyncTask.execute(() -> {
                            boolean isDBNull; // Check if DB is null and don't contains Currency Symbols & Name.
                            try {
                                isDBNull = currencyDao.getCurrencySymbols().get(0) == null;
                            } catch (Exception e) {
                                isDBNull = true;
                            }

                            for (Map.Entry<String, String> entry : response.body().getSymbols().entrySet()) {
                                if (isDBNull)
                                    currencyDao.insertCurrencyDomain(new CurrencyDomain(entry.getKey(), entry.getValue()));
                                else
                                    currencyDao.updateSymbol(entry.getKey(), entry.getValue());
                            }
                            arrayListCurrencies.addAll(currencyDao.getCurrencySymbols());
                            listener.onShowSymbols(arrayListCurrencies);
                            Log.d("Response For Symbols", "Successfully got response.");
                        });
                    }

                    @Override
                    public void onFailure(Call<CurrencySymbolsModel> call, Throwable t) {
                        Log.d("Response For Symbols", "Failed to get response.");
                        listener.onError(ctx, 0);
                    }
                });
            } else {
                listener.onShowSymbols(arrayListCurrencies);
            }
        });
    }

    @Override
    public void getRates(OnGetCurrencyResultListener listener, Context ctx, boolean refresh) {
        listener.onBeforeUpdateTime(ctx.getResources().getString(R.string.updating_values));
        AsyncTask.execute(() -> {
            final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();
            final int selectedPosition = sharedPreferenceManager.getSelectedPosition(ctx);

            Call<CurrencyRateModel> call = RestClient.call().fetchLatestRates();
            call.enqueue(new Callback<CurrencyRateModel>() {
                @Override
                public void onResponse(Call<CurrencyRateModel> call, Response<CurrencyRateModel> response) {
                    AsyncTask.execute(() -> {
                        boolean isDBNull; // Check if DB is null and don't contains Currency Symbols & Name.
                        try {
                            isDBNull = currencyDao.getCurrencySymbols().get(0) == null;
                        } catch (Exception e) {
                            isDBNull = true;
                        }

                        for (Map.Entry<String, Double> entry : response.body().getRates().entrySet()) {
                            if (isDBNull || !refresh)
                                currencyDao.insertCurrencyDomain(new CurrencyDomain(entry.getKey(), entry.getValue()));
                            else
                                currencyDao.updateRate(entry.getKey(), entry.getValue());
                        }
                        listener.onUpdateTime(response.body().getTimestamp());
                        listener.onUpdateCurrencyRates(returnCalculatedRates("1", ctx), selectedPosition);
                        Log.d("Response For Rates", "Successfully got response.");

                    });
                }

                @Override
                public void onFailure(Call<CurrencyRateModel> call, Throwable t) {
                    Log.d("Response For Rates", "Failed to get response.");
                    listener.onError(ctx, 0);
                }
            });
        });

    }

    @Override
    public void onTextViewClick(OnGetCurrencyResultListener listener, Context ctx, int position) {
        AsyncTask.execute(() -> {
            final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();

            List<CurrencyDomain> currenciesFromDB = currencyDao.getCurrencySymbols();
            ArrayList<CurrencyDomain> arrayListCurrencies = new ArrayList<>(currenciesFromDB);

            if (currenciesFromDB.isEmpty()) { // If Symbols don't exist on DB, retrieve them through an API call
                Call<CurrencySymbolsModel> call = RestClient.call().fetchSymbols();
                call.enqueue(new Callback<CurrencySymbolsModel>() {
                    @Override
                    public void onResponse(Call<CurrencySymbolsModel> call, Response<CurrencySymbolsModel> response) {
                        AsyncTask.execute(() -> {

                            Log.d("Response For Symbols", "Successfully got response.");
                            for (Map.Entry<String, String> entry : response.body().getSymbols().entrySet()) {
                                currencyDao.insertCurrencyDomain(new CurrencyDomain(entry.getKey(), entry.getValue(), null));
                            }
                            arrayListCurrencies.addAll(currencyDao.getCurrencySymbols());
                            listener.onShowSymbols(arrayListCurrencies, position);
                        });
                    }

                    @Override
                    public void onFailure(Call<CurrencySymbolsModel> call, Throwable t) {
                        Log.d("Response For Symbols", "Failed to get response.");
                        listener.onError(ctx, 1);
                    }
                });
            } else {
                listener.onShowSymbols(arrayListCurrencies, position);
            }
        });
    }

    @Override
    public void calculateRates(OnGetCurrencyResultListener listener, Context ctx, String value) {
        AsyncTask.execute(() -> {
            final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();
            final String firstCur = sharedPreferenceManager.getSavedCurrencySymbol(0, ctx);
            final String secondCur = sharedPreferenceManager.getSavedCurrencySymbol(1, ctx);
            final String thirdCur = sharedPreferenceManager.getSavedCurrencySymbol(2, ctx);

            if (currencyDao.getRateForCurrency(firstCur) == null || currencyDao.getRateForCurrency(secondCur) == null || currencyDao.getRateForCurrency(thirdCur) == null)
                listener.onError(ctx, 0);
            else {
                final int position = sharedPreferenceManager.getSelectedPosition(ctx);
                listener.onUpdateCurrencyRates(returnCalculatedRates(value, ctx), position);
            }

        });
    }

    @Override
    public void insertNumber(OnGetCurrencyResultListener listener, Context ctx, String insertedNumber, String currentValue) {
        AsyncTask.execute(() -> {
            final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();
            final String firstCur = sharedPreferenceManager.getSavedCurrencySymbol(0, ctx);
            final String secondCur = sharedPreferenceManager.getSavedCurrencySymbol(1, ctx);
            final String thirdCur = sharedPreferenceManager.getSavedCurrencySymbol(2, ctx);
            final int position = sharedPreferenceManager.getSelectedPosition(ctx);

            if (currencyDao.getRateForCurrency(firstCur) == null || currencyDao.getRateForCurrency(secondCur) == null || currencyDao.getRateForCurrency(thirdCur) == null)
                listener.onError(ctx, 2);
            else {
                if (calculatorHelper.isDecimal(currentValue)) {
                    if (calculatorHelper.getFractionDigits(currentValue) < 4) { // User Is Allowed to Enter only 4 decimal digits
                        final String insertedValue = calculatorHelper.addDigitIfNumberNonNull(currentValue, insertedNumber);
                        ArrayList<String> rates = returnCalculatedRates(insertedValue, ctx);
                        listener.onUpdateCurrencyRates(rates, position);
                    } else {
                        listener.doNothing();
                    }
                } else {
                    if (calculatorHelper.getIntegerDigits(currentValue) < 15) { // User Is Allowed to Enter only 15 integer digits
                        final String insertedValue = calculatorHelper.addDigitIfNumberNonNull(currentValue, insertedNumber);
                        ArrayList<String> rates = returnCalculatedRates(insertedValue, ctx);
                        listener.onUpdateCurrencyRates(rates, position);
                    } else {
                        listener.doNothing();
                    }
                }
            }

        });
    }

    @Override
    public void getSymbolSelectorFragment(OnGetCurrencyResultListener listener, Context ctx, int position) {
        AsyncTask.execute(() -> {
            final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();

            boolean isDBNull; // Check if DB is null and don't contains Currency Symbols & Name.
            try {
                isDBNull = currencyDao.getCurrencySymbols().get(0).getSymbol() == null;
            } catch (Exception e) {
                isDBNull = true;
            }

            if (isDBNull)
                listener.onError(ctx, 1);
            else
                listener.onAddSelectorFragment(position);
        });
    }

    @Override
    public void clearValues(OnGetCurrencyResultListener listener, Context ctx) {
        listener.onClear();
    }

    @Override
    public void insertComma(OnGetCurrencyResultListener listener, Context ctx, String currencyValue) {

        AsyncTask.execute(() -> {
            final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();
            final String firstCur = sharedPreferenceManager.getSavedCurrencySymbol(0, ctx);
            final String secondCur = sharedPreferenceManager.getSavedCurrencySymbol(1, ctx);
            final String thirdCur = sharedPreferenceManager.getSavedCurrencySymbol(2, ctx);
            final int position = sharedPreferenceManager.getSelectedPosition(ctx);

            if (currencyDao.getRateForCurrency(firstCur) == null || currencyDao.getRateForCurrency(secondCur) == null || currencyDao.getRateForCurrency(thirdCur) == null)
                listener.onError(ctx, 2);
            else {
                String insertedValue = calculatorHelper.addCommaToNumber(currencyValue);
                listener.onAddSingleCharOnCurrentRate(insertedValue, position);
            }
        });
    }

    @Override
    public void backspace(OnGetCurrencyResultListener listener, Context ctx, String currentValue) {

        final int position = sharedPreferenceManager.getSelectedPosition(ctx);

        AsyncTask.execute(() -> {

            if (currentValue.length() == 1)
                listener.onClear();
            else {
                String removedValue = calculatorHelper.removeLastElementFromStringForCurrencies(currentValue);
                ArrayList<String> rates = returnCalculatedRates(removedValue, ctx);

                listener.onUpdateCurrencyRates(rates, position);
            }

        });

    }

    private ArrayList<String> returnCalculatedRates(String value, Context ctx) {
        ArrayList<String> rates = new ArrayList<>();
        final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();
        final String firstCur = sharedPreferenceManager.getSavedCurrencySymbol(0, ctx);
        final String secondCur = sharedPreferenceManager.getSavedCurrencySymbol(1, ctx);
        final String thirdCur = sharedPreferenceManager.getSavedCurrencySymbol(2, ctx);
        final int position = sharedPreferenceManager.getSelectedPosition(ctx);
        final String selectedCur = sharedPreferenceManager.getSavedCurrencySymbol(position, ctx);

        String firstRate = Double.toString(currencyDao.getRateForCurrency(firstCur));
        String secondRate = Double.toString(currencyDao.getRateForCurrency(secondCur));
        String thirdRate = Double.toString(currencyDao.getRateForCurrency(thirdCur));
        String selectedRate = Double.toString(currencyDao.getRateForCurrency(selectedCur));

        firstRate = calculatorHelper.calculateCurrencies(firstRate, selectedRate, DIVISION);
        secondRate = calculatorHelper.calculateCurrencies(secondRate, selectedRate, DIVISION);
        thirdRate = calculatorHelper.calculateCurrencies(thirdRate, selectedRate, DIVISION);

        firstRate = calculatorHelper.calculateCurrencies(firstRate, value, MULTIPLICATION);
        secondRate = calculatorHelper.calculateCurrencies(secondRate, value, MULTIPLICATION);
        thirdRate = calculatorHelper.calculateCurrencies(thirdRate, value, MULTIPLICATION);

        if (position == 0) {
            rates.add(value);
            rates.add(secondRate);
            rates.add(thirdRate);
        } else if (position == 1) {
            rates.add(firstRate);
            rates.add(value);
            rates.add(thirdRate);
        } else {
            rates.add(firstRate);
            rates.add(secondRate);
            rates.add(value);
        }

        return rates;
    }

}

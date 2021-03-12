package handreolas.divinelink.calculator.currency;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import handreolas.divinelink.calculator.R;
import handreolas.divinelink.calculator.base.HomeDatabase;
import handreolas.divinelink.calculator.features.SharedPreferenceManager;
import handreolas.divinelink.calculator.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyInteractorImpl implements ICurrencyInteractor {

    final SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager();


    @Override
    public void getSymbols(OnGetCurrencyResultListener listener, Context ctx) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();

                List<SymbolsDomain> currenciesFromDB = currencyDao.getCurrencySymbols();
                ArrayList<SymbolsDomain> arrayListCurrencies = new ArrayList<>(currenciesFromDB);


                if (currenciesFromDB.isEmpty()) { // If Symbols don't exist on DB, retrieve them through an API call

                    Call<CurrencySymbolsModel> call = RestClient.call().fetchSymbols();
                    call.enqueue(new Callback<CurrencySymbolsModel>() {
                        @Override
                        public void onResponse(Call<CurrencySymbolsModel> call, Response<CurrencySymbolsModel> response) {
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    Log.d("Response For Symbols", "Successfully got response.");
                                    for (Map.Entry<String, String> entry : response.body().getSymbols().entrySet()) {
                                        currencyDao.insertCurrencySymbols(new SymbolsDomain(entry.getKey(), entry.getValue()));
                                    }
                                    arrayListCurrencies.addAll(currencyDao.getCurrencySymbols());
                                    listener.onShowSymbols(arrayListCurrencies);

                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<CurrencySymbolsModel> call, Throwable t) {
                            Log.d("Response For Symbols", "Failed to get response.");
                        }
                    });
                } else {
                    listener.onShowSymbols(arrayListCurrencies);
                }
            }
        });
    }

    @Override
    public void getRates(OnGetCurrencyResultListener listener, Context ctx) {
        listener.onBeforeUpdateTime(ctx.getResources().getString(R.string.updating_values));
        AsyncTask.execute(() -> {
            final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();
            final String firstCur = sharedPreferenceManager.getSavedCurrencySymbol(0, ctx);
            final String secondCur = sharedPreferenceManager.getSavedCurrencySymbol(1, ctx);
            final String thirdCur = sharedPreferenceManager.getSavedCurrencySymbol(2, ctx);
            final int selectedPosition = sharedPreferenceManager.getSelectedPosition(ctx);


            Call<CurrencyRateModel> call = RestClient.call().fetchLatestRates();
            call.enqueue(new Callback<CurrencyRateModel>() {
                @Override
                public void onResponse(Call<CurrencyRateModel> call, Response<CurrencyRateModel> response) {
                    Log.d("Response For Rates", "Successfully got response.");
                    AsyncTask.execute(() -> {
                        for (Map.Entry<String, Double> entry : response.body().getRates().entrySet()) {
                            currencyDao.updateRate(entry.getKey(), entry.getValue());
                        }
                        listener.onUpdateTime(response.body().getTimestamp());

                        ArrayList<Double> arrayList = new ArrayList<>();
                        arrayList.add(currencyDao.getRateForCurrency(firstCur));
                        arrayList.add(currencyDao.getRateForCurrency(secondCur));
                        arrayList.add(currencyDao.getRateForCurrency(thirdCur));

                        listener.onUpdateCurrencyRates(arrayList, selectedPosition);

                    });
                }

                @Override
                public void onFailure(Call<CurrencyRateModel> call, Throwable t) {
                    Log.d("Response For Rates", "Failed to get response.");
                }
            });
        });

    }


    @Override
    public void setNumber(OnGetCurrencyResultListener listener, Context ctx, String number) {

    }

    @Override
    public void onTextViewClick(OnGetCurrencyResultListener listener, Context ctx, int position) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();

                List<SymbolsDomain> currenciesFromDB = currencyDao.getCurrencySymbols();
                ArrayList<SymbolsDomain> arrayListCurrencies = new ArrayList<>(currenciesFromDB);


                if (currenciesFromDB.isEmpty()) { // If Symbols don't exist on DB, retrieve them through an API call

                    Call<CurrencySymbolsModel> call = RestClient.call().fetchSymbols();
                    call.enqueue(new Callback<CurrencySymbolsModel>() {
                        @Override
                        public void onResponse(Call<CurrencySymbolsModel> call, Response<CurrencySymbolsModel> response) {
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {

                                    Log.d("Response For Symbols", "Successfully got response.");
                                    for (Map.Entry<String, String> entry : response.body().getSymbols().entrySet()) {
                                        currencyDao.insertCurrencySymbols(new SymbolsDomain(entry.getKey(), entry.getValue(), null));
                                    }
                                    arrayListCurrencies.addAll(currencyDao.getCurrencySymbols());
                                    listener.onShowSymbols(arrayListCurrencies, position);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<CurrencySymbolsModel> call, Throwable t) {
                            Log.d("Response For Symbols", "Failed to get response.");
                        }
                    });
                } else {
                    listener.onShowSymbols(arrayListCurrencies, position);
                }
            }
        });
    }

    @Override
    public void calculateRates(OnGetCurrencyResultListener listener, Context ctx, int position) {
        AsyncTask.execute(() -> {

            final CurrencyDao currencyDao = HomeDatabase.getDatabase(ctx).currencyDao();
            final String firstCur = sharedPreferenceManager.getSavedCurrencySymbol(0, ctx);
            final String secondCur = sharedPreferenceManager.getSavedCurrencySymbol(1, ctx);
            final String thirdCur = sharedPreferenceManager.getSavedCurrencySymbol(2, ctx);
            final String selectedCur = sharedPreferenceManager.getSavedCurrencySymbol(position, ctx);
            ArrayList<Double> rates = new ArrayList<>();

            Double firstRate = currencyDao.getRateForCurrency(firstCur) / currencyDao.getRateForCurrency(selectedCur);
            Double secondRate = currencyDao.getRateForCurrency(secondCur) / currencyDao.getRateForCurrency(selectedCur);
            Double thirdRate = currencyDao.getRateForCurrency(thirdCur) / currencyDao.getRateForCurrency(selectedCur);

            rates.add(firstRate);
            rates.add(secondRate);
            rates.add(thirdRate);

            listener.onUpdateCurrencyRates(rates, position);


        });
    }
}

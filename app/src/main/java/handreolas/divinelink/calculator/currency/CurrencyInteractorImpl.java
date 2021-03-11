package handreolas.divinelink.calculator.currency;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import handreolas.divinelink.calculator.base.HomeDatabase;
import handreolas.divinelink.calculator.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyInteractorImpl implements ICurrencyInteractor {

//    private final static String API_KEY = "390279d0a198a604020f9761a80e918f";

    @Override
    public void getSymbols(OnGetCurrencyResultFinishListener listener, Context ctx) {
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
    public void setNumber(OnGetCurrencyResultFinishListener listener, Context ctx, String number) {

    }

    @Override
    public void onTextViewClick(OnGetCurrencyResultFinishListener listener, Context ctx, int position) {
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
}

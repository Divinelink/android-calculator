package handreolas.divinelink.calculator.rest;


import handreolas.divinelink.calculator.currency.CurrencyRateModel;
import handreolas.divinelink.calculator.currency.CurrencySymbolsModel;
import retrofit2.Call;
import retrofit2.http.GET;


public interface RestAPI {

    Call<CurrencyRateModel> fetchLatestRates();


    Call<CurrencySymbolsModel> fetchSymbols();

}

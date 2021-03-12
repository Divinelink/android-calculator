package handreolas.divinelink.calculator.rest;


import handreolas.divinelink.calculator.currency.CurrencyRateModel;
import handreolas.divinelink.calculator.currency.CurrencySymbolsModel;
import retrofit2.Call;
import retrofit2.http.GET;


public interface RestAPI {

    @GET("latest?access_key=")
    Call<CurrencyRateModel> fetchLatestRates();


    @GET("symbols?access_key=")
    Call<CurrencySymbolsModel> fetchSymbols();

}

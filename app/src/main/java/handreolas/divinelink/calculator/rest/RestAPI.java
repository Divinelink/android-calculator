package handreolas.divinelink.calculator.rest;


import handreolas.divinelink.calculator.currency.CurrencyRateModel;
import handreolas.divinelink.calculator.currency.CurrencySymbolsModel;
import retrofit2.Call;
import retrofit2.http.GET;


public interface RestAPI {

    @GET("latest?access_key=f71af430fc192cbb2e4ed4045799778d")
    Call<CurrencyRateModel> fetchLatestRates();


    @GET("symbols?access_key=f71af430fc192cbb2e4ed4045799778d")
    Call<CurrencySymbolsModel> fetchSymbols();

}

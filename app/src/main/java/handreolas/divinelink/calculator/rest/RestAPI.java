package handreolas.divinelink.calculator.rest;

import org.json.JSONArray;

import java.util.ArrayList;

import handreolas.divinelink.calculator.currency.CurrencyRateModel;
import handreolas.divinelink.calculator.currency.CurrencySymbolsModel;
import retrofit2.Call;
import retrofit2.http.GET;


public interface RestAPI {

    @GET("latest?access_key={API_KEY}")
    Call<ArrayList<CurrencyRateModel>> fetchLatest();



    @GET("symbols?access_key=")
    Call<CurrencySymbolsModel> fetchSymbols() ;

}

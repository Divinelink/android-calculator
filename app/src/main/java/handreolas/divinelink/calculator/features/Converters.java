package handreolas.divinelink.calculator.features;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import androidx.room.TypeConverter;
import handreolas.divinelink.calculator.currency.CurrencySymbolsModel;

public class Converters {

    @TypeConverter
    public String fromSymbolsList(HashMap<String, String> symbols) {
        if (symbols == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.toJson(symbols, type);
    }

    @TypeConverter
    public HashMap<String, String> toSymbolsList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.fromJson(optionValuesString, type);
    }

    @TypeConverter
    public String fromRatesList(HashMap<String, Double> rates) {
        if (rates == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Double>>() {
        }.getType();
        return gson.toJson(rates, type);
    }

    @TypeConverter
    public HashMap<String, Double> toRatesList(String rate) {
        if (rate == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Double>>() {
        }.getType();
        return gson.fromJson(rate, type);
    }

}

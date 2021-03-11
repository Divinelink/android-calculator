package handreolas.divinelink.calculator.currency;

import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Rates")
public class CurrencyRateModel {

    @PrimaryKey
    @NonNull
    private String base;
    private HashMap<String, Double> rates;


    public CurrencyRateModel(String base, HashMap<String, Double> rates) {
        this.base = base;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public HashMap<String, Double> getRates() {
        return rates;
    }

    public void setRates(HashMap<String, Double> rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "CurrencyRateModel{" +
                "base='" + base + '\'' +
                ", rates=" + rates +
                '}';
    }
}

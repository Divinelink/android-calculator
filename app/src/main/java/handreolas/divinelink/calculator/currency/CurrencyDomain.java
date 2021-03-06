package handreolas.divinelink.calculator.currency;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Currencies")
public class CurrencyDomain {


    @PrimaryKey
    @NonNull
    private final String symbol;

    private String description;

    private Double rate;


    public CurrencyDomain(@NonNull String symbol, String description, Double rate) {
        this.symbol = symbol;
        this.description = description;
        this.rate = rate;
    }

    @Ignore
    public CurrencyDomain(@NonNull String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

    @Ignore
    public CurrencyDomain(@NonNull String symbol, Double rate) {
        this.symbol = symbol;
        this.rate = rate;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @NonNull
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        symbol = symbol;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    @Override
    public String toString() {
        return "SymbolsDomain{" +
                "symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

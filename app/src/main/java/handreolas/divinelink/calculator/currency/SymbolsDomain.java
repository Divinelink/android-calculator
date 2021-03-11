package handreolas.divinelink.calculator.currency;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Symbols")
public class SymbolsDomain {

    @PrimaryKey
    @NonNull
    private final String symbol;

    private final String description;


    public SymbolsDomain(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

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

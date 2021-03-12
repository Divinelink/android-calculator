package handreolas.divinelink.calculator.currency;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void insertCurrencyDomain(CurrencyDomain currencyDomain);

    @Query("SELECT * FROM Currencies")
    public abstract List<CurrencyDomain> getCurrencySymbols();

    @Query("SELECT rate FROM Currencies WHERE symbol == :symbol")
    public abstract Double getRateForCurrency(String symbol);

    @Query("UPDATE Currencies SET rate = :rate WHERE symbol == :symbol")
    public abstract void updateRate(String symbol, Double rate);

    @Query("UPDATE Currencies SET description = :description WHERE symbol == :symbol")
    public abstract void updateSymbol(String symbol, String description);


}

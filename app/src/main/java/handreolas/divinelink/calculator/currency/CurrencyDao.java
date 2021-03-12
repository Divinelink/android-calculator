package handreolas.divinelink.calculator.currency;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void insertCurrencySymbols(SymbolsDomain symbolsDomain);

    @Query("SELECT * FROM Symbols")
    public abstract List<SymbolsDomain> getCurrencySymbols();

    @Query("SELECT rate FROM Symbols WHERE symbol == :symbol")
    public abstract Double getRateForCurrency(String symbol);

    @Query("UPDATE Symbols SET rate = :rate WHERE symbol == :symbol")
    public abstract void updateRate(String symbol, Double rate);


}

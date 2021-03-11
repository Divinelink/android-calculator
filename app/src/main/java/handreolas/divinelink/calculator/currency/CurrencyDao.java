package handreolas.divinelink.calculator.currency;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void insertCurrencySymbols(SymbolsDomain symbolsDomain);

    @Query("SELECT * FROM Symbols")
    abstract List<SymbolsDomain> getCurrencySymbols();

}

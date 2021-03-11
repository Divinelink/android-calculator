package handreolas.divinelink.calculator.base;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import handreolas.divinelink.calculator.calculator.CalculatorDao;
import handreolas.divinelink.calculator.calculator.CalculatorDomain;
import handreolas.divinelink.calculator.currency.CurrencyDao;
import handreolas.divinelink.calculator.currency.CurrencyRateModel;
import handreolas.divinelink.calculator.currency.CurrencySymbolsModel;
import handreolas.divinelink.calculator.currency.SymbolsDomain;
import handreolas.divinelink.calculator.features.Converters;

@Database(entities = {CalculatorDomain.class, CurrencyRateModel.class, SymbolsDomain.class}, version = 8, exportSchema = false)
@TypeConverters(Converters.class)
abstract public class HomeDatabase extends RoomDatabase {

    public abstract CalculatorDao calculatorDao();

    public abstract CurrencyDao currencyDao();


    static private HomeDatabase INSTANCE;

    public static HomeDatabase getDatabase(Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(),
                    HomeDatabase.class, "Home_Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;

    }
}

package handreolas.divinelink.calculator.base;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import handreolas.divinelink.calculator.calculator.CalculatorDao;
import handreolas.divinelink.calculator.calculator.CalculatorDomain;

@Database(entities = {CalculatorDomain.class}, version = 2, exportSchema = false)
abstract public class HomeDatabase extends RoomDatabase {

    public abstract CalculatorDao calculatorDao();



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

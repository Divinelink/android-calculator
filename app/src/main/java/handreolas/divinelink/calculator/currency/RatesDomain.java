package handreolas.divinelink.calculator.currency;

import androidx.room.Entity;

@Entity (tableName = "Rates")
public class RatesDomain {


    private String symbol;
    private double rate;

}

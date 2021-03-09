package handreolas.divinelink.calculator.calculator;

import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class CalculatorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertCalculation(CalculatorDomain formattedDomain);

    @Query("SELECT numberA From Calculations")
    public abstract String getFirstNumber();

    @Query("SELECT numberB From Calculations")
    public abstract String getSecondNumber();

    @Query("SELECT result From Calculations")
    public abstract String getResult();


    @Query("SELECT operation From Calculations")
    public abstract String getOperator();

    @Query("DELETE FROM Calculations")
    abstract void deleteAll();

    @Query("UPDATE Calculations SET numberA = :firstNumber")
    abstract void updateFirstNumber(String firstNumber);

    @Query("UPDATE Calculations SET numberB = :secondNumber")
    abstract void updateSecondNumber(String secondNumber);

    @Query("UPDATE Calculations SET result = :result")
    abstract void updateResult(String result);


    @Query("UPDATE Calculations SET operation = :operand")
    abstract void updateOperation(String operand);

    @Transaction
    public void clearEntries() {
        deleteAll();
        insertCalculation(
                new CalculatorDomain(null, null, null, null));
    }

    @Transaction
    public void updateCalculator(CalculatorDomain formattedDomain) {
        deleteAll();
        insertCalculation(formattedDomain);
    }

}

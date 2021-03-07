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
    abstract void insertCalculation(CalculatorDomain calculatorDomain, CalculatorDomain formattedDomain);

    @Query("SELECT numberA From Calculations WHERE id = 0")
    public abstract String getFirstNumber();

    @Query("SELECT numberB From Calculations WHERE id = 0")
    public abstract String getSecondNumber();

    @Query("SELECT result From Calculations WHERE id = 0")
    public abstract String getResult();

    @Query("SELECT numberA From Calculations WHERE id = 1")
    public abstract String getFormattedFirstNumber();

    @Query("SELECT numberB From Calculations WHERE id = 1")
    public abstract String getFormattedSecondNumber();

    @Query("SELECT result From Calculations WHERE id = 1")
    public abstract String getFormattedResult();


    @Query("SELECT operation From Calculations")
    public abstract String getOperator();

    @Query("DELETE FROM Calculations")
    abstract void deleteAll();


    @Query("UPDATE Calculations SET numberA = :firstNumber WHERE id = 0")
    abstract void updateFirstNumber(String firstNumber);

    @Query("UPDATE Calculations SET numberB = :secondNumber  WHERE id = 0")
    abstract void updateSecondNumber(String secondNumber);

    @Query("UPDATE Calculations SET result = :result WHERE  id = 0")
    abstract void updateResult(String result);


    @Query("UPDATE Calculations SET numberA = :firstNumber WHERE id = 1")
    abstract void updateFormattedFirstNumber(String firstNumber);

    @Query("UPDATE Calculations SET numberB = :secondNumber  WHERE id = 1")
    abstract void updateFormattedSecondNumber(String secondNumber);

    @Query("UPDATE Calculations SET result = :result WHERE  id = 1")
    abstract void updateFormattedResult(String result);


    @Query("UPDATE Calculations SET operation = :operand")
    abstract void updateOperation(String operand);

    @Transaction
    public void clearEntries() {
        deleteAll();
        insertCalculation(
                new CalculatorDomain(0, null, null, null, null), // Non Formatted
                new CalculatorDomain(1, null, null, null, null));  // Formatted
    }

    @Transaction
    public void updateCalculator(CalculatorDomain calculatorDomain, CalculatorDomain formattedDomain) {
        deleteAll();
        insertCalculation(calculatorDomain, formattedDomain);
    }


}

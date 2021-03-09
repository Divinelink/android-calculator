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

    @Query("SELECT * FROM CALCULATIONS")
    public abstract CalculatorDomain getCalculatorDomain();

    @Query("SELECT * FROM CALCULATIONS WHERE id = 1")
    public abstract CalculatorDomain getSavedCalculatorDomain();

    @Query("SELECT numberA From Calculations WHERE id = 1 ")
    public abstract String getSavedFirstNumber();

    @Query("SELECT numberB From Calculations WHERE id = 1")
    public abstract String getSavedSecondNumber();

    @Query("SELECT operation From Calculations WHERE id = 1")
    public abstract String getSavedOperand();

    @Query("SELECT result From Calculations WHERE id = 1")
    public abstract String getSavedResult();

    @Query("UPDATE Calculations SET numberA = :firstNumber WHERE id = 1")
    abstract void updateSavedFirstNumber(String firstNumber);

    @Query("UPDATE Calculations SET numberB = :secondNumber WHERE id = 1")
    abstract void updateSavedSecondNumber(String secondNumber);

    @Query("UPDATE Calculations SET operation = :operand WHERE id = 1")
    abstract void updateSavedOperation(String operand);



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

    @Query("UPDATE Calculations SET numberA = :firstNumber WHERE id = 0")
    abstract void updateFirstNumber(String firstNumber);

    @Query("UPDATE Calculations SET numberB = :secondNumber WHERE id = 0")
    abstract void updateSecondNumber(String secondNumber);

    @Query("UPDATE Calculations SET result = :result WHERE id = 0")
    abstract void updateResult(String result);

    @Query("UPDATE Calculations SET operation = :operand WHERE id = 0")
    abstract void updateOperation(String operand);

    @Transaction
    public void clearEntries() {
        deleteAll();
        insertCalculation(new CalculatorDomain(0,null, null, null, null));
        insertCalculation(new CalculatorDomain(1,null, null, null, null));

    }

    @Transaction
    public void updateCalculator(CalculatorDomain formattedDomain) {
        deleteAll();
        insertCalculation(formattedDomain);
    }

}

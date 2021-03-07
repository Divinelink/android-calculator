package handreolas.divinelink.calculator.calculator;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Calculations")
public class CalculatorDomain {

    @PrimaryKey (autoGenerate = false)
    private int id;

    private String numberA;
    private String numberB;
    private String result;
    private String operation;

    @Ignore
    public CalculatorDomain() {
    }

    public CalculatorDomain(int id, String numberA, String numberB, String result, String operation) {
        this.id = id;
        this.numberA = numberA;
        this.numberB = numberB;
        this.result = result;
        this.operation = operation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberA() {
        return numberA;
    }

    public void setNumberA(String numberA) {
        this.numberA = numberA;
    }

    public String getNumberB() {
        return numberB;
    }

    public void setNumberB(String numberB) {
        this.numberB = numberB;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}

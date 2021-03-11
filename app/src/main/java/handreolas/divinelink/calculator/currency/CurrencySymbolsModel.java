package handreolas.divinelink.calculator.currency;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class CurrencySymbolsModel {


    private boolean success;

    private Map<String, String> symbols;


    public CurrencySymbolsModel(boolean success, Map<String, String> symbols) {
        this.success = success;
        this.symbols = symbols;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, String> getSymbols() {
        return symbols;
    }

    public void setSymbols(HashMap<String, String> symbols) {
        this.symbols = symbols;
    }
}

package handreolas.divinelink.calculator.features;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

public class SharedPreferenceManager {

    private static final String FIRST_SYMBOL = "FirstCurrencySymbol";
    private static final String SECOND_SYMBOL = "SecondCurrencySymbol";
    private static final String THIRD_SYMBOL = "ThirdCurrencySymbol";

    private static final String FIRST_NAME = "FirstCurrencyName";
    private static final String SECOND_NAME = "SecondCurrencyName";
    private static final String THIRD_NAME = "ThirdCurrencyName";

    private static final String SELECTED_POSITION = "SelectedPosition";


    public String getSavedCurrencySymbol(int position, Context ctx) {
        String KEY = null;
        String DEFAULT_VALUE = null;
        switch (position) {
            case 0:
                KEY = FIRST_SYMBOL;
                DEFAULT_VALUE = "EUR";
                break;
            case 1:
                KEY = SECOND_SYMBOL;
                DEFAULT_VALUE = "USD";
                break;
            case 2:
                KEY = THIRD_SYMBOL;
                DEFAULT_VALUE = "GBP";
                break;

        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(KEY, DEFAULT_VALUE);
    }

    public String getSavedCurrencyName(int position, Context ctx) {
        String KEY = null;
        String DEFAULT_VALUE = null;
        switch (position) {
            case 0:
                KEY = FIRST_NAME;
                DEFAULT_VALUE = "Euro";
                break;
            case 1:
                KEY = SECOND_NAME;
                DEFAULT_VALUE = "United States dollar";
                break;
            case 2:
                KEY = THIRD_NAME;
                DEFAULT_VALUE = "British pound";
                break;

        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(KEY, DEFAULT_VALUE);
    }

    public int getSelectedPosition(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getInt(SELECTED_POSITION, 0);
    }

    public void saveSelectedPosition(int position, Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SELECTED_POSITION, position);
        editor.apply();
    }

    public void saveCurrencySymbol(String symbol, int position, Context ctx) {

        String KEY = null;
        switch (position) {
            case 0:
                KEY = FIRST_SYMBOL;
                break;
            case 1:
                KEY = SECOND_SYMBOL;
                break;
            case 2:
                KEY = THIRD_SYMBOL;
                break;

        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY, symbol);
        editor.apply();
    }

    public void saveCurrencyName(String name, int position, Context ctx) {

        String KEY = null;
        switch (position) {
            case 0:
                KEY = FIRST_NAME;
                break;
            case 1:
                KEY = SECOND_NAME;
                break;
            case 2:
                KEY = THIRD_NAME;
                break;

        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY, name);
        editor.apply();
    }
}

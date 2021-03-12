package handreolas.divinelink.calculator.currency;

import java.util.ArrayList;
import java.util.Date;

public interface ICurrencyView {

    void showSymbols(String a, String b, String c);

    void showCurrencyList(ArrayList<SymbolsDomain> currencySymbols, int position);

    void updateTime(String date);

    void updateTimeBeforeCall(String updating);

    void updateCurrencyRates(Double r1, Double r2, Double r3);

}

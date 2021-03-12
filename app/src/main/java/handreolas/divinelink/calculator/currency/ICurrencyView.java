package handreolas.divinelink.calculator.currency;

import java.util.ArrayList;

public interface ICurrencyView {

    void updateTime(String date);

    void updateTimeBeforeCall(String updating);

    void updateCurrencyRates(String r1, String r2, String r3);

    void addCommaOnCurrentRate(String r1, int position);

    void addCurrencySelectorFragment(int position);

    void onError(int errorCode);

}

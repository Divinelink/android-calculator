package handreolas.divinelink.calculator.currency;

import java.util.ArrayList;

public interface ICurrencyView {

    void showSymbols(String a, String b, String c);

    void showCurrencyList(ArrayList<SymbolsDomain> currencySymbols, int position);

}

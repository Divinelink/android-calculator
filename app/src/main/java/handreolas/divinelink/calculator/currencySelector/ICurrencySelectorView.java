package handreolas.divinelink.calculator.currencySelector;

import java.util.ArrayList;

import handreolas.divinelink.calculator.currency.SymbolsDomain;

public interface ICurrencySelectorView {

    void showCurrencyListOnSelector(ArrayList<SymbolsDomain> currencySymbols, int position);

}

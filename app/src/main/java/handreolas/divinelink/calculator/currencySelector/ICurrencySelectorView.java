package handreolas.divinelink.calculator.currencySelector;

import java.util.ArrayList;

import handreolas.divinelink.calculator.currency.CurrencyDomain;

public interface ICurrencySelectorView {

    void showCurrencyListOnSelector(ArrayList<CurrencyDomain> currencySymbols, int position);

}

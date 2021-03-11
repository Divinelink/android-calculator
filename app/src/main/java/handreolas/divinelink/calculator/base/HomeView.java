package handreolas.divinelink.calculator.base;

import java.io.Serializable;

public interface HomeView extends Serializable {

    void addCalculatorFragment();

    void addCurrencyFragment();

    void addCurrencySelectorFragment(int position);
}

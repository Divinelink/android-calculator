package handreolas.divinelink.calculator.calculator;

public interface ICalculatorView {

    void showResult(String result);

    void showResultOnResultTV(String result);

    void onClearTextViews();

    void resultOnButtonPress(String result);
}

package handreolas.divinelink.calculator.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import handreolas.divinelink.calculator.R;
import handreolas.divinelink.calculator.calculator.CalculatorFragment;

import android.os.Bundle;

public class HomeActivity extends AppCompatActivity implements HomeView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeRoot, CalculatorFragment.newInstance())

                .commit();
    }

    @Override
    public void addCalculatorFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeRoot, CalculatorFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addCurrencyFragment() {

    }
}
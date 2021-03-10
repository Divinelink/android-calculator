package handreolas.divinelink.calculator.base;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import handreolas.divinelink.calculator.R;
import handreolas.divinelink.calculator.calculator.CalculatorFragment;
import handreolas.divinelink.calculator.currency.CurrencyFragment;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    private final int[] tabIcons = {
            R.drawable.ic_calculator,
            R.drawable.ic_currency
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mPager = (ViewPager) findViewById(R.id.homeRoot);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        setupViewPager(mPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
        setupTabIcons();


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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeRoot, CurrencyFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ScreenSlidePagerAdapter adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CurrencyFragment(), "Currency");
        adapter.addFragment(new CalculatorFragment(), "Calculator");
        viewPager.setAdapter(adapter);
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 1)
                return new CalculatorFragment();
            else
                return new CurrencyFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



}
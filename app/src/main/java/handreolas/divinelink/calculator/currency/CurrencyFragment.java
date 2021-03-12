package handreolas.divinelink.calculator.currency;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import handreolas.divinelink.calculator.R;
import handreolas.divinelink.calculator.currencySelector.CurrencySelectorFragment;
import handreolas.divinelink.calculator.features.SharedPreferenceManager;

public class CurrencyFragment extends Fragment implements ICurrencyView {


    private ICurrencyPresenter presenter;

    private Button mOne, mTwo, mThree, mFour, mFive, mSix, mSeven, mEight, mNine, mZero, mComma;
    private Button mBackspace, mDelete;


    private TextView listFirstTV, listSecondTV, listThirdTV, valueFirstTV, valueSecondTV, valueThirdTV, infoFirstTV, infoSecondTV, infoThirdTV, exchangeRatesTV;

    private final SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager();
    private int positionOfSelectedCurrency;


    public CurrencyFragment() {
        // Required empty public constructor
    }


    public static CurrencyFragment newInstance() {
        CurrencyFragment fragment = new CurrencyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_currency, container, false);


        mOne = v.findViewById(R.id.buttonOne);
        mTwo = v.findViewById(R.id.buttonTwo);
        mThree = v.findViewById(R.id.buttonThree);
        mFour = v.findViewById(R.id.buttonFour);
        mFive = v.findViewById(R.id.buttonFive);
        mSix = v.findViewById(R.id.buttonSix);
        mSeven = v.findViewById(R.id.buttonSeven);
        mEight = v.findViewById(R.id.buttonEight);
        mNine = v.findViewById(R.id.buttonNine);
        mZero = v.findViewById(R.id.buttonZero);
        mComma = v.findViewById(R.id.buttonComma);

        mDelete = v.findViewById(R.id.buttonAC);
        mBackspace = v.findViewById(R.id.buttonBackspace);

        listFirstTV = v.findViewById(R.id.currencyListButtonFirst);
        listSecondTV = v.findViewById(R.id.currencyListButtonSecond);
        listThirdTV = v.findViewById(R.id.currencyListButtonThird);
        valueFirstTV = v.findViewById(R.id.currencyValueTVFirst);
        valueSecondTV = v.findViewById(R.id.currencyValueTVSecond);
        valueThirdTV = v.findViewById(R.id.currencyValueTVThird);
        infoFirstTV = v.findViewById(R.id.currencyInfoTVFirst);
        infoSecondTV = v.findViewById(R.id.currencyInfoTVSecond);
        infoThirdTV = v.findViewById(R.id.currencyInfoTVThird);

        exchangeRatesTV = v.findViewById(R.id.exchange_rates_TV);


        listFirstTV.setText(sharedPreferenceManager.getSavedCurrencySymbol(0, getContext()));
        listSecondTV.setText(sharedPreferenceManager.getSavedCurrencySymbol(1, getContext()));
        listThirdTV.setText(sharedPreferenceManager.getSavedCurrencySymbol(2, getContext()));

        infoFirstTV.setText(sharedPreferenceManager.getSavedCurrencyName(0, getContext()));
        infoSecondTV.setText(sharedPreferenceManager.getSavedCurrencyName(1, getContext()));
        infoThirdTV.setText(sharedPreferenceManager.getSavedCurrencyName(2, getContext()));


        mOne.setOnClickListener(this::onClick);
        mTwo.setOnClickListener(this::onClick);
        mThree.setOnClickListener(this::onClick);
        mFour.setOnClickListener(this::onClick);
        mFive.setOnClickListener(this::onClick);
        mSix.setOnClickListener(this::onClick);
        mSeven.setOnClickListener(this::onClick);
        mEight.setOnClickListener(this::onClick);
        mNine.setOnClickListener(this::onClick);
        mZero.setOnClickListener(this::onClick);
        mDelete.setOnClickListener(this::onClick);
        mComma.setOnClickListener(this::onClick);
        mBackspace.setOnClickListener(this::onClick);

        listFirstTV.setOnClickListener(this::onClick);
        listSecondTV.setOnClickListener(this::onClick);
        listThirdTV.setOnClickListener(this::onClick);

        valueFirstTV.setOnClickListener(this::onClick);
        valueSecondTV.setOnClickListener(this::onClick);
        valueThirdTV.setOnClickListener(this::onClick);

        presenter = new CurrencyPresenterImpl(this);

        mDelete.setOnTouchListener(touchListener);
        mBackspace.setOnTouchListener(touchListener);



        getFocusedCurrency();

        presenter.getCurrencySymbols(getContext());
        presenter.getCurrencyRates(getContext(), true);
        presenter.calculateRates(getValueOfFocusedCurrency(), getContext());

        return v;
    }


    public void onClick(View v) {
        if (v.getId() == R.id.currencyListButtonFirst) {
            presenter.getCurrencySelectorFragment(getContext(), 0);
//            addCurrencySelectorFragment(0);
        } else if (v.getId() == R.id.currencyListButtonSecond) {
            presenter.getCurrencySelectorFragment(getContext(), 1);
//            addCurrencySelectorFragment(1);
        } else if (v.getId() == R.id.currencyListButtonThird) {
            presenter.getCurrencySelectorFragment(getContext(), 2);
//            addCurrencySelectorFragment(2);
        } else if (v.getId() == R.id.currencyValueTVFirst) {
            sharedPreferenceManager.saveSelectedPosition(0, getContext());
            valueFirstTV.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVariant));
            valueSecondTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            valueThirdTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            presenter.calculateRates("1", getContext());
        } else if (v.getId() == R.id.currencyValueTVSecond) {
            sharedPreferenceManager.saveSelectedPosition(1, getContext());
            valueFirstTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            valueSecondTV.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVariant));
            valueThirdTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            presenter.calculateRates("1", getContext());
        } else if (v.getId() == R.id.currencyValueTVThird) {
            sharedPreferenceManager.saveSelectedPosition(2, getContext());
            valueFirstTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            valueSecondTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            valueThirdTV.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVariant));
            presenter.calculateRates("1", getContext());
        } else if (v.getId() == R.id.buttonOne) {
            presenter.insertNumber(getValueOfFocusedCurrency(), "1", getContext());
        } else if (v.getId() == R.id.buttonTwo) {
            presenter.insertNumber(getValueOfFocusedCurrency(), "2", getContext());
        } else if (v.getId() == R.id.buttonThree) {
            presenter.insertNumber(getValueOfFocusedCurrency(), "3", getContext());
        } else if (v.getId() == R.id.buttonFour) {
            presenter.insertNumber(getValueOfFocusedCurrency(), "4", getContext());
        } else if (v.getId() == R.id.buttonFive) {
            presenter.insertNumber(getValueOfFocusedCurrency(), "5", getContext());
        } else if (v.getId() == R.id.buttonSix) {
            presenter.insertNumber(getValueOfFocusedCurrency(), "6", getContext());
        } else if (v.getId() == R.id.buttonSeven) {
            presenter.insertNumber(getValueOfFocusedCurrency(), "7", getContext());
        } else if (v.getId() == R.id.buttonEight) {
            presenter.insertNumber(getValueOfFocusedCurrency(), "8", getContext());
        } else if (v.getId() == R.id.buttonNine) {
            presenter.insertNumber(getValueOfFocusedCurrency(), "9", getContext());
        } else if (v.getId() == R.id.buttonZero) {
            presenter.insertNumber(getValueOfFocusedCurrency(), "0", getContext());
        } else if (v.getId() == R.id.buttonComma) {
            presenter.insertComma(getContext(), getValueOfFocusedCurrency());
        } else if (v.getId() == R.id.buttonBackspace) {
            presenter.backspace(getContext(), getValueOfFocusedCurrency());
        } else if (v.getId() == R.id.buttonAC) {
            presenter.clearValues(getContext());
        }
    }


    private void getFocusedCurrency() {
        positionOfSelectedCurrency = sharedPreferenceManager.getSelectedPosition(getContext());
        if (positionOfSelectedCurrency == 0) {
            valueFirstTV.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVariant));
            valueSecondTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            valueThirdTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        } else if (positionOfSelectedCurrency == 1) {
            valueFirstTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            valueSecondTV.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVariant));
            valueThirdTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        } else {
            valueFirstTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            valueSecondTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            valueThirdTV.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVariant));
        }
    }

    private String getValueOfFocusedCurrency() {
        positionOfSelectedCurrency = sharedPreferenceManager.getSelectedPosition(getContext());
        String value;
        if (positionOfSelectedCurrency == 0)
            value = valueFirstTV.getText().toString();
        else if (positionOfSelectedCurrency == 1)
            value = valueSecondTV.getText().toString();
        else
            value = valueThirdTV.getText().toString();

        if (value.equals(""))
            return "1";
        else return value;
    }


    @Override
    public void addCurrencySelectorFragment(int position) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_currency, CurrencySelectorFragment.newInstance(position))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void updateTime(String date) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> exchangeRatesTV.setText(String.format("%s\n(%s)", getResources().getString(R.string.exchange_rates_text_short), date), TextView.BufferType.SPANNABLE));
        }
    }

    @Override
    public void updateTimeBeforeCall(String updating) {

        String text = getResources().getString(R.string.exchange_rates_text_short) + "\n" + updating;
        Spannable spannable = new SpannableString(text);

        spannable.setSpan(new ForegroundColorSpan(Color.RED),
                getResources().getString(R.string.exchange_rates_text_short).length(),
                text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exchangeRatesTV.setText(spannable, TextView.BufferType.SPANNABLE);
        timerHandler.postDelayed(failedToGetRatesRunnable, 0);
    }

    Handler timerHandler = new Handler();
    Runnable failedToGetRatesRunnable = new Runnable() {
        @Override
        public void run() {
            if (checkNetworkStatus(getContext()).equals("WiFi") || checkNetworkStatus(getContext()).equals("MobileData")) {
                presenter.getCurrencyRates(getContext(), true);
                presenter.getCurrencySymbols(getContext());
                timerHandler.removeCallbacks(failedToGetRatesRunnable);
            } else {
                timerHandler.postDelayed(this, 5000);
            }
        }
    };

    public static String checkNetworkStatus(final Context context) {
        String networkStatus = "";
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo WiFi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (WiFi.isConnected()) {
            networkStatus = "WiFi";
        } else if (mobile.isConnected()) {
            networkStatus = "MobileData";
        } else {
            networkStatus = "noNetwork";
        }
        return networkStatus;
    }


    @Override
    public void updateCurrencyRates(String r1, String r2, String r3) {
        valueFirstTV.setText(r1);
        valueSecondTV.setText(r2);
        valueThirdTV.setText(r3);

        timerHandler.removeCallbacks(failedToGetRatesRunnable);
    }

    @Override
    public void addCommaOnCurrentRate(String r1, int position) {
        if (position == 0) valueFirstTV.setText(r1);
        else if (position == 1) valueSecondTV.setText(r1);
        else valueThirdTV.setText(r1);
    }

    @Override
    public void onError(int errorCode) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (errorCode == 1)
                    Toast.makeText(getContext(), "Cannot get currency symbols.\nConnect to a network", Toast.LENGTH_LONG).show();
                else if (errorCode == 2)
                    Toast.makeText(getContext(), "Connect to a network to get rates", Toast.LENGTH_LONG).show();
            });
        }
    }

    View.OnTouchListener touchListener = this::onTouch;
    private boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            AnimatorSet reducer = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.reduce_size);
            reducer.setTarget(view);
            reducer.start();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            AnimatorSet regainer = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.regain_size);
            regainer.setTarget(view);
            regainer.start();
        }
        return false;
    }
}
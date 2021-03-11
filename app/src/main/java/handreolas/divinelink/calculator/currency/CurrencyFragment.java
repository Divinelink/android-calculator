package handreolas.divinelink.calculator.currency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import handreolas.divinelink.calculator.R;
import handreolas.divinelink.calculator.base.HomeView;
import handreolas.divinelink.calculator.currencySelector.CurrencySelectorFragment;
import handreolas.divinelink.calculator.features.SharedPreferenceManager;

public class CurrencyFragment extends Fragment implements ICurrencyView {


    private ICurrencyPresenter presenter;

    private Button mOne, mTwo, mThree, mFour, mFive, mSix, mSeven, mEight, mNine, mZero, mComma;
    private Button mBackspace, mDelete;

    private CardView mCurrencySelectorView;

    private TextView listFirstTV, listSecondTV, listThirdTV, valueFirstTV, valueSecondTV, valueThirdTV, infoFirstTV, infoSecondTV, infoThirdTV;

    private SharedPreferenceManager sharedPreferenceManager;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_currency, container, false);

        sharedPreferenceManager = new SharedPreferenceManager();

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

//        mCurrencySelectorView = v.findViewById(R.id.currencySelectorRoot);
//        mCurrencySelectorView.setVisibility(View.GONE);

        listFirstTV.setText(sharedPreferenceManager.getSavedCurrencySymbol(0, getContext()));
        listSecondTV.setText(sharedPreferenceManager.getSavedCurrencySymbol(1, getContext()));
        listThirdTV.setText(sharedPreferenceManager.getSavedCurrencySymbol(2, getContext()));

        infoFirstTV.setText(sharedPreferenceManager.getSavedCurrencyName(0, getContext()));
        infoSecondTV.setText(sharedPreferenceManager.getSavedCurrencyName(1, getContext()));
        infoThirdTV.setText(sharedPreferenceManager.getSavedCurrencyName(2, getContext()));


//        mOne.setOnClickListener(this::onClick);
//        mTwo.setOnClickListener(this::onClick);
//        mThree.setOnClickListener(this::onClick);
//        mFour.setOnClickListener(this::onClick);
//        mFive.setOnClickListener(this::onClick);
//        mSix.setOnClickListener(this::onClick);
//        mSeven.setOnClickListener(this::onClick);
//        mEight.setOnClickListener(this::onClick);
//        mNine.setOnClickListener(this::onClick);
//        mZero.setOnClickListener(this::onClick);
//        mDelete.setOnClickListener(this::onClick);
//
//        mComma.setOnClickListener(this::onClick);

//        mBackspace.setOnClickListener(this::onClick);

        listFirstTV.setOnClickListener(this::onClick);
        listSecondTV.setOnClickListener(this::onClick);
        listThirdTV.setOnClickListener(this::onClick);


        mZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getCurrencyRatios(getContext());
            }
        });


        presenter = new CurrencyPresenterImpl(this);

        presenter.getCurrencyRatios(getContext());

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void onClick(View v) {
        // TODO Is this efficient?

        if (v.getId() == R.id.currencyListButtonFirst) {
//            presenter.getCurrencyList(0, getContext());
            addCurrencySelectorFragment(0);
        } else if (v.getId() == R.id.currencyListButtonSecond) {
//            presenter.getCurrencyList(1, getContext());
            addCurrencySelectorFragment(1);
        } else if (v.getId() == R.id.currencyListButtonThird) {
//            presenter.getCurrencyList(2, getContext());
            addCurrencySelectorFragment(2);
        }

//        if (v.getId() == R.id.buttonOne) {
//            presenter.setNumber(getContext(), "1");
//        } else if (v.getId() == R.id.buttonTwo) {
//            presenter.setNumber(getContext(), "2");
//        } else if (v.getId() == R.id.buttonThree) {
//            presenter.setNumber(getContext(), "3");
//        } else if (v.getId() == R.id.buttonFour) {
//            presenter.setNumber(getContext(), "4");
//        } else if (v.getId() == R.id.buttonFive) {
//            presenter.setNumber(getContext(), "5");
//        } else if (v.getId() == R.id.buttonSix) {
//            presenter.setNumber(getContext(), "6");
//        } else if (v.getId() == R.id.buttonSeven) {
//            presenter.setNumber(getContext(), "7");
//        } else if (v.getId() == R.id.buttonEight) {
//            presenter.setNumber(getContext(), "8");
//        } else if (v.getId() == R.id.buttonNine) {
//            presenter.setNumber(getContext(), "9");
//        } else if (v.getId() == R.id.buttonZero) {
//            presenter.setNumber(getContext(), "0");
//        } else if (v.getId() == R.id.buttonAC) {
//            presenter.clearNumber(getContext());
//        } else if (v.getId() == R.id.buttonAddition) {
//            presenter.setOperand(getContext(), "+");
//        } else if (v.getId() == R.id.buttonSubtraction) {
//            presenter.setOperand(getContext(), "-");
//        } else if (v.getId() == R.id.buttonMultiplication) {
//            presenter.setOperand(getContext(), "×");
//        } else if (v.getId() == R.id.buttonDivision) {
//            presenter.setOperand(getContext(), "÷");
//        } else if (v.getId() == R.id.buttonComma) {
//            presenter.setComma(getContext());
//        } else if (v.getId() == R.id.buttonBackspace) {
//            presenter.backspace(getContext());
//        } else if (v.getId() == R.id.buttonResult) {
//            presenter.result(getContext());
//        } else if (v.getId() == R.id.buttonPercent) {
//            presenter.percentage(getContext());
//        }

    }

    @Override
    public void showSymbols(String a, String b, String c) {

//        infoFirstTV.setText(a);
//        infoSecondTV.setText(b);
//        infoThirdTV.setText(c);

    }

    @Override
    public void showCurrencyList(ArrayList<SymbolsDomain> currencySymbols, int position) {


        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
//                CurrencyRvAdapter currencyRvAdapter = new CurrencyRvAdapter(currencySymbols, getContext());

                @Override
                public void run() {


                }
            });
        }

    }



    public void addCurrencySelectorFragment(int position) {

//        mCurrencySelectorView.setVisibility(View.VISIBLE);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_currency, CurrencySelectorFragment.newInstance(position))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }
}
package handreolas.divinelink.calculator.currency;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import handreolas.divinelink.calculator.R;

public class CurrencyFragment extends Fragment implements ICurrencyView {


    private ICurrencyPresenter presenter;

    private Button mOne, mTwo, mThree, mFour, mFive, mSix, mSeven, mEight, mNine, mZero, mComma;
    private Button mBackspace, mDelete;

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


        presenter = new CurrencyPresenterImpl(this);

        presenter.getCurrencyRatios(getContext());

        return v;
    }
}
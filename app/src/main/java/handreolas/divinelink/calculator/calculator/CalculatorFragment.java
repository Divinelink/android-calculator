package handreolas.divinelink.calculator.calculator;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import handreolas.divinelink.calculator.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculatorFragment extends Fragment implements ICalculatorView {

    private ICalculatorPresenter presenter;

    private Button mOne, mTwo, mThree, mFour, mFive, mSix, mSeven, mEight, mNine, mZero, mDelete, mComma;
    private Button mDivision, mMultiplication, mSubtraction, mAddition, mResult, mPercentage;
    private ImageButton mBackspace;

    private TextView mCalculationTV, mResultTV;
    private Guideline mGuideLineResult;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment CalculatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculatorFragment newInstance() {
        CalculatorFragment fragment = new CalculatorFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calculator, container, false);

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

        mDivision = v.findViewById(R.id.buttonDivision);
        mMultiplication = v.findViewById(R.id.buttonMultiplication);
        mSubtraction = v.findViewById(R.id.buttonSubtraction);
        mAddition = v.findViewById(R.id.buttonAddition);

        mPercentage = v.findViewById(R.id.buttonPercent);

        mDelete = v.findViewById(R.id.buttonAC);
        mBackspace = v.findViewById(R.id.buttonBackspace);
        mResult = v.findViewById(R.id.buttonResult);

        mCalculationTV = v.findViewById(R.id.calculationTextView);
        mResultTV = v.findViewById(R.id.resultTextView);

        mGuideLineResult = v.findViewById(R.id.horizontalCalculationTVGuideline);


//        GetCalculatorFactory factory = new GetCalculatorFactory();
//        factory.getNumber(5);
//        factory.getOperation(5);


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

        mMultiplication.setOnClickListener(this::onClick);
        mDivision.setOnClickListener(this::onClick);
        mAddition.setOnClickListener(this::onClick);
        mSubtraction.setOnClickListener(this::onClick);
        mDelete.setOnClickListener(this::onClick);

        mBackspace.setOnClickListener(this::onClick);
        mResult.setOnClickListener(this::onClick);
        mPercentage.setOnClickListener(this::onClick);


        presenter = new CalculatorPresenterImpl(this);

        presenter.getNumber(getContext());

        return v;
    }

    public void onClick(View v) {
        // TODO Is this efficient?
        if (v.getId() == R.id.buttonOne) {
            presenter.setNumber(getContext(), "1");
        } else if (v.getId() == R.id.buttonTwo) {
            presenter.setNumber(getContext(), "2");
        } else if (v.getId() == R.id.buttonThree) {
            presenter.setNumber(getContext(), "3");
        } else if (v.getId() == R.id.buttonFour) {
            presenter.setNumber(getContext(), "4");
        } else if (v.getId() == R.id.buttonFive) {
            presenter.setNumber(getContext(), "5");
        } else if (v.getId() == R.id.buttonSix) {
            presenter.setNumber(getContext(), "6");
        } else if (v.getId() == R.id.buttonSeven) {
            presenter.setNumber(getContext(), "7");
        } else if (v.getId() == R.id.buttonEight) {
            presenter.setNumber(getContext(), "8");
        } else if (v.getId() == R.id.buttonNine) {
            presenter.setNumber(getContext(), "9");
        } else if (v.getId() == R.id.buttonZero) {
            presenter.setNumber(getContext(), "0");
        } else if (v.getId() == R.id.buttonAC) {
            presenter.clearNumber(getContext());
        } else if (v.getId() == R.id.buttonAddition) {
            presenter.setOperand(getContext(), "+");
        } else if (v.getId() == R.id.buttonSubtraction) {
            presenter.setOperand(getContext(), "-");
        } else if (v.getId() == R.id.buttonMultiplication) {
            presenter.setOperand(getContext(), "×");
        } else if (v.getId() == R.id.buttonDivision) {
            presenter.setOperand(getContext(), "÷");
        } else if (v.getId() == R.id.buttonComma) {
            presenter.setComma(getContext());
        } else if (v.getId() == R.id.buttonBackspace) {
            presenter.backspace(getContext());
        } else if (v.getId() == R.id.buttonResult) {
            presenter.result(getContext());
        } else if (v.getId() == R.id.buttonPercent) {
            presenter.percentage(getContext());
        }

    }


    @Override
    public void showResult(String result) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCalculationTV.setText(result);
                }
            });
        }
    }

    @Override
    public void showResultOnResultTV(String result) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mResultTV.setText(String.format("= %s", result));
                    mResultTV.setVisibility(View.VISIBLE);

                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mGuideLineResult.getLayoutParams();
                    params.guidePercent = 0.6f; // 45% // range: 0 <-> 1

                    mGuideLineResult.setLayoutParams(params);
                }
            });
        }
    }

    @Override
    public void onClearTextViews() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCalculationTV.setText("0");
                    mResultTV.setVisibility(View.GONE);

                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mGuideLineResult.getLayoutParams();
                    params.guidePercent = 0.8f;

                    mGuideLineResult.setLayoutParams(params);
                }
            });
        }
    }
}
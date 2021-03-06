package handreolas.divinelink.calculator.calculator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import handreolas.divinelink.calculator.R;

import static handreolas.divinelink.calculator.features.CalculatorHelper.ADDITION;
import static handreolas.divinelink.calculator.features.CalculatorHelper.DIVISION;
import static handreolas.divinelink.calculator.features.CalculatorHelper.MULTIPLICATION;
import static handreolas.divinelink.calculator.features.CalculatorHelper.SUBTRACTION;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculatorFragment extends Fragment implements ICalculatorView {

    private ICalculatorPresenter presenter;

    private MaterialButton mOne, mTwo, mThree, mFour, mFive, mSix, mSeven, mEight, mNine, mZero, mDelete, mComma;
    private MaterialButton mDivision, mMultiplication, mSubtraction, mAddition, mResult, mPercentage;
    private ImageButton mBackspace;

    private TextView mCalculationTV, mResultTV;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    public static CalculatorFragment newInstance() {
        CalculatorFragment fragment = new CalculatorFragment();
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
        View v = inflater.inflate(R.layout.fragment_calculator, container, false);
        //TODO division button is not centerd 10/3/21 - 5:44PM
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
            presenter.setOperand(getContext(), ADDITION);
        } else if (v.getId() == R.id.buttonSubtraction) {
            presenter.setOperand(getContext(), SUBTRACTION);
        } else if (v.getId() == R.id.buttonMultiplication) {
            presenter.setOperand(getContext(), MULTIPLICATION);
        } else if (v.getId() == R.id.buttonDivision) {
            presenter.setOperand(getContext(), DIVISION);
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
                    mResultTV.setTextSize(mResultTV.getAutoSizeMaxTextSize());
                    mResultTV.setText(String.format("= %s", result));
                    mResultTV.setVisibility(View.VISIBLE);

                    mResultTV.setTextColor(getContext().getResources().getColor(android.R.color.tab_indicator_text));
                    mCalculationTV.setTextColor(getContext().getResources().getColor(android.R.color.black));

                    mResultTV.setTextSize(getContext().getResources().getDimensionPixelSize(R.dimen.resultTextViewTextSize) / getResources().getDisplayMetrics().scaledDensity);
                    mCalculationTV.setAutoSizeTextTypeUniformWithConfiguration(
                            getContext().getResources().getDimensionPixelOffset(R.dimen.calculationMinTextSize),
                            getContext().getResources().getDimensionPixelOffset(R.dimen.calculationMaxTextSize),
                            getContext().getResources().getDimensionPixelOffset(R.dimen.calculationAutoSizeStepGranularity), 0);


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

                    mResultTV.setTextColor(getContext().getResources().getColor(android.R.color.tab_indicator_text));
                    mCalculationTV.setTextColor(getContext().getResources().getColor(android.R.color.black));

                    mResultTV.setTextSize(getContext().getResources().getDimensionPixelSize(R.dimen.resultTextViewTextSize) / getResources().getDisplayMetrics().scaledDensity);
                    mCalculationTV.setAutoSizeTextTypeUniformWithConfiguration(
                            getContext().getResources().getDimensionPixelOffset(R.dimen.calculationMinTextSize),
                            getContext().getResources().getDimensionPixelOffset(R.dimen.calculationMaxTextSize),
                            getContext().getResources().getDimensionPixelOffset(R.dimen.calculationAutoSizeStepGranularity), 0);
                }
            });
        }
    }

    @Override
    public void resultOnButtonPress(String result) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    final TextView tv = mResultTV;

                    mResultTV.setTextColor(getContext().getResources().getColor(android.R.color.black));
                    mCalculationTV.setTextColor(getContext().getResources().getColor(android.R.color.tab_indicator_text));

                    final float startSize = mResultTV.getTextSize() / getResources().getDisplayMetrics().scaledDensity; // Size in SP
                    final float endSize =  getContext().getResources().getDimensionPixelOffset(R.dimen.resultTextViewEnlarged) / getResources().getDisplayMetrics().scaledDensity;
                    long animationDuration = 200; // Animation duration in ms

                    ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
                    animator.setDuration(animationDuration);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float animatedValue = (float) valueAnimator.getAnimatedValue();
                            tv.setTextSize(animatedValue);
                        }
                    });
                    animator.start();
                }
            });
        }
    }
}
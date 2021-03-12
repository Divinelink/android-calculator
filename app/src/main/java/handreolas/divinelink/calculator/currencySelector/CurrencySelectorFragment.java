package handreolas.divinelink.calculator.currencySelector;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import handreolas.divinelink.calculator.R;
import handreolas.divinelink.calculator.currency.CurrencyFragment;
import handreolas.divinelink.calculator.currency.SymbolsDomain;
import handreolas.divinelink.calculator.features.SharedPreferenceManager;


public class CurrencySelectorFragment extends Fragment implements ICurrencySelectorView {

    private ICurrencySelectorPresenter presenter;
    private RecyclerView mRecyclerView;
    private MaterialButton mCancelButton;
    private SharedPreferenceManager preferenceManager;


    private static final String POSITION = "position";

    private int mPositionOfClickedItem;

    public CurrencySelectorFragment() {
        // Required empty public constructor
    }

    public static CurrencySelectorFragment newInstance(int position) {
        CurrencySelectorFragment fragment = new CurrencySelectorFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPositionOfClickedItem = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_currency_selector, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = v.findViewById(R.id.currenciesSelectorRV);
        mRecyclerView.setLayoutManager(layoutManager);

        mCancelButton = v.findViewById(R.id.currenciesSelectorCancelButton);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_currency, CurrencyFragment.newInstance())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
                getFragmentManager().popBackStack();
            }
        });

        preferenceManager = new SharedPreferenceManager();


        presenter = new CurrencySelectorPresenterImpl(this);
        presenter.getCurrencyList(mPositionOfClickedItem, getActivity());

        return v;
    }


    @Override
    public void showCurrencyListOnSelector(ArrayList<SymbolsDomain> currencySymbols, int position) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                final CurrencySelectorRvAdapter currencySelectorRvAdapter = new CurrencySelectorRvAdapter(currencySymbols, new OnCurrencySelectorClickListener() {
                    @Override
                    public void onCurrencyClickListener(SymbolsDomain selectedCurrency) {
                        // Save Data on Shared Pref
                        preferenceManager.saveCurrencyName(selectedCurrency.getDescription(), position, getActivity());
                        preferenceManager.saveCurrencySymbol(selectedCurrency.getSymbol(), position, getActivity());

                        getFragmentManager().popBackStack();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.fragment_currency, CurrencyFragment.newInstance())
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                                .commit();
                    }
                }, getContext());

                @Override
                public void run() {

                    mRecyclerView.setAdapter(currencySelectorRvAdapter);

                }
            });
        }
    }
}
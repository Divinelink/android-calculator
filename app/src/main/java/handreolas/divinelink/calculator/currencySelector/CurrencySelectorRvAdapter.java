package handreolas.divinelink.calculator.currencySelector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import handreolas.divinelink.calculator.R;
import handreolas.divinelink.calculator.currency.CurrencyDomain;

public class CurrencySelectorRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private ArrayList<CurrencyDomain> currencySymbols;
    final OnCurrencySelectorClickListener listener;
    final private Context context;


    public CurrencySelectorRvAdapter(ArrayList<CurrencyDomain> currencySymbols, OnCurrencySelectorClickListener listener, Context context) {
        this.currencySymbols = currencySymbols;
        this.listener = listener;
        this.context = context;
    }

    static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        final private TextView currencyInfo;
        final private LinearLayout currencyItemRoot;

        public CurrencyViewHolder(View v){
            super(v);
            this.currencyInfo = v.findViewById(R.id.currencyDescription);
            this.currencyItemRoot = v.findViewById(R.id.currencySelectorRootItem);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.currency_selector_item, viewGroup, false);
        CurrencyViewHolder vh = new CurrencyViewHolder(v);


        return vh;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       CurrencyViewHolder currencyViewHolder = (CurrencyViewHolder) holder;


       currencyViewHolder.currencyInfo.setText(String.format("%s %s", currencySymbols.get(position).getDescription(), currencySymbols.get(position).getSymbol()));

       currencyViewHolder.currencyItemRoot.setOnClickListener(view -> {
           listener.onCurrencyClickListener(currencySymbols.get(position));
       });

       currencyViewHolder.currencyItemRoot.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View view, MotionEvent motionEvent) {

               currencyViewHolder.currencyItemRoot.setBackground(context.getDrawable(R.drawable.item_pressed));

               return false;
           }
       });

    }

    @Override
    public int getItemCount() {
        return currencySymbols.size();
    }
}

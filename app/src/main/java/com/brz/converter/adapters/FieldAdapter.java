package com.brz.converter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.brz.converter.Currency;
import com.brz.converter.MainActivity;
import com.brz.converter.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FieldAdapter extends ArrayAdapter<String> {

    private ArrayList<TextView> textViews = new ArrayList<>();
    private ArrayList<Spinner> spinners = new ArrayList<>();
    private int selectedPosition = 0;

    public FieldAdapter(@NonNull Context context, int resource, String[] strings) {
        super(context, resource, strings);
    }

    public TextView getTextView(int position){
        return textViews.get(position);
    }

    public Currency selectedSpinnerItem(int position){
        return (Currency)spinners.get(position).getSelectedItem();
    }

    public int getSelectedPosition(){
        return selectedPosition;
    }

    public void setSelectedPosition(int position){
        selectedPosition = position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View grid;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            grid = inflater.inflate(R.layout.field_item, parent, false);
        }else{
            grid = convertView;
        }

        Currency[] currencies = MainActivity.currencies.toArray(new Currency[0]);
        CurrencySpinnerAdapter spinnerAdapter = new CurrencySpinnerAdapter(getContext(), R.layout.spinner_item, currencies);

        final TextView inputText = grid.findViewById(R.id.input_text);
        inputText.setHeight(parent.getHeight()/getCount());
        inputText.setWidth(parent.getWidth()/2);

        Spinner currencySpinner = grid.findViewById(R.id.currency_spinner);
        currencySpinner.setAdapter(spinnerAdapter);
        currencySpinner.setFocusable(false);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = (String) inputText.getText();
                boolean isMain;
                if(value.isEmpty()){
                    isMain = false;
                    value = String.valueOf(selectedSpinnerItem(selectedPosition).getNominal());
                }else{
                    isMain = true;
                }
                for(int j = 0; j<getCount(); j++){
                    if(j!=selectedPosition) {
                        TextView textView = getTextView(j);
                        Currency currencyNew = selectedSpinnerItem(j);
                        Currency currencyOld = selectedSpinnerItem(selectedPosition);
                        double result = (Double.parseDouble(String.valueOf(value))*currencyOld.getValue()*currencyNew.getNominal())/(currencyOld.getNominal()*currencyNew.getValue());
                        String res = String.valueOf(result);
                        /*if(res.length()-res.indexOf('.')>4){
                            res = res.substring(0, res.indexOf('.')+4);
                        }*/
                        if(isMain) {
                            textView.setText(res);
                        }else{
                            textView.setText("");
                            textView.setHint(res);
                            getTextView(selectedPosition).setHint(value);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if(textViews.size()>position){
            textViews.set(position, inputText);
            spinners.set(position, currencySpinner);
        }else{
            textViews.add(position, inputText);
            spinners.add(position, currencySpinner);
        }

        return grid;
    }
}

package com.brz.converter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.brz.converter.adapters.FieldAdapter;
import com.brz.converter.adapters.KeyboardAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static List<Currency> currencies;
    private Animation shakeAnimation;
    private File cbrFile;
    private static FieldAdapter fieldAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shakeAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        cbrFile = new File(getFilesDir(),"cbr.xml");
        try {
            InputStream inputStream = getApplicationContext().getAssets().open("cbr");
            FileOutputStream outputStream = new FileOutputStream(cbrFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        //currencies = new CbrCurrencyParser(getResources().getString(R.string.cbr_xml_url)).parse();
        currencies = new CbrCurrencyParser(cbrFile).parse();
        refreshCurrencyValues();

        fieldAdapter = new FieldAdapter(this, R.layout.field_item,
                getResources().getStringArray(R.array.field_id));
        ListView fieldView = findViewById(R.id.field_view);
        fieldView.setAdapter(fieldAdapter);
        fieldView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value  = (String) fieldAdapter.getTextView(fieldAdapter.getSelectedPosition()).getText();
                fieldAdapter.setSelectedPosition(i);
                if(value.isEmpty()){
                    updateValues(String.valueOf(fieldAdapter.selectedSpinnerItem(i).getNominal()), i, false);
                }else {
                    updateValues(value, i, true);
                }
                fieldAdapter.getTextView(i).setText(value);
            }
        });

        KeyboardAdapter keyboardAdapter = new KeyboardAdapter(this, R.layout.keyboard_item, getResources().getStringArray(R.array.keyboard));
        GridView keyboardView = findViewById(R.id.keyboard_view);
        keyboardView.setAdapter(keyboardAdapter);
        keyboardView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String buttonValue = getResources().getStringArray(R.array.keyboard)[i];
                TextView textView = fieldAdapter.getTextView(fieldAdapter.getSelectedPosition());
                if(buttonValue.equalsIgnoreCase("del")){
                    textView.setText("");
                    textView.setHint(String.valueOf(currencies.get(i).getNominal()));
                    updateValues(String.valueOf(currencies.get(i).getNominal()), fieldAdapter.getSelectedPosition(), false);
                }
                return false;
            }
        });
        keyboardView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String buttonValue = getResources().getStringArray(R.array.keyboard)[i];
                TextView textView = fieldAdapter.getTextView(fieldAdapter.getSelectedPosition());
                String tmp = String.valueOf(textView.getText());
                if(buttonValue.equalsIgnoreCase("del")){
                    if(tmp.length()>0) {
                        tmp = tmp.substring(0, tmp.length() - 1);
                    }else {
                        tmp="";
                    }
                }else {
                    if (buttonValue.equals(".")) {
                        if (tmp.isEmpty()) {
                            tmp = "0.";
                        } else if (!tmp.contains(".")) {
                            tmp = tmp + buttonValue;
                        } else {
                            textView.startAnimation(shakeAnimation);
                        }
                    } else {
                        if ((tmp.contains(".") && tmp.length() < 15) || (tmp.length() < 9 )) {
                            tmp = tmp + buttonValue;
                        } else {
                            textView.startAnimation(shakeAnimation);
                        }
                    }

                }

                textView.setText(tmp);
                if(tmp.length()>0) {
                    if(tmp.indexOf('.')!=tmp.length()-1) {
                        updateValues(tmp, fieldAdapter.getSelectedPosition(), true);
                    }
                }else {
                    updateValues(String.valueOf(currencies.get(fieldAdapter.getSelectedPosition()).getNominal()), fieldAdapter.getSelectedPosition(), false);
                }
            }
        });

    }

    private void updateValues(String value, int position, boolean isMain){
        for(int i = 0; i<fieldAdapter.getCount(); i++){
            if(i!=position) {
                TextView textView = fieldAdapter.getTextView(i);
                Currency currencyNew = fieldAdapter.selectedSpinnerItem(i);
                Currency currencyOld = fieldAdapter.selectedSpinnerItem(position);
                double result = (Double.parseDouble(value)*currencyOld.getValue()*currencyNew.getNominal())/(currencyOld.getNominal()*currencyNew.getValue());

                String res = String.valueOf(result);
                /*if(res.length()-res.indexOf('.')>4){
                    res = res.substring(0, res.indexOf('.')+4);
                }*/
                if(isMain) {
                    textView.setText(res);
                }else{
                    textView.setText("");
                    textView.setHint(res);
                    fieldAdapter.getTextView(position).setHint(value);
                }
            }
        }
    }

    private void refreshCurrencyValues(){
        FileLoadingTask mt = new FileLoadingTask(getResources().getString(R.string.cbr_xml_url),cbrFile, getApplicationContext());
        mt.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            refreshCurrencyValues();
        }
        return true;
    }
}

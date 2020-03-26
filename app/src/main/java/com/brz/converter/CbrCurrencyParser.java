package com.brz.converter;

import android.util.Xml;

import com.brz.converter.protos.XmlParser;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CbrCurrencyParser implements XmlParser {


    private static final String CHAR_CODE = "CharCode";
    private static final String NOMINAL = "Nominal";
    private static final String NAME = "Name";
    private static final String VALUE = "Value";
    private static final String VALUTE = "Valute";
    private static final String VAL_CURS = "ValCurs";
    private static final String ID = "ID";

    private URL url;
    private File file;

    public CbrCurrencyParser(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    CbrCurrencyParser(File file) {
        this.file = file;
    }

    @Override
    public List<Currency> parse() {

        List<Currency> currencies = null;
        XmlPullParser parser = Xml.newPullParser();
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN); //numbers with comma

        try {
            InputStream inputStream;
            if(url == null){
                inputStream = new FileInputStream(file);
            }else{
                inputStream = url.openConnection().getInputStream();
            }
            parser.setInput(inputStream, null);
            int eventType = parser.getEventType();
            Currency currentCurrency = null;
            boolean done = false;

            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        currencies = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(VALUTE)) {
                            currentCurrency = new Currency();
                            if (parser.getAttributeCount() > 0)
                                if (parser.getAttributeName(0).equalsIgnoreCase(ID)) {
                                    currentCurrency.setId(parser.getAttributeValue(0));
                                }
                        } else if (currentCurrency != null) {
                            if (name.equalsIgnoreCase(CHAR_CODE)) {
                                currentCurrency.setCharCode(parser.nextText());
                            } else if (name.equalsIgnoreCase(NOMINAL)) {
                                currentCurrency.setNominal(Integer.parseInt(parser.nextText()));
                            } else if (name.equalsIgnoreCase(NAME)) {
                                currentCurrency.setName(parser.nextText());
                            } else if (name.equalsIgnoreCase(VALUE)) {
                                currentCurrency.setValue(numberFormat.parse(parser.nextText()).doubleValue());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(VALUTE) && currentCurrency != null && currencies != null) {
                            currencies.add(currentCurrency);
                        } else if (name.equalsIgnoreCase(VAL_CURS)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return currencies;
    }
}
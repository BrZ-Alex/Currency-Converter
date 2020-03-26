package com.brz.converter;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileLoadingTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private File destination;
    private Throwable throwable;
    private Context context;

    FileLoadingTask(String url, File destination, Context context) {
        this.url = url;
        this.destination = destination;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            FileUtils.copyURLToFile(new URL(url), destination);
        } catch (IOException e) {
            throwable = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (throwable != null) {
            Toast.makeText(context, context.getResources().getString(R.string.refresh_fail), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.refresh_success), Toast.LENGTH_SHORT).show();
            MainActivity.currencies = new CbrCurrencyParser(destination).parse();

        }
    }
}
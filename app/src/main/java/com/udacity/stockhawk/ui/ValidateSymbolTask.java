package com.udacity.stockhawk.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.udacity.stockhawk.R;

import java.io.IOException;

import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.udacity.stockhawk.R.id.symbol;

/**
 * Created by gene on 12/13/16.
 */

public class ValidateSymbolTask extends AsyncTask<Void, Void, Boolean> {

    Context mContext;
    String mSymbol;

    public ValidateSymbolTask(Context context, String symbol) {
        mContext = context;
        mSymbol = symbol;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            Stock stock = YahooFinance.get(mSymbol);

            if (stock.getQuote().getPrice() == null) {
                Timber.d("quote is null");
                return false;
            } else {
                Timber.d("quote is not null");
                return true;
            }
        } catch (IOException e) {
            Timber.d("Error while getting stock");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean isValid) {
        if (isValid && mContext instanceof MainActivity) {
            Timber.d("is valid");
            ((MainActivity)mContext).addStock(mSymbol);
        } else {
            Timber.d("is not valid");
            Toast.makeText(mContext, R.string.toast_stock_does_not_exist, Toast.LENGTH_SHORT).show();
        }
    }
}

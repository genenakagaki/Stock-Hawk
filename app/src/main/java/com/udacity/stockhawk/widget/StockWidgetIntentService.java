package com.udacity.stockhawk.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.udacity.stockhawk.data.Contract;

/**
 * Created by gene on 12/16/16.
 */

public class StockWidgetIntentService extends IntentService {

    public StockWidgetIntentService() {
        super(StockWidgetIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                StockWidgetProvider.class));

        // Get stock data from ContentProvider
        //Uri uri = Contract.Quote.makeUriForStock()
    }
}

package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Visibility;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.PrefUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import yahoofinance.histquotes.HistoricalQuote;

import static com.udacity.stockhawk.R.id.graph;
import static com.udacity.stockhawk.data.Contract.*;

/**
 * Created by gene on 12/13/16.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_DETAIL_URI = "arg_detail_uri";

    private Uri mContentUri;

    private static final int DETAIL_LOADER = 0;

    @BindView(R.id.data_upto_date_textview)
    TextView mDataUpToDateTextView;
    @BindView(graph)
    GraphView mGraphView;

    public static DetailFragment newInstance(Uri contentUri) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_DETAIL_URI, contentUri);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }

    public DetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mContentUri = args.getParcelable(ARG_DETAIL_URI);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mContentUri != null) {
            return new CursorLoader(
                    getActivity(),
                    mContentUri,
                    Quote.QUOTE_COLUMNS,
                    null,
                    null,
                    null
            );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            // Set title
            String symbol = data.getString(Quote.POSITION_SYMBOL);
            getActivity().setTitle(symbol);

            if (PrefUtils.isDataUpToDate(getActivity())) {
                mDataUpToDateTextView.setVisibility(View.GONE);
            } else {
                mDataUpToDateTextView.setVisibility(View.VISIBLE);
            }

            // Fill graph with data
            String history = data.getString(Quote.POSITION_HISTORY);
            String[] histories = history.split("\n");
            Calendar calendar = Calendar.getInstance();

            DataPoint[] dataPoints = new DataPoint[histories.length];

            Date firstDate = null;

            for (int i = 0; i < histories.length; i++) {
                String[] values = histories[i].split(", ", 2);

                calendar.setTimeInMillis(Long.parseLong(values[0]));
                Date date = calendar.getTime();

                if (i == 0) {
                    firstDate = calendar.getTime();
                }

                dataPoints[histories.length-1 -i] = new DataPoint(date.getTime(), Double.parseDouble(values[1]));
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

            mGraphView.addSeries(series);
            // set date label formatter
            mGraphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            mGraphView.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space


            // set manual x bounds to have nice steps
            mGraphView.getViewport().setMinX(calendar.getTime().getTime());
            mGraphView.getViewport().setMaxX(firstDate.getTime());
            mGraphView.getViewport().setXAxisBoundsManual(true);
            // as we use dates as labels, the human rounding to nice readable numbers
            // is not necessary
            mGraphView.getGridLabelRenderer().setHumanRounding(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

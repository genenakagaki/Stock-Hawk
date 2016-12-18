package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;

/**
 * Created by gene on 12/13/16.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            DetailFragment detailFragment = DetailFragment.newInstance(getIntent().getData());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.stock_detail_container, detailFragment)
                    .commit();
        }
    }
}

package com.ecarus.employee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SearchOrScanProfileBarcode extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout mScanBarcodeLinearLayout;
    private SearchView searchView;
    public static String mSearchBarcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_scan_profile_barcode);

        mScanBarcodeLinearLayout = (LinearLayout)findViewById(R.id.scanBarcodeLinearLayout);
        mScanBarcodeLinearLayout.setOnClickListener(this);

        searchView = (SearchView)findViewById(R.id.barcodeSearchView);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint("Search profile barcode");
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchBarcode = query.trim();
                Intent intent = new Intent(SearchOrScanProfileBarcode.this,BarcodeGetOrders.class);
                Bundle bundle = new Bundle();
                bundle.putString("barcode",mSearchBarcode);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == mScanBarcodeLinearLayout){
            startActivity(new Intent(this, BarcodeScanner.class));
        }
    }
}

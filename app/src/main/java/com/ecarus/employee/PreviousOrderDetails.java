package com.ecarus.employee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreviousOrderDetails extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<PreviousOrderDetailsList> previousOrderDetailsList;
    private TextView mTotalItems, mTotalQuantity, mTotalBill, mTotalWeight;
    private Button mCheckout;
    float weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previous_order_details);

        mTotalItems = (TextView) findViewById(R.id.totalItems);
        mTotalQuantity = (TextView) findViewById(R.id.totalQuantity);
        mTotalBill = (TextView) findViewById(R.id.totalBill);
        mTotalWeight = (TextView) findViewById(R.id.totalWeight);

        recyclerView = (RecyclerView) findViewById(R.id.PreviousOrderDetailsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCheckout = (Button) findViewById(R.id.checkoutButton);
        mCheckout.setOnClickListener(this);
        previousOrderDetailsList = new ArrayList<>();
        loadPreviousOrderDetails();
        //loadTotalBill();
    }

    private void loadPreviousOrderDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading order details...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PREVIOUS_ORDER_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("productDetails");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                PreviousOrderDetailsList item = new PreviousOrderDetailsList(o.getString("products")
                                        , o.getString("quantity"), o.getString("total"), o.getString("totalweight"));
                                previousOrderDetailsList.add(item);
                            }
                            loadEmployeeDetails();
                            adapter = new PreviousOrderDetailsAdapter(previousOrderDetailsList, PreviousOrderDetails.this);
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderId", PreviousOrderAdapter.OrderId);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void loadEmployeeDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading order details...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PREVIOUS_ORDER_DETAILS1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("employeeDetails");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                mTotalItems.setText(o.getString("TotalItems"));
                                mTotalQuantity.setText(o.getString("TotalQuantity"));
                                mTotalBill.setText("₹ "+o.getString("TotalBill"));
                                weight = Float.parseFloat(o.getString("TotalWeight"));
                            }
                            weight = weight+7;
                            mTotalWeight.setText(String.valueOf(weight)+ " Kg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderId", PreviousOrderAdapter.OrderId);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void checkoutSuccessfully() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_PREVIOUS_ORDER_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {
                                final AlertDialog.Builder alert = new AlertDialog.Builder(PreviousOrderDetails.this);
                                alert.setTitle("Notification from server");
                                alert.setMessage(jsonObject.getString("message"));
                                alert.setCancelable(false);

                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        Intent a = new Intent(PreviousOrderDetails.this,SearchOrScanProfileBarcode.class);
                                        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(a);


                                    }
                                });
                                final AlertDialog alertDialog = alert.create();
                                alertDialog.show();

                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderId", PreviousOrderAdapter.OrderId);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }


    @Override
    public void onClick(View v) {
        if (v == mCheckout){
            checkoutSuccessfully();
        }
    }


}
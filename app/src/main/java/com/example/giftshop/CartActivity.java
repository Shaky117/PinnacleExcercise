package com.example.giftshop;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private ProductsAdapter adapter;

    private TextView btnGoBack;

    private AppCompatButton btnBuy;

    private ArrayList<Product> productsList;

    private AdminSQLiteOpenHelper dbHelper;

    private int type;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Bundle bundle = getIntent().getExtras();

        type = bundle.getInt("type");
        id = bundle.getInt("id");

        productsList = new ArrayList<>();
        productsList.clear();

        dbHelper = new AdminSQLiteOpenHelper(this);

        btnBuy = findViewById(R.id.btnBuy);
        btnGoBack = findViewById(R.id.btnBack);

        setUpRecyclerView();

        btnGoBack.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
    }

    public void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvCart);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        adapter = new ProductsAdapter(productsList, new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {

            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getDataFromSQLite();
    }

    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                productsList.clear();
                productsList.clear();
                String idString = String.valueOf(id);
                productsList.addAll(dbHelper.getUserCart(idString));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnBuy:
                if(type == -1){
                    Toast.makeText(getApplicationContext(), "You need to be logged in",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "You have finished your purchase",
                            Toast.LENGTH_LONG).show();
                    dbHelper.deleteCart();
                }
                break;
        }
    }
}

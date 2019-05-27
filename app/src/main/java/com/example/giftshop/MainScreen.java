package com.example.giftshop;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {

    private ProductsAdapter adapter;
    private ArrayList<Product> productsList;

    private FloatingActionButton btnAddProduct;

    private AdminSQLiteOpenHelper dbHelper;

    private TextView btnCart;
    private TextView txtCartCount;

    private int type;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        dbHelper = new AdminSQLiteOpenHelper(this);

        btnAddProduct = findViewById(R.id.aBtnAdd);
        btnCart = findViewById(R.id.btnCart);
        txtCartCount = findViewById(R.id.txtCartCount);

        txtCartCount.setVisibility(View.INVISIBLE);

        btnAddProduct.hide();
        productsList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("TYPE");
        id = bundle.getInt("ID");

        if (type == 0 || type == -1) {
            btnAddProduct.hide();
        }else{
            btnAddProduct.show();
        }

        btnAddProduct.setOnClickListener(this);
        btnCart.setOnClickListener(this);

        setUpRecyclerView();

    }

    public void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvProducts);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        adapter = new ProductsAdapter(productsList, new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(MainScreen.this, ProductDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", type);
                bundle.putInt("id", product.getId());
                bundle.putString("name", product.getName());
                bundle.putInt("price", product.getPrice());
                bundle.putString("description", product.getDescription());
                bundle.putString("category", product.getCategory());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getDataFromSQLite();
    }

    public void onUpdateList(){
        int count = adapter.getCartCount();

        if(count > 0){
            txtCartCount.setVisibility(View.VISIBLE);
            txtCartCount.setText(String.valueOf(count));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aBtnAdd:
                Intent intentAdd = new Intent(MainScreen.this, AddProductActivity.class);
                startActivity(intentAdd);
                break;
            case R.id.btnCart:
                ArrayList<Product> cart = adapter.getCart();

                for(int i = 0; i < cart.size(); i++){
                    dbHelper.addCart(cart.get(i), String.valueOf(id));
                }

                Intent intentCart = new Intent(MainScreen.this, CartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", type);
                bundle.putInt("id", id);
                intentCart.putExtras(bundle);
                startActivity(intentCart);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromSQLite();
    }

    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                productsList.clear();

                productsList.addAll(dbHelper.getAllProduct());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
                onUpdateList();
            }
        }.execute();
    }
}

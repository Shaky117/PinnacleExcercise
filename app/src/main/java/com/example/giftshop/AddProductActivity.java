package com.example.giftshop;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton btnAdd;

    private AdminSQLiteOpenHelper dbHelper;

    private EditText nameET;
    private EditText priceET;
    private EditText descriptionET;
    private EditText categoryET;

    private TextView btnBack;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dbHelper = new AdminSQLiteOpenHelper(this);

        btnBack = findViewById(R.id.btnBack);
        nameET = findViewById(R.id.eTNameAdd);
        priceET = findViewById(R.id.eTPriceAdd);
        descriptionET = findViewById(R.id.eTDescriptionAdd);
        categoryET = findViewById(R.id.eTCategoryAdd);
        btnAdd = findViewById(R.id.btnAddProduct);

        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddProduct:
                String name = nameET.getText().toString();
                int price = Integer.valueOf(priceET.getText().toString());
                String description = descriptionET.getText().toString();
                String category = categoryET.getText().toString();

                product = new Product(name, price, description, category);

                dbHelper.addProduct(product);

                Toast.makeText(getApplicationContext(), "Product Added", Toast.LENGTH_LONG).show();

                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }
}

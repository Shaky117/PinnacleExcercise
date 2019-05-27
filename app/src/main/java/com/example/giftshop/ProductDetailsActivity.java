package com.example.giftshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private AdminSQLiteOpenHelper dbHelper;

    private AppCompatButton btnEdit;
    private AppCompatButton btnDelete;

    private int id;
    private int type;

    private TextView txtName;
    private TextView txtPrice;
    private TextView txtDescription;
    private TextView txtCategory;
    private TextView btnGoBack;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Bundle bundle = getIntent().getExtras();

        type = bundle.getInt("type");
        id = bundle.getInt("id");
        String name = bundle.getString("name");
        int price = bundle.getInt("price");
        String description = bundle.getString("description");
        String category = bundle.getString("category");

        dbHelper = new AdminSQLiteOpenHelper(this);
        txtName = findViewById(R.id.txtNameProduct);
        txtCategory = findViewById(R.id.txtCategoryProduct);
        txtDescription = findViewById(R.id.txtDescriptionProduct);
        txtPrice = findViewById(R.id.txtPriceProduct);
        btnEdit = findViewById(R.id.btnEditProduct);
        btnDelete = findViewById(R.id.btnDelete);
        btnGoBack = findViewById(R.id.btnBack);

        txtName.setText(name);
        txtCategory.setText(category);
        txtDescription.setText(description);
        txtPrice.setText("$ " + String.valueOf(price));

        btnDelete.setVisibility(View.INVISIBLE);
        btnEdit.setVisibility(View.INVISIBLE);

        if(type == 1){
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnDelete.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnGoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnEditProduct:
                Intent intent = new Intent(ProductDetailsActivity.this, EditProductActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
            case R.id.btnDelete:
                dbHelper.deleteProduct(String.valueOf(id));
                finish();
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }
}

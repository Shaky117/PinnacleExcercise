package com.example.giftshop;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProductActivity extends AppCompatActivity implements View.OnClickListener {

    private AdminSQLiteOpenHelper dbHelper;

    private AppCompatButton btnEdit;

    private Context context;

    private EditText eTName;
    private EditText eTPrice;
    private EditText eTDescription;
    private EditText eTCategory;

    private int id;

    private TextView btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        dbHelper = new AdminSQLiteOpenHelper(this);

        context = this;

        eTName = findViewById(R.id.eTNameEdit);
        eTPrice = findViewById(R.id.etPriceEdit);
        eTDescription = findViewById(R.id.eTDescripcionEdit);
        eTCategory = findViewById(R.id.etCategory);
        btnEdit = findViewById(R.id.btnEditProduct);
        btnGoBack = findViewById(R.id.btnBack);

        btnEdit.setOnClickListener(this);
        btnGoBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnEditProduct:

                String name = eTName.getText().toString().trim();
                String price = eTPrice.getText().toString().trim();
                String description = eTDescription.getText().toString().trim();
                String category = eTCategory.getText().toString().trim();
                String idString = String.valueOf(id);

                if(!name.isEmpty() || !price.isEmpty() || !description.isEmpty() || !category.isEmpty() || !idString.isEmpty()) {
                    dbHelper.updateProduct(idString, name, price, description, category);
                }else{
                    Toast.makeText(getApplicationContext(), "Fill all the missing fields", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}

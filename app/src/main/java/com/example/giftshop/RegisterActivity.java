package com.example.giftshop;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText eTName;
    private TextInputEditText eTType;
    private TextInputEditText eTPassword;
    private TextInputEditText eTConfirmPassword;

    private AppCompatButton btnRegister;
    private AppCompatTextView txtLoginLink;

    private AdminSQLiteOpenHelper dbHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new AdminSQLiteOpenHelper(this);
        user = new User();

        eTName = findViewById(R.id.textInputEditTextName);
        eTType = findViewById(R.id.textInputEditTextType);
        eTPassword = findViewById(R.id.textInputEditTextPassword);
        eTConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);

        btnRegister = findViewById(R.id.btnAddProduct);

        txtLoginLink = findViewById(R.id.appCompatTextViewLoginLink);

        btnRegister.setOnClickListener(this);
        txtLoginLink.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddProduct:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    private void postDataToSQLite() {
        String name = eTName.getText().toString().trim();
        String type = eTType.getText().toString().trim();
        String password = eTPassword.getText().toString().trim();
        String repeatPassword = eTConfirmPassword.getText().toString().trim();

        if(name.isEmpty() || type.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fill all missing fields", Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.equals(repeatPassword)){
            Toast.makeText(getApplicationContext(), "Input the same password in both fields", Toast.LENGTH_LONG).show();
            return;
        }

        int typeInt = Integer.valueOf(type);

        if(typeInt < 0 || typeInt > 1){
            Toast.makeText(getApplicationContext(), "Please Input value of 0 or 1", Toast.LENGTH_LONG).show();
            return;
        }

        if (!dbHelper.checkUser(eTType.getText().toString().trim())) {

            user.setName(eTName.getText().toString().trim());
            user.setType(Integer.valueOf(eTType.getText().toString().trim()));
            user.setPassword(eTPassword.getText().toString().trim());

            dbHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Toast.makeText(getApplicationContext(), getString(R.string.success_message), Toast.LENGTH_LONG).show();
            emptyInputEditText();


        } else {
            // Snack Bar to show error message that record already exists
            Toast.makeText(getApplicationContext(), getString(R.string.error_email_exists), Toast.LENGTH_LONG).show();
        }


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        eTName.setText(null);
        eTType.setText(null);
        eTPassword.setText(null);
        eTConfirmPassword.setText(null);
    }
}

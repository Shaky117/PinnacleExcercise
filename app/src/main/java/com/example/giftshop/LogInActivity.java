package com.example.giftshop;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText eTEmail;
    private TextInputEditText eTPassword;

    private AppCompatButton btnLogin;
    private AppCompatButton btnGuest;

    private AppCompatTextView textViewLinkRegister;

    private AdminSQLiteOpenHelper dbHelper;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = new User();

        eTEmail = findViewById(R.id.textInputEditTextEmail);
        eTPassword = findViewById(R.id.textInputEditTextPassword);

        btnLogin = findViewById(R.id.appCompatButtonLogin);
        btnGuest = findViewById(R.id.btnEditProduct);

        textViewLinkRegister = findViewById(R.id.textViewLinkRegister);

        btnLogin.setOnClickListener(this);
        btnGuest.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);

        dbHelper = new AdminSQLiteOpenHelper(this);

        /*user.setName("admin");
        user.setPassword("123");
        user.setType(1);
        */
        dbHelper.addUser(user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.btnEditProduct:
                Intent accountsIntent = new Intent(LogInActivity.this, MainScreen.class);
                int type = -1;
                accountsIntent.putExtra("TYPE", type);
                startActivity(accountsIntent);
                break;
        }
    }

    private void verifyFromSQLite() {
        String name = eTEmail.getText().toString().trim();
        String password = eTPassword.getText().toString().trim();

        if(name.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), getString(R.string.error_valid_email_password), Toast.LENGTH_LONG).show();
            return;
        }

        if (dbHelper.checkUser(name, password)){
            Intent accountsIntent = new Intent(LogInActivity.this, MainScreen.class);
            int type;
            int id;
            type = dbHelper.getUserType(name, password);
            id = dbHelper.getUserId(name, password);
            Bundle bundle = new Bundle();
            bundle.putInt("TYPE", type);
            bundle.putInt("ID", id);
            accountsIntent.putExtras(bundle);
            emptyInputEditText();
            startActivity(accountsIntent);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_valid_email_password), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        eTEmail.setText(null);
        eTPassword.setText(null);
    }
}

package com.aanchal.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.aanchal.taskmanager.bll.LoginBLL;
import com.aanchal.taskmanager.strictmode.StrictModeClass;

public class LoginActivity extends AppCompatActivity {
    EditText etuser,etpassword;
    Button btnlogin;
    TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        etuser = findViewById(R.id.etuser);
        etpassword = findViewById(R.id.etpassword);
        tvSignup = findViewById(R.id.tvSignup);
        btnlogin = findViewById(R.id.btnlogin);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(); }
        });
    }

    private void login() {
        String username = etuser.getText().toString();
        String password = etpassword.getText().toString();

        LoginBLL loginBLL = new LoginBLL();

        StrictModeClass.StrictMode();
        if (loginBLL.checkUser(username, password)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Either username or password is incorrect", Toast.LENGTH_SHORT).show();
            etuser.requestFocus();
        }
    }
}

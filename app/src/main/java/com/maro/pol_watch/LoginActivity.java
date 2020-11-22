package com.maro.pol_watch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText userName, passWord;
    Button login;
//    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.username);
        passWord = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(view -> {
            String username = userName.getText().toString();
            String password = passWord.getText().toString();

            if (username.length() < 2 || username.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter a username with more than two characters", Toast.LENGTH_SHORT).show();
            }else if (password.length() < 2 || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter a password with more than two characters", Toast.LENGTH_SHORT).show();
            } else {
                userLogin(username,password);
            }
        });
    }

    public void userLogin(String name, String pass) {
//        mAPIService.loginUser(name, pass).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if(response.isSuccessful()) {
//                    Log.d("whyy", "more why");
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                } else {
//                    Log.d("whyy", "less why");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "Unable to login. Please check your network connection and try again", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
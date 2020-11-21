package com.maro.pol_watch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText username, email, firstName, lastName, password, phone;
    Spinner role_spinner;
    String role;
    Button register;
    private APIService mAPIService;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone_number);
        role_spinner = findViewById(R.id.role);
        register = findViewById(R.id.sign_up);
        login = findViewById(R.id.login);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.roles, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        role_spinner.setAdapter(adapter);
        role_spinner.setOnItemSelectedListener(this);
        mAPIService = ApiUtils.getAPIService();

        login.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });

        register.setOnClickListener(view -> {
            String userName = username.getText().toString();
            String Email = email.getText().toString();
            String firstname = firstName.getText().toString();
            String lastname = lastName.getText().toString();
            String passWord = password.getText().toString();
            String number = phone.getText().toString();
//                role = role_spinner.toString();

            if (userName.length() < 2 || userName.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please enter a username with more than 2 characters", Toast.LENGTH_SHORT).show();
            } else if (Email.isEmpty() || !Email.contains("@")) {
                Toast.makeText(SignUpActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else if (firstname.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
            } else if (lastname.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
            } else if (passWord.isEmpty() || passWord.length() < 4) {
                Toast.makeText(SignUpActivity.this, "Please enter a password with more than 4 characters", Toast.LENGTH_SHORT).show();
            } else if (number.isEmpty() || number.length() < 11) {
                Toast.makeText(SignUpActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(userName, Email, firstname, lastname, number, passWord, role);
            }
        });
    }

    public void registerUser(String un, String em, String fn, String ln, String pn, String pw, String ro ) {
        mAPIService.createUser(un, em, fn, ln, pn, pw, ro).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    Log.d("whyy", "more why");
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                } else {
                    Log.d("whyy", "less why");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Unable to register. Please check your network connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        role = (String) adapterView.getItemAtPosition(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
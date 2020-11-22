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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText username, email, firstName, lastName, password, phone;
    Spinner role_spinner;
    String role;
    Button register;
    TextView login;
    ProgressBar progressBar;

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
        progressBar = findViewById(R.id.progressBar2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.roles, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        role_spinner.setAdapter(adapter);
        role_spinner.setOnItemSelectedListener(this);

        login.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });

        register.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String userName = username.getText().toString();
            String Email = email.getText().toString();
            String firstname = firstName.getText().toString();
            String lastname = lastName.getText().toString();
            String passWord = password.getText().toString();
            String number = phone.getText().toString();
//                role = role_spinner.toString();

            if (userName.length() < 2) {
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
                progressBar.setVisibility(View.VISIBLE);
                register();
            }
        });
    }

    public void register(){
        ApiInterface apiInterface;
        apiInterface = ApiClient.addUser().create(ApiInterface.class);

        User user = new User();
        user.setUsername(username.getText().toString());
        user.setFirst_name(firstName.getText().toString());
        user.setLast_name(lastName.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setPassword(password.getText().toString());
        user.setRole(role);

        Call<JsonObject> call = apiInterface.register(user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code()==200){
//                    Toast.makeText(SignUpActivity.this, "SignUp successful", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                }else{
                    Toast.makeText(SignUpActivity.this, ""+ response.code(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Unsuccessful" + t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        role = (String) adapterView.getSelectedItem();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
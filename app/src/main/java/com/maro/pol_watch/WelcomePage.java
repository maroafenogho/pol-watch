package com.maro.pol_watch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {

    TextView continueText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        continueText = findViewById(R.id.textViewContinue);

        continueText.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomePage.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
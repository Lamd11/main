package com.example.myapplicationnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountCreatedPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created_page);

        Button continueButton = (Button)findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountCreatedPage.this, SignInPage.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, SignInPage.class));
        finish();
    }
}
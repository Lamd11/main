package com.example.myapplicationnew;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplicationnew.authUtils.PasswordEncryption;
import com.example.myapplicationnew.authUtils.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.logging.Level;

public class CreateAccountPage extends AppCompatActivity {
    public static ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_page);

        loadData();
        Button createAccountButton = (Button)findViewById(R.id.createAccountButton);
        EditText createUsernameBox = (EditText)findViewById(R.id.createUsernameBox);
        EditText createPasswordBox = (EditText)findViewById(R.id.createPasswordBox);
        EditText confirmPasswordBox = (EditText)findViewById(R.id.confirmPasswordBox);
        EditText secQuestionOneBox = (EditText)findViewById(R.id.securityQuestion1Box);
        EditText secQuestionTwoBox = (EditText)findViewById(R.id.securityQuestion2Box);
        EditText secQuestionThreeBox = (EditText)findViewById(R.id.securityQuestion3Box);
        ImageButton backButton = (ImageButton)findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                createUsernameBox.setBackgroundResource(R.drawable.black_border);
                createPasswordBox.setBackgroundResource(R.drawable.black_border);
                confirmPasswordBox.setBackgroundResource(R.drawable.black_border);

                if(createUsernameBox.getText().toString().length() < 3 || createUsernameBox.getText().toString().length() > 10) {
                    createUsernameBox.setText("");
                    createUsernameBox.setBackgroundResource(R.drawable.red_border);
                }
                else if(createPasswordBox.getText().toString().length() < 7 || createPasswordBox.getText().toString().length() > 15 || !createPasswordBox.getText().toString().equals(confirmPasswordBox.getText().toString())) {
                    createPasswordBox.setText("");
                    confirmPasswordBox.setText("");
                    createPasswordBox.setBackgroundResource(R.drawable.red_border);
                    confirmPasswordBox.setBackgroundResource(R.drawable.red_border);
                }
                else if(secQuestionOneBox.getText().toString().isEmpty()) {
                    secQuestionOneBox.setBackgroundResource(R.drawable.red_border);
                }
                else if(secQuestionTwoBox.getText().toString().isEmpty()) {
                    secQuestionTwoBox.setBackgroundResource(R.drawable.red_border);
                }
                else if(secQuestionThreeBox.getText().toString().isEmpty()) {
                    secQuestionThreeBox.setBackgroundResource(R.drawable.red_border);
                }
                else if(userList.stream().anyMatch(o -> createUsernameBox.getText().toString().equalsIgnoreCase(o.getUsername()))) {
                    createUsernameBox.setBackgroundResource(R.drawable.red_border);
                    createUsernameBox.setText("");
                    createUsernameBox.setHintTextColor(Color.RED);
                    createUsernameBox.setHint("Username already exists");
                }
                else {
                    User newUser = new User(createUsernameBox.getText().toString(), PasswordEncryption.encrypt(createPasswordBox.getText().toString()), secQuestionOneBox.getText().toString(), secQuestionTwoBox.getText().toString(), secQuestionThreeBox.getText().toString());

                    saveData(newUser);

                    startActivity(new Intent(CreateAccountPage.this, AccountCreatedPage.class));
                }
            }
        });
    }

    public void saveData(User user) {
        loadData();
        SharedPreferences mPrefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        Gson gson = new Gson();
        userList.add(user);
        String json = gson.toJson(userList);
        editor.putString("User list", json);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences mPrefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("User list", null);
        Type type = new TypeToken<ArrayList<User>>(){}.getType();
        userList = gson.fromJson(json, type);

        if(userList == null) {
            userList = new ArrayList<>();
        }

    }

    public static User findUser(String username) {
        for(int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getUsername().equalsIgnoreCase(username)) {
                return userList.get(i);
            }
        }
        return null;
    }
}
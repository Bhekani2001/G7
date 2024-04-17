package com.example.g7;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout loginLayout;
    private LinearLayout registrationLayout;
    private DatabaseHelper databaseHelper;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText registrationEmailEditText;
    private EditText registrationPasswordEditText;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        Button getStartedButton = findViewById(R.id.getStartedButton);
        loginLayout = findViewById(R.id.loginLayout);
        registrationLayout = findViewById(R.id.registrationLayout);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registrationEmailEditText = findViewById(R.id.registrationEmailEditText);
        registrationPasswordEditText = findViewById(R.id.registrationPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginScreen();
            }
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    // Show appropriate message if email or password is empty
                    showMessage("Email or password cannot be empty");
                } else {
                    // Perform login action
                    performLogin(email, password);
                }
            }
        });

        TextView registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegistrationScreen();
            }
        });

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = registrationEmailEditText.getText().toString().trim();
                String password = registrationPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    // Show appropriate message if any field is empty
                    showMessage("Email, password, or confirm password cannot be empty");
                } else {
                    // Perform registration action
                    performRegistration(email, password, confirmPassword);
                }
            }
        });

        TextView loginTextView = findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginScreen();
            }
        });
    }

    private void showLoginScreen() {
        findViewById(R.id.welcomeMessageTextView).setVisibility(View.GONE);
        findViewById(R.id.getStartedButton).setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
        registrationLayout.setVisibility(View.GONE);
    }

    private void showRegistrationScreen() {
        findViewById(R.id.welcomeMessageTextView).setVisibility(View.GONE);
        findViewById(R.id.getStartedButton).setVisibility(View.GONE);
        registrationLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
    }

    private void performLogin(String email, String password) {
        // Check if the email and password match a user in the database
        if (checkCredentials(email, password)) {
            // If credentials are correct, show the home screen
            showHomeScreen();
        } else {
            // If credentials are incorrect, display an error message
            showMessage("Invalid email or password");
        }
    }

    // Method to check if the email and password match a user in the database
    private boolean checkCredentials(String email, String password) {
        // Use the DatabaseHelper to check if the email and password match a user in the database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        return databaseHelper.checkUserExists(email, password);
    }


    private void showHomeScreen() {
        // Hide the welcome message and get started button
        findViewById(R.id.welcomeMessageTextView).setVisibility(View.GONE);
        findViewById(R.id.getStartedButton).setVisibility(View.GONE);
        findViewById(R.id.loginLayout).setVisibility(View.GONE);
        findViewById(R.id.registrationLayout).setVisibility(View.GONE);

        // Show the home layout
        findViewById(R.id.homeLayout).setVisibility(View.VISIBLE);
    }

    private void performRegistration(String email, String password, String confirmPassword) {
        // Check if the passwords match
        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match");
            return;
        }

        // Instantiate the DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        // Check if the email is already registered
        if (databaseHelper.checkUserExists(email,password)) {
            showMessage("Email already registered");
            return;
        }

        // Insert the user into the database
        long userId = databaseHelper.insertUser(email, password);

        // Check if user insertion was successful
        if (userId != -1) {
            showMessage("Registration Successful");
            // Redirect to the login screen after registration
            showLoginScreen();
        } else {
            showMessage("Registration Failed. Please try again");
        }
    }


    // Method to show a message
    private void showMessage(String message) {
        // You can customize this method to show messages in different ways, e.g., using Toast
        // For simplicity, I'm just logging the message to the console
        System.out.println(message);
    }
}

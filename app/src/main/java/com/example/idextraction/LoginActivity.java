package com.example.idextraction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                authenticateUser(username, password);
            } else {
                Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });

        registerLink.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void authenticateUser(String username, String password) {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.1.6:5000/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("username", username);
                json.put("password", password);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_SHORT).show());
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
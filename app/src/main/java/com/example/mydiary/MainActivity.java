package com.example.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText passwordEditText = findViewById(R.id.password_edit_text);
                String inputPassword = passwordEditText.getText().toString();
                if (PASSWORD.equals(inputPassword)) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Welcome to MyDiary.",
                            Toast.LENGTH_LONG
                    ).show();

                    Intent intent = new Intent(getApplicationContext(), NoteListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Invalid password! Please try again.",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });
    }
}

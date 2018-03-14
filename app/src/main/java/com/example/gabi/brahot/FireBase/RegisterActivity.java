package com.example.gabi.brahot.FireBase;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabi.brahot.Classes.Users;
import com.example.gabi.brahot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText nameET, lastNameET, emailET, cityET, passwordET;
    private Button doneBtn;
    private String email, name, lastName, city, password;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.activity_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar.setBackgroundDrawable(getDrawable(R.drawable.actionbar));
        }
        TextView actionB = (TextView) findViewById(R.id.action_bar_first_title_view);
        actionB.setText("הירשם לאפליקציה");

        // set a back button on tollbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        nameET = (EditText) findViewById(R.id.activity_register_name_edit_text);
        lastNameET = (EditText) findViewById(R.id.activity_register_last_name_edit_text);
        emailET = (EditText) findViewById(R.id.activity_register_email_edit_text);
        passwordET = (EditText) findViewById(R.id.activity_register_password_edit_text);
        cityET = (EditText) findViewById(R.id.activity_register_city_edit_text);

        doneBtn = (Button) findViewById(R.id.activity_register_done_button);
        doneBtn.setOnClickListener(this);

    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (password.length() <= 5) {
                            passwordET.setError("הסיסמה צריכה להיות לפחות 6 ספרות");
                            Toast.makeText(RegisterActivity.this, "הסיסמה צריכה להיות לפחות 6 ספרות", Toast.LENGTH_SHORT).show();
                        }else{
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "תודה שנרשמת לאפליקציית 'ברך אותי'", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    emailET.setError("כבר נרשמת עם המייל הזה");
                                    Toast.makeText(RegisterActivity.this, "כבר נרשמת עם המייל הזה", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {

        name = nameET.getText().toString().trim();
        lastName = lastNameET.getText().toString().trim();
        email = emailET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        city = cityET.getText().toString().trim();

        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || city.isEmpty()) {
            validateForm(name, lastName, email, password, city);
        } else {
            registerUser();

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("users");

            Users user = new Users(name, lastName, city, email);

            String userID = myRef.push().getKey();
            myRef.child(userID).setValue(user);

        }

    }

    // validate the EditText
    private boolean validateForm(String name, String lastName, String email, String password, String city) {
        boolean valid = true;

        if (name.isEmpty()){
            valid = false;
            nameET.setError("נא רשום את השם הפרטי...");
        }else {
            nameET.setError(null);
        }

        if (lastName.isEmpty()) {
            valid = false;
            lastNameET.setError("נא רשום את שם המשפחה...");
        } else {
            lastNameET.setError(null);
        }

        if (email.isEmpty()) {
            valid = false;
            emailET.setError("נא רשום את המייל...");
        } else {
            emailET.setError(null);
        }

        if (password.isEmpty()) {
            valid = false;
            passwordET.setError("נא רשום את הסיסמה...");
        } else {
            passwordET.setError(null);
        }

        if (city.isEmpty()) {
            valid = false;
            cityET.setError("נא רשום את העיר...");
        } else {
            cityET.setError(null);
        }


        return valid;
    }

    // back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

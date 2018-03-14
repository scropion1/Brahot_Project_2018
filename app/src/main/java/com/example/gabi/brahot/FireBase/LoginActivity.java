package com.example.gabi.brahot.FireBase;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabi.brahot.Activities.MainActivity;
import com.example.gabi.brahot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText emailET, passwordET;
    // private TextView register;
    private Button loginBtn, registerBtn;
    private String email, password;

    // private static final int SEND_SMS_PERMISSIONS_REQUEST = 1;
    private static final int WRITE_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.activity_bar);
        TextView actionB = (TextView) findViewById(R.id.action_bar_first_title_view);
        actionB.setText("התחבר לאפליקציה");


        mAuth = FirebaseAuth.getInstance();

        emailET = (EditText) findViewById(R.id.activity_login_email_edit_text);
        passwordET = (EditText) findViewById(R.id.activity_login_password_edit_text);

        loginBtn = (Button) findViewById(R.id.activity_login_login_button);
        loginBtn.setOnClickListener(this);

        registerBtn = (Button) findViewById(R.id.activity_login_register_button);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // make a permission alert
        /*int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the sms is required for this app to write files.")
                        .setTitle("Permission required");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("TAG", "Clicked");
                        makeRequest();
                    }
                });
                //Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                makeRequest();
            }
        }*/

        // make a permission alert
        int permission1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "Permission to write denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the storage is required for this app to write files.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("TAG", "Clicked");
                        makeRequest1();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                makeRequest1();
            }
        }

    }

    @Override
    public void onClick(View view) {
        email = emailET.getText().toString().trim();
        password = passwordET.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            validateForm(email, password);
        } else {
            loginUser();
        }

}
    // make request to sms
    /*protected void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                SEND_SMS_PERMISSIONS_REQUEST);
    }*/

    // make request to external storage
    protected void makeRequest1() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                WRITE_REQUEST_CODE);
    }

    // logion new user to firebase Authentication
    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "לא קיים משתמש כזה, נא לחצו על 'הרשמה'", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // validate the EditText
    private boolean validateForm(String email, String password) {
        boolean valid = true;

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

        return valid;
    }

    // create share button on action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);

    }

    // the action of the share button in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share_menu:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "שתפו את האפליקצייה : https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                startActivity(Intent.createChooser(shareIntent, "שתף את האפליקצייה"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

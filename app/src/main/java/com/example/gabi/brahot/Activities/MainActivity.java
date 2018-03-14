package com.example.gabi.brahot.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabi.brahot.Classes.Users;
import com.example.gabi.brahot.FireBase.LoginActivity;
import com.example.gabi.brahot.FireBase.RegisterActivity;
import com.example.gabi.brahot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText nameET, parentsET;
    private TextView nameTV;
    private String name, parents, blessing, blessing2, blessing3, userID;
    private RadioGroup radioGroup;
    private RadioButton male, female, selectedRadioButton;
    private Button successB, fareB, healthB, virtueB, pairingB, newB;
    // private TextView successTV, fareTV, healthTV, virtueTV, pairingTV;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.activity_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar.setBackgroundDrawable(getDrawable(R.drawable.action_bar_logo));
        }
        /*TextView actionB = (TextView) findViewById(R.id.action_bar_first_title_view);
        actionB.setText(R.string.app_name);*/

        mAuth = FirebaseAuth.getInstance();


        // check if the user not signed in, if ture start LoginActivity
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Toast.makeText(MainActivity.this, "Successfuly signed in", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Successfuly signed out", Toast.LENGTH_SHORT).show();
                }
            }
        };

        nameET = (EditText) findViewById(R.id.activity_main_name_edit_text);
        parentsET = (EditText) findViewById(R.id.activity_main_parent_edit_text);
        nameTV = (TextView) findViewById(R.id.activity_main_you_text_view);

        radioGroup = (RadioGroup) findViewById(R.id.activity_main_radio_group);
        male = (RadioButton) findViewById(R.id.activity_main_male_radio_button);
        female = (RadioButton) findViewById(R.id.activity_main_female_radio_button);

        // TODO: 3/13/2018 NOT WORKING
        /*FirebaseUser u = mAuth.getCurrentUser();
        userID = u.getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Users user = new Users();
                    user.setName(ds.child(userID).getValue(Users.class).getName());
                    nameTV.setText(user.getName());
                    Toast.makeText(MainActivity.this, user.getName() , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "The read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });*/

        successB = (Button) findViewById(R.id.activity_main_success_button);
        successB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString().trim();
                parents = parentsET.getText().toString().trim();
                String title = successB.getText().toString();
                selectedRadioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                blessing = (String) getText(R.string.success_bless);
                if (name.isEmpty() || parents.isEmpty() || !male.isChecked() && !female.isChecked()) {
                    valid(name, parents);
                } else {
                    String yourVote = selectedRadioButton.getText().toString();
                    Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                    intent.putExtra("backGround", R.drawable.background_success);
                    intent.putExtra("actionT",  "ברכת " + title);
                    intent.putExtra("type", title);
                    intent.putExtra("for", "ל" + name + " ");
                    intent.putExtra("radioChosen", yourVote + " ");
                    intent.putExtra("parents", parents);
                    intent.putExtra("bless", blessing);
                    startActivity(intent);
                }
            }
        });

        fareB = (Button) findViewById(R.id.activity_main_fare_button);
        fareB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString().trim();
                parents = parentsET.getText().toString().trim();
                String title = fareB.getText().toString();
                selectedRadioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                blessing = (String) getText(R.string.fare_bless);
                if (name.isEmpty() || parents.isEmpty() || !male.isChecked() && !female.isChecked()) {
                    valid(name, parents);
                } else {
                    String yourVote = selectedRadioButton.getText().toString();
                    Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                    intent.putExtra("backGround", R.drawable.background_fare);
                    intent.putExtra("actionT",  "ברכת " + title);
                    intent.putExtra("type", title);
                    intent.putExtra("for", "ל" + name + " ");
                    intent.putExtra("radioChosen", yourVote + " ");
                    intent.putExtra("parents", parents);
                    intent.putExtra("bless", blessing);
                    startActivity(intent);
                }
            }
        });

        healthB = (Button) findViewById(R.id.activity_main_health_button);
        healthB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString().trim();
                parents = parentsET.getText().toString().trim();
                String title = healthB.getText().toString();
                selectedRadioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                blessing = (String) getText(R.string.health1_bless);
                blessing2 = (String) getText(R.string.health2_bless);
                blessing3 = (String) getText(R.string.health3_bless);
                if (name.isEmpty() || parents.isEmpty() || !male.isChecked() && !female.isChecked()) {
                    valid(name, parents);
                } else {
                    String yourVote = selectedRadioButton.getText().toString();
                    Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                    intent.putExtra("backGround", R.drawable.background_health);
                    intent.putExtra("actionT",  "ברכת " + title);
                    intent.putExtra("type", title);
                    intent.putExtra("for", "ל" + name + " ");
                    intent.putExtra("radioChosen", yourVote + " ");
                    intent.putExtra("parents", parents);
                    intent.putExtra("bless", blessing + " " + name + " " + blessing2 + " " + name + " " + blessing3);
                    startActivity(intent);
                }
            }
        });

        virtueB = (Button) findViewById(R.id.activity_main_virtue_button);
        virtueB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString().trim();
                parents = parentsET.getText().toString().trim();
                String title = virtueB.getText().toString();
                selectedRadioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                blessing = (String) getText(R.string.virtue_bless);
                if (name.isEmpty() || parents.isEmpty() || !male.isChecked() && !female.isChecked()) {
                    valid(name, parents);
                } else {
                    String yourVote = selectedRadioButton.getText().toString();
                    Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                    intent.putExtra("backGround", R.drawable.background_virtue);
                    intent.putExtra("actionT",  "ברכת " + title);
                    intent.putExtra("type", title);
                    intent.putExtra("for", "ל" + name + " ");
                    intent.putExtra("radioChosen", yourVote + " ");
                    intent.putExtra("parents", parents);
                    intent.putExtra("bless", blessing);
                    startActivity(intent);
                }
            }
        });

        pairingB = (Button) findViewById(R.id.activity_main_pairing_button);
        pairingB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString().trim();
                parents = parentsET.getText().toString().trim();
                String title = pairingB.getText().toString();
                selectedRadioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                blessing = (String) getText(R.string.pairing_man_bless);
                blessing2 = (String) getText(R.string.pairing_woman_bless);
                if (name.isEmpty() || parents.isEmpty() || !male.isChecked() && !female.isChecked()) {
                    valid(name, parents);
                } else {
                    if (male.isChecked()) {
                        String yourVote = selectedRadioButton.getText().toString();
                        Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                        intent.putExtra("backGround", R.drawable.background_boy);
                        intent.putExtra("actionT",  "ברכת " + title + " לבן");
                        intent.putExtra("type", title + " ");
                        intent.putExtra("sex", "לבן");
                        intent.putExtra("for", "ל" + name + " ");
                        intent.putExtra("radioChosen", yourVote + " ");
                        intent.putExtra("parents", parents);
                        intent.putExtra("bless", blessing);
                        startActivity(intent);
                    } else {
                        String yourVote = selectedRadioButton.getText().toString();
                        Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                        intent.putExtra("backGround", R.drawable.background_girl);
                        intent.putExtra("actionT",  "ברכת " + title + " לבת");
                        intent.putExtra("type", title + " ");
                        intent.putExtra("sex", "לבת");
                        intent.putExtra("for", "ל" + name + " ");
                        intent.putExtra("radioChosen", yourVote + " ");
                        intent.putExtra("parents", parents);
                        intent.putExtra("bless", blessing2);
                        startActivity(intent);
                    }
                }
            }
        });

        newB = (Button) findViewById(R.id.activity_main_new_button);
        newB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString().trim();
                if (name.isEmpty()) {
                    validNew(name);
                } else {
                    Intent intent = new Intent(MainActivity.this, CustomActivity.class);
                    intent.putExtra("for", "ל" + name );
                    startActivity(intent);
                }
            }
        });
    }

    // valid name and parents if written
    private boolean valid(String name, String parents) {
        boolean valid = true;

        if (name.isEmpty()) {
            valid = false;
            nameET.setError("נא הזן את השם...");
        }else{
            nameET.setError(null);
        }

        if (parents.isEmpty()) {
            valid = false;
            parentsET.setError("נא הזן את שם האם או האב...");
        }else{
            parentsET.setError(null);
        }

        if (!male.isChecked() && !female.isChecked()) {
            valid = false;
            female.setError("נא לחץ על אחת האפשרויות");
        }else{
            female.setError(null);
        }

        return valid;
    }

    // valid name to new message
    private boolean validNew(String name) {
        boolean valid = true;

        if (name.isEmpty()) {
            valid = false;
            nameET.setError("נא הזן את השם...");
        }else{
            nameET.setError(null);
        }

        return valid;
    }

    // create share button and signout button on action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    // the action of the share button and signout button in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mShare:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "שתפו את האפליקצייה : https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                startActivity(Intent.createChooser(shareIntent, "שתף את האפליקצייה"));
                break;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

}

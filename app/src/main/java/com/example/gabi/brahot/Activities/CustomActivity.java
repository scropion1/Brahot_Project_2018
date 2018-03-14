package com.example.gabi.brahot.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabi.brahot.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class CustomActivity extends AppCompatActivity {

    private TextView brahaTV, forTV, counterChar;
    private EditText customET, titleCustomET;
    private Button shareBtn, whatsappBtn;
    private String blessing, titleCustom, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.activity_bar);
        TextView actionB = (TextView) findViewById(R.id.action_bar_first_title_view);
        actionB.setText("ברכה אישית");

        // set a back button on tollbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://brahot-57b54.appspot.com");

        brahaTV = (TextView) findViewById(R.id.activity_custom_braha_text_view);
        forTV = (TextView) findViewById(R.id.activity_custom_for_text_view);
        counterChar = (TextView) findViewById(R.id.activity_custom_count_text_view);
        customET = (EditText) findViewById(R.id.activity_custom_text_edit_text);
        titleCustomET = (EditText) findViewById(R.id.activity_custom_title_edit_text);

        Intent in2 = getIntent();
        final String forHwo = in2.getStringExtra("for");
        forTV.setText(forHwo);

        customET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = customET.getText().toString().trim();
                int symbols = text.length();
                counterChar.setText(symbols + "/400");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // share button text to any app in the smartphone
        shareBtn = (Button) findViewById(R.id.activity_custom_share_button);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = brahaTV.getText().toString().trim();
                titleCustom = titleCustomET.getText().toString().trim();
                blessing = customET.getText().toString().trim();
                if (titleCustom.isEmpty() || blessing.isEmpty()) {
                    valid(titleCustom, blessing);
                } else {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + titleCustom + "\n" + forHwo + "\n" + blessing + "\n" + "\n" + "הברכה שותפה באמצעות אפלייקצית 'ברך אותי' להורדת האפליקציה : https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                    startActivity(Intent.createChooser(shareIntent, "שתף את הברכה"));
                }

            }
        });

        // share button image to whatsapp and upload image in firebase storage
        whatsappBtn = (Button) findViewById(R.id.activity_custom_share_pic_button);
        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = brahaTV.getText().toString().trim();
                titleCustom = titleCustomET.getText().toString().trim();
                blessing = customET.getText().toString().trim();
                customET.hasFocus();
                if (titleCustom.isEmpty() || blessing.isEmpty()) {
                    valid(titleCustom, blessing);
                } else {
                    counterChar.setVisibility(View.GONE);
                    Bitmap bitmap = takeScreenshot();
                    saveBitmap(bitmap);
                    uploadFile(bitmap);

                    String photoPath = Environment.getExternalStorageDirectory() + "/Brahot/braha.png";
                    Uri filePath = Uri.parse(photoPath);

                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("image/png");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, filePath);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "הברכה שותפה באמצעות אפלייקצית 'ברך אותי' להורדת האפליקציה : https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }
            }

        });

    }

    // uploading the image to Firebase Storage
    private void uploadFile(Bitmap bitmap) {
        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://brahot-57b54.appspot.com");
        StorageReference mountainImagesRef = storageRef.child("images/" + "(" + currentDateTimeString + ")" + title + titleCustom + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CustomActivity.this, "Upload Failed -> ", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String photoPath = Environment.getExternalStorageDirectory() + "/Brahot/braha.png";
                Uri downloadUrl = Uri.parse(photoPath);
                Toast.makeText(CustomActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
            }
        });

    }

    // check if whatsapp is installed
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    // screen shot
    public Bitmap takeScreenshot() {

        View view = findViewById(R.id.activity_custom_linear);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;

    }


    // save bitmap on external storage
    public void saveBitmap(Bitmap bitmap) {

        File dir = new File(Environment.getExternalStorageDirectory() + "/Brahot");
        if (!dir.exists()) {
            Boolean result = dir.mkdir();
        }
        try {
            File file = new File(dir, "braha.png");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
            Toast.makeText(this, "התמונה נשמרה", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    // valid title and text if written
    private boolean valid(String titleCustom, String blessing) {
        boolean valid = true;

        if (titleCustom.isEmpty()) {
            valid = false;
            titleCustomET.setError("נא הזן את שם הברכה...");
        }else{
            titleCustomET.setError(null);
        }

        if (blessing.isEmpty()) {
            valid = false;
            customET.setError("נא הזן את הברכה...");
        }else{
            customET.setError(null);
        }

        return valid;
    }

    // back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

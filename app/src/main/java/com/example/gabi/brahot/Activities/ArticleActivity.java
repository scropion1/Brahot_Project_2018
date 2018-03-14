package com.example.gabi.brahot.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabi.brahot.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;


public class ArticleActivity extends AppCompatActivity {

    private TextView brahaTV, forTV, genderTV, parentsTV, blessTV, sexTV;
    private String title, sex, background;
    private ScrollView scrollView;
    private Button shareBtn, whatsappBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.activity_bar);
        Intent inTitle = getIntent();
        String t = inTitle.getStringExtra("actionT");
        TextView actionB = (TextView) findViewById(R.id.action_bar_first_title_view);
        actionB.setText(t);

        // set a back button on tollbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://brahot-57b54.appspot.com");

        brahaTV = (TextView) findViewById(R.id.activity_article_braha_text_view);
        sexTV = (TextView) findViewById(R.id.activity_article_sex_text_view);
        forTV = (TextView) findViewById(R.id.activity_article_for_text_view);
        genderTV = (TextView) findViewById(R.id.activity_article_gender_text_view);
        parentsTV = (TextView) findViewById(R.id.activity_article_parents_text_view);
        blessTV = (TextView) findViewById(R.id.activity_article_text_text_view);
        scrollView = (ScrollView) findViewById(R.id.activity_article_scroll_view);

        // get image from intent and set on scrollview as a background
        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            return;
        }
        int res = extras.getInt("backGround");
        scrollView.setBackgroundResource(res);


        Intent in = getIntent();
        title = in.getStringExtra("type");
        brahaTV.setText(title);

        Intent in2 = getIntent();
        sex = in2.getStringExtra("sex");
        sexTV.setText(sex);

        Intent in3 = getIntent();
        final String forHwo = in3.getStringExtra("for");
        forTV.setText(forHwo);

        Intent in4 = getIntent();
        final String gender = in4.getStringExtra("radioChosen");
        genderTV.setText(gender);

        Intent in5 = getIntent();
        final String parents = in5.getStringExtra("parents");
        parentsTV.setText(parents);

        Intent in6 = getIntent();
        final String blessing = in6.getStringExtra("bless");
        blessTV.setText(blessing);

        // share button text to any app in the smartphone
        shareBtn = (Button) findViewById(R.id.activity_article_share_button);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sex != null) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + sex + "\n" + forHwo + gender + parents + "\n" + blessing + "\n" + "\n" + "הברכה שותפה באמצעות אפלייקצית 'ברך אותי' להורדת האפליקציה : https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                    startActivity(Intent.createChooser(shareIntent, "שתף את הברכה"));
                }else{
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + "\n" + forHwo + gender + parents + "\n" + blessing + "\n" + "\n" + "הברכה שותפה באמצעות אפלייקצית 'ברך אותי' להורדת האפליקציה : https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                    startActivity(Intent.createChooser(shareIntent, "שתף את הברכה"));
                }

            }
        });

        // share button image to whatsapp and upload image in firebase storage
        whatsappBtn = (Button) findViewById(R.id.activity_article_share_pic_button);
        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bitmap = takeScreenshot();
                saveBitmap(bitmap);
                uploadFile(bitmap);

                String photoPath = Environment.getExternalStorageDirectory() + "/Brahot/braha.png";
                Uri filePath= Uri.parse(photoPath);

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("image/png");
                sendIntent.putExtra(Intent.EXTRA_STREAM, filePath);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "הברכה שותפה באמצעות אפלייקצית 'ברך אותי' להורדת האפליקציה : https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
                }

        });

    }

    // uploading the image to Firebase Storage
    private void uploadFile(Bitmap bitmap) {
        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
        Intent ini = getIntent();
        sex = ini.getStringExtra("sex");
        sexTV.setText(sex);
        if (sex != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://brahot-57b54.appspot.com");
            StorageReference mountainImagesRef = storageRef.child("images/" + "(" + currentDateTimeString + ")" + title + sex + ".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = mountainImagesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(ArticleActivity.this, "Upload Failed -> ", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    String photoPath = Environment.getExternalStorageDirectory() + "/Brahot/braha.png";
                    Uri downloadUrl = Uri.parse(photoPath);
                    Toast.makeText(ArticleActivity.this, "התמונה הועלת לשרת", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://brahot-57b54.appspot.com");
            StorageReference mountainImagesRef = storageRef.child("images/" + "(" + currentDateTimeString + ")" + title + ".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = mountainImagesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(ArticleActivity.this, "Upload Failed -> ", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    String photoPath = Environment.getExternalStorageDirectory() + "/Brahot/braha.png";
                    Uri downloadUrl = Uri.parse(photoPath);
                    Toast.makeText(ArticleActivity.this, "התמונה הועלת לשרת", Toast.LENGTH_LONG).show();
                }
            });
        }

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

        View view = findViewById(R.id.activity_article_scroll_view);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
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

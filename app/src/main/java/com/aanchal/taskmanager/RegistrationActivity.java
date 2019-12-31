package com.aanchal.taskmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView imgview;
    private Button btnregister;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        imgview=findViewById(R.id.imgview);

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowserImage();
            }
        });

    }

    private void BrowserImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            if (data==null){
                Toast.makeText(this, "please select picture", Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri =data.getData();
        imagePath=getRealPathFromUri(uri);
        imgview.setImageURI(uri);
    }

    private String getRealPathFromUri(Uri uri) {

        String[] projection ={MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor =loader.loadInBackground();
        int colIndex=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }
}

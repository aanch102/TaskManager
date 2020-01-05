package com.aanchal.taskmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.aanchal.taskmanager.api.UsersAPI;
import com.aanchal.taskmanager.model.Users;
import com.aanchal.taskmanager.serverResponse.ImageResponse;
import com.aanchal.taskmanager.serverResponse.SignUpResponse;
import com.aanchal.taskmanager.strictmode.StrictModeClass;
import com.aanchal.taskmanager.url.Url;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView imgview;
    private Button btnregister;
    private EditText etfirst,etsecond,etusername,etpass,etconpass;
    String imagePath;
    private String imageName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

       imgview=findViewById(R.id.imgview);
        btnregister=findViewById(R.id.btnregister);
        etfirst=findViewById(R.id.etfirst);
        etsecond=findViewById(R.id.etsecond);
        etpass=findViewById(R.id.etpass);
        etusername=findViewById(R.id.etusername);
        etconpass=findViewById(R.id.etconpass);


        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowserImage();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etpass.getText().toString().equals(etconpass.getText().toString())) {
                    if (validate()) {
                        save();
                        saveImageOnly();

                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "", Toast.LENGTH_SHORT).show();
                    etpass.requestFocus();
                    return;
                }
            }

        });

    }

    private boolean validate() {
        boolean status=true;
        if (etusername.getText().toString().length() < 6) {
            etusername.setError("Minimum 6 character");
            status=false;
        }
        return status;
    }

    private void save(){
        String firstName = etfirst.getText().toString();
        String secondName = etsecond.getText().toString();
        String username = etusername.getText().toString();
        String password=etpass.getText().toString();

       Users users=new Users (firstName,secondName,username,password,imageName);
      /* Retrofit retrofit=new Retrofit.Builder()
               .baseUrl(Url.base_url)
               .addConverterFactory(GsonConverterFactory.create())
               .build();*/

       UsersAPI usersAPI =Url.getInstance().create(UsersAPI.class);

      Call<SignUpResponse> usersCall =usersAPI.registrationUser(users);


       usersCall.enqueue(new Callback<SignUpResponse>() {
           @Override
           public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
               if (!response.isSuccessful()){
                   Toast.makeText(RegistrationActivity.this, "code"+response.code(), Toast.LENGTH_SHORT).show();
                   return;
               }
               Toast.makeText(RegistrationActivity.this, "successfully added", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onFailure(Call<SignUpResponse> call, Throwable t) {
               Toast.makeText(RegistrationActivity.this, "error"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

    private void saveImageOnly() {
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile",
                file.getName(), requestBody);

        UsersAPI usersAPI = Url.getInstance().create(UsersAPI.class);
        Call<ImageResponse> responseBodyCall = usersAPI.uploadImage(body);

        StrictModeClass.StrictMode();
        //Synchronous method
        try {
            Response<ImageResponse> imageResponseResponse = responseBodyCall.execute();
            imageName = imageResponseResponse.body().getFilename();
            Toast.makeText(this, "Image inserted" + imageName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

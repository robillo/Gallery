package com.appbusters.robinkamboj.gallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE=1;
    private static final int MY_PERMISSIONS= 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText title= (EditText) findViewById(R.id.title);
        EditText subTitle= (EditText) findViewById(R.id.subTitle);
        //Auto Capitalize all letters of EditTexts
        title.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        subTitle.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!hasPermissions(this, permissions)){
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS);
        }

    }


    //Permissions Helper Method
    public static boolean hasPermissions(Context context, String[] permissions){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            for(String permission : permissions){
                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!= null){
            Uri selectedImage= data.getData();
            String[] filePathColumn= { MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query( selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex= cursor.getColumnIndex(filePathColumn[0]);
            String picturePath= cursor.getString(columnIndex);
            cursor.close();

            //String picturePath contains the path of the selected image.
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            imageView.setScaleX((float) 1.0);
            imageView.setScaleY((float) 1.0);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    public void buttonOnClick(View v) {
        switch (v.getId()) {
            case R.id.fabL2:
            {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                break;
            }
            case R.id.fabL:
            {
                break;
            }
            case R.id.fabR:
            {
                break;
            }
            case R.id.fabR2:
            {
                break;
            }
        }
    }
}

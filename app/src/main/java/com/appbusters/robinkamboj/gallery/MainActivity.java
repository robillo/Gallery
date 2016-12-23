package com.appbusters.robinkamboj.gallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private static int GALLERY_IMAGE=1;
    private static int CAMERA_IMAGE=2;

    private static final int MY_PERMISSIONS= 123;
    private Boolean FabClosed=true;
    private Animation rotate_forward, rotate_backward, fab_open, fab_close;
    private FloatingActionButton fab, fabL, fabR, fabL2, fabR2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText title= (EditText) findViewById(R.id.title);
        EditText subTitle= (EditText) findViewById(R.id.subTitle);
        //Auto Capitalize all letters of EditTexts
        title.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        subTitle.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if(!hasPermissions(this, permissions)){
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS);
        }

        fab= (FloatingActionButton) findViewById(R.id.fab);
        fabL= (FloatingActionButton) findViewById(R.id.fabL);
        fabR= (FloatingActionButton) findViewById(R.id.fabR);
        fabL2= (FloatingActionButton) findViewById(R.id.fabL2);
        fabR2= (FloatingActionButton) findViewById(R.id.fabR2);
        fab_open= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_backward= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        rotate_forward= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        Animation fab_close_initial= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close_initial);
        fabL.setAnimation(fab_close_initial);
        fabR.setAnimation(fab_close_initial);
        fabL2.setAnimation(fab_close_initial);
        fabR2.setAnimation(fab_close_initial);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatefab();
            }
        });
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


        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        if(requestCode== GALLERY_IMAGE && resultCode==RESULT_OK && data!= null){
            Uri selectedImage= data.getData();
            String[] filePathColumn= { MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query( selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex= cursor.getColumnIndex(filePathColumn[0]);
            String picturePath= cursor.getString(columnIndex);
            cursor.close();

            //String picturePath contains the path of the selected image.
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        else if(requestCode== CAMERA_IMAGE && resultCode==RESULT_OK && data!=null){
            Bundle extras= data.getExtras();
            Bitmap imageBitmap= (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

        imageView.setScaleX((float) 1.0);
        imageView.setScaleY((float) 1.0);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private void animatefab() {

        if(FabClosed){
            fab.startAnimation(rotate_forward);
            fabL.startAnimation(fab_open);
            fabR.startAnimation(fab_open);
            fabL2.setAnimation(fab_open);
            fabR2.setAnimation(fab_open);
            FabClosed=false;
        }
        else{
            fab.startAnimation(rotate_backward);
            fabL.startAnimation(fab_close);
            fabR.startAnimation(fab_close);
            fabL2.setAnimation(fab_close);
            fabR2.setAnimation(fab_close);
            FabClosed=true;
        }
    }

    public void buttonOnClick(View v) {
        switch (v.getId()) {
            case R.id.fabL2:
            {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLERY_IMAGE);
                break;
            }
            case R.id.fabL:
            {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, CAMERA_IMAGE);
                }
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

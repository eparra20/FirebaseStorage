package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity {

    private Button buttonTomarFoto;
    private ImageView imageViewFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTomarFoto = findViewById(R.id.buttonTomarFoto);
        imageViewFoto = findViewById(R.id.imageViewFoto);



        buttonTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aca es donde le vamos a decir que abra la camara
                EasyImage.openChooserWithGallery(MainActivity.this,"Gatitos App quiere ",1);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                //el codigo de glide o lo que sea donde hago que se coloque la imagen en el imageView
                onPhotosReturned(imageFiles);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                super.onCanceled(source, type);
                Toast.makeText(MainActivity.this, "Cobarde, toma la foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onPhotosReturned(List<File> imageFiles){
        Toast.makeText(this, "Foto tomada correctamente", Toast.LENGTH_SHORT).show();
        Glide.with(this).load(imageFiles.get(0)).into(imageViewFoto);

    }
}

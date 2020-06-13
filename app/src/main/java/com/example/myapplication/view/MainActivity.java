package com.example.myapplication.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.controller.PersonaController;
import com.example.myapplication.model.Persona;
import com.example.myapplication.util.ResultListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends AppCompatActivity {

    private Button buttonTomarFoto;
    private ImageView imageViewFoto;
    private FirebaseStorage storage;
    private Button buttonBajarFoto;
    private Button buttonAddPerson;
    private Button buttonReadList;

    private ProgressBar progressBar;
    private PersonaController personaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonTomarFoto = findViewById(R.id.buttonTomarFoto);
        imageViewFoto = findViewById(R.id.imageViewFoto);
        buttonBajarFoto = findViewById(R.id.buttonBajarFoto);
        progressBar = findViewById(R.id.progressBar);
        buttonAddPerson = findViewById(R.id.buttonAddPerson);
        buttonReadList = findViewById(R.id.buttonLeerList);
        personaController = new PersonaController();

        storage = FirebaseStorage.getInstance();


        buttonTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aca es donde le vamos a decir que abra la camara
                EasyImage.openCameraForVideo(MainActivity.this,1);
            }
        });

        buttonBajarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bajarFoto();
            }
        });

        buttonAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Persona persona = new Persona("Robin", "Willians", "48");

                personaController.addPerson(persona, new ResultListener<Persona>() {
                    @Override
                    public void onFinish(Persona result) {
                        Toast.makeText(MainActivity.this, "Todo bien "+result.getNombre(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        buttonReadList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               personaController.getPersons(new ResultListener<List<Persona>>() {
                   @Override
                   public void onFinish(List<Persona> result) {
                       Toast.makeText(MainActivity.this, result.get(0).getNombre(), Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onError(String message) {
                       Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                   }
               });
            }
        });

    }

    private void bajarFoto() {
        StorageReference reference = storage.getReference().child("gatitos/gatito_egipcio.jpg");
        Glide.with(this).load(reference).into(imageViewFoto);
    }

    private void subirFoto(File file){

        Uri uriFile = Uri.fromFile(file);

        StorageReference storageRef = storage.getReference().child("gatitos/gatito_egipcio.jpg");
        UploadTask uploadTask = storageRef.putFile(uriFile);

        progressBar.setVisibility(View.VISIBLE);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(MainActivity.this, "Perdon no pude procesar el video", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "Archivo subida correctamente", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                Long max = taskSnapshot.getTotalByteCount();
                Long bytesTransferred = taskSnapshot.getBytesTransferred();

                progressBar.setMax(max.intValue());
                progressBar.setProgress(bytesTransferred.intValue());
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
        //Glide.with(this).load(imageFiles.get(0)).into(imageViewFoto);
        subirFoto(imageFiles.get(0));

    }
}

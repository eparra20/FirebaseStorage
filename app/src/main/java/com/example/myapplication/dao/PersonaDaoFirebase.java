package com.example.myapplication.dao;


import androidx.annotation.NonNull;

import com.example.myapplication.model.Persona;
import com.example.myapplication.util.ResultListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PersonaDaoFirebase {

    public static final String COLLECTION_NAME = "personas";
    public static final String NOMBRE = "nombre";
    private FirebaseFirestore instance;
    private CollectionReference reference;

    public PersonaDaoFirebase() {
        instance = FirebaseFirestore.getInstance();
        reference = instance.collection(COLLECTION_NAME);
    }


    public void addPerson(final Persona persona, final ResultListener<Persona> resultListener) {
        reference.add(persona)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    resultListener.onFinish(persona);
                }
            }).addOnFailureListener(new OnFailureListener() {
                 @Override
                public void onFailure(@NonNull Exception e) {
                     resultListener.onError("Ha ocurrido un error al intentar guardar la persona");
                }
            });
    }


    public void getPersonas(final ResultListener<List<Persona>> resultListener) {

        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Persona> personas = queryDocumentSnapshots.toObjects(Persona.class);
                resultListener.onFinish(personas);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError("Error al consultar las personas");
            }
        });
    }

    public void getPersonasByName(String name,final ResultListener<List<Persona>> resultListener) {

        reference.whereEqualTo(NOMBRE,name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Persona> personas = queryDocumentSnapshots.toObjects(Persona.class);
                resultListener.onFinish(personas);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultListener.onError("Error al consultar las personas");
            }
        });


    }


}

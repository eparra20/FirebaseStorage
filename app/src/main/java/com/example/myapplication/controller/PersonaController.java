package com.example.myapplication.controller;

import android.app.Person;

import com.example.myapplication.dao.PersonaDaoFirebase;
import com.example.myapplication.model.Persona;
import com.example.myapplication.util.ResultListener;

import java.util.List;

public class PersonaController {

    private PersonaDaoFirebase personaDaoFirebase;
    //es el DAO de la entidad persona para base de datos
    private Object personaDaoRoom;

    public PersonaController() {
        this.personaDaoFirebase = new PersonaDaoFirebase();
    }


    public void addPerson(Persona persona, final ResultListener<Persona> resultListenerFromView){

        personaDaoFirebase.addPerson(persona, new ResultListener<Persona>() {
            @Override
            public void onFinish(Persona result) {
                resultListenerFromView.onFinish(result);
            }

            @Override
            public void onError(String message) {
                resultListenerFromView.onError(message);
            }
        });

    }


    ///todo como ahjcemos con la lista.
    public void addPerson(List<Persona> personas, final ResultListener<Persona> resultListenerFromView){

        for (Persona persona : personas) {
            personaDaoFirebase.addPerson(persona, new ResultListener<Persona>() {
                @Override
                public void onFinish(Persona result) {
                    resultListenerFromView.onFinish(result);
                }

                @Override
                public void onError(String message) {
                    resultListenerFromView.onError(message);
                }
            });
        }

    }


    public void getPersons(final ResultListener<List<Persona>> resultListenerFromView){

        personaDaoFirebase.getPersonas(new ResultListener<List<Persona>>() {
            @Override
            public void onFinish(List<Persona> result) {
                resultListenerFromView.onFinish(result);
            }

            @Override
            public void onError(String message) {
                resultListenerFromView.onError(message);
            }
        });
    }

    public void getPersonByName(){
        personaDaoFirebase.getPersonasByName("Edaurdo", new ResultListener<List<Persona>>() {
            @Override
            public void onFinish(List<Persona> result) {

            }

            @Override
            public void onError(String message) {

            }
        });
    }

}

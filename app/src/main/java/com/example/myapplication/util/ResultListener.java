package com.example.myapplication.util;

public interface ResultListener<T> {

  public void onFinish(T result);
  public void onError(String message);

}

package com.krish.rentmate.repositories;

public interface RepositoryCallback<T> {
    void onSuccess(T result);
    void onError(String errorMessage);
}
package org.example.currency_conversion.dao;


import java.util.List;


public interface Dao<T> {

    T add(T entity);

    List<T> findAll();
}

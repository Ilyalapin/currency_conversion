package org.example.demo123.dao;

import org.example.demo123.Currency;

import java.util.List;
import java.util.Optional;

public interface Dao<T, ID> {
 Currency addCurrency(T currency);
 Optional<T> updateCurrency(T currency);
 void removeCurrency(ID id);
 Optional<T> findById(ID id);
 List<T> findAll();
}

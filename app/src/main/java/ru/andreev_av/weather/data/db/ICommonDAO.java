package ru.andreev_av.weather.data.db;

import java.sql.SQLException;
import java.util.List;

// описывает общие действия с БД для всех объектов
public interface ICommonDAO<T> {

    List<T> getAll();

    T get(long id);

    boolean add(T object) throws SQLException;

    boolean update(T object) throws SQLException;// boolean - чтобы удостовериться, что операция прошла успешно

    boolean delete(T object) throws SQLException;
}

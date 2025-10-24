package ma.projet.util;

import java.util.List;

public interface IHibernateUtil<T> {
    T create(T o);
    boolean delete(T o);
    T update(T o);
    T findById(int id);
    List<T> findAll();
}
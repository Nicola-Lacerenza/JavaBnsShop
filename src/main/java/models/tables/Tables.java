package models.tables;

import models.Oggetti;

import java.util.List;
import java.util.Optional;

public interface Tables <E extends Oggetti<E>> {

    boolean  insertElement(E oggetto);

    boolean updateElement(E oggetto,int id);

    boolean deleteElement(int id);

    Optional<E> getElement(int id);

    List<E> getAllElements();
}

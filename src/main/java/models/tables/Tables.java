package models.tables;

import models.Oggetti;

public interface Tables <E extends Oggetti<E>> {

    boolean  insertElement(E oggetto);

    boolean updateElement(E oggetto,int id);

    boolean deleteElement(int id);

    boolean getElement(int id);

    boolean getAllElements();
}

package models.tables;

import models.Oggetti;

public interface Tables <E extends Oggetti<E>> {

    boolean  insertElement(E oggetto);

    boolean updateElement(E oggetto,String id);

    boolean deleteElement(String id);

    boolean getElement(String id);

    boolean getAllElements();
}

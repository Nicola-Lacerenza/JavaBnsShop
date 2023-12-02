package models.tables;

import models.Oggetti;

public interface Tables <E extends Oggetti<E>> {

    boolean  insertElement(E oggetto);
}

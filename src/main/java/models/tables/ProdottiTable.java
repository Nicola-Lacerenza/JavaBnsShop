package models.tables;

import models.Prodotti;
import utility.Database;

import java.util.HashMap;

public class ProdottiTable implements Tables <Prodotti> {

    @Override
    public boolean insertElement(Prodotti oggetto) {
        return Database.insertElement(new HashMap<>(),"PRODOTTI");
    }
}

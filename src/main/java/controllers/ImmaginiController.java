package controllers;

import models.Immagini;
import models.tables.ImmaginiTable;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImmaginiController implements Controllers<Immagini> {
    private ImmaginiTable tabella;
    public ImmaginiController(){
        this.tabella= new ImmaginiTable();
    }

    @Override
    public Optional<Immagini> insertObject(Map<String, String> request) {
        int idImmagini = Integer.parseInt(request.get("idimmagini"));
        int idProdotti = Integer.parseInt(request.get("idprodotti"));
        String url = String.valueOf(Integer.parseInt(request.get("url")));
        Immagini immagine = new Immagini(0,0,url);
        if (this.tabella.insertElement(immagine)){
            return Optional.of(immagine);
        }
        return Optional.empty();
    }

    @Override
    public boolean updateObject(Map<String, String> request) {
        int idImmagini = Integer.parseInt(request.get("idimmagini"));
        int idProdotti = Integer.parseInt(request.get("idprodotti"));
        String url = String.valueOf(Integer.parseInt(request.get("url")));
        Immagini immagine = new Immagini(idImmagini,idProdotti,url);
        return this.tabella.updateElement(immagine,idImmagini);
    }

    @Override
    public boolean deleteObject(int objectid) {
        return this.tabella.deleteElement(objectid);
    }

    @Override
    public Optional<Immagini> getObject(int objectid) {
        return this.tabella.getElement(objectid);
    }

    @Override
    public List<Immagini> getAllObjects() {
        return this.tabella.getAllElements();
    }
}

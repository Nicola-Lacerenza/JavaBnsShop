package controllers;

import models.ResiProdotti;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ResiProdottiController implements Controllers<ResiProdotti>{
    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {

        Map<Integer, QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        try{
            fields1.put(0,new QueryFields<>("id_utente",request.get("id_utente"),TipoVariabile.longNumber));
            fields1.put(1,new QueryFields<>("id_ordine",request.get("id_ordine"),TipoVariabile.longNumber));
            fields1.put(2,new QueryFields<>("motivo",request.get("motivo"),TipoVariabile.string));
            fields1.put(3,new QueryFields<>("importo",request.get("importo"),TipoVariabile.realNumber));
            fields1.put(4,new QueryFields<>("valuta",request.get("valuta"),TipoVariabile.string));
            fields1.put(5,new QueryFields<>("stato_reso","IN ATTESA DI APPROVAZIONE",TipoVariabile.string));
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
        boolean success = Database.executeTransaction(connection -> {
            int idReso = Database.insertElement(connection,"resi",fields1);
            if (idReso<=0){
                return false;
            }
            int idResoProdotto = Database.insertElement(connection,"resi_has_prodotti",fields1);
            return true;
        });
        return -1;
    }

    @Override
    public boolean updateObject(int id, Map<Integer, QueryFields<? extends Comparable<?>>> request) {
        return false;
    }

    @Override
    public boolean deleteObject(int objectid) {
        return false;
    }

    @Override
    public Optional<ResiProdotti> getObject(int objectid) {
        return Optional.empty();
    }

    @Override
    public List<ResiProdotti> getAllObjects() {
        return null;
    }

    @Override
    public List<ResiProdotti> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return null;
    }
}

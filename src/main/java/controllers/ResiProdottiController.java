package controllers;

import models.ResiProdotti;
import models.Taglia;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ResiProdottiController implements Controllers<ResiProdotti>{

    public int insertObject(List<Map<Integer, QueryFields<? extends Comparable<?>>>> request){


        Map<Integer, QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        try{
            fields1.put(0,new QueryFields<>("id_utente",request.getFirst().get(1), TipoVariabile.longNumber));
            fields1.put(1,new QueryFields<>("id_ordine",request.getFirst().get(0),TipoVariabile.longNumber));
            fields1.put(2,new QueryFields<>("motivo",request.getFirst().get(6),TipoVariabile.string));
            fields1.put(3,new QueryFields<>("importo",0,TipoVariabile.realNumber));
            fields1.put(4,new QueryFields<>("valuta",request.getFirst().get(7),TipoVariabile.string));
            fields1.put(5,new QueryFields<>("stato_reso","IN ATTESA DI APPROVAZIONE",TipoVariabile.string));
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }

        AtomicInteger idReso = new AtomicInteger();

        boolean success = Database.executeTransaction(connection -> {
            idReso.set(Database.insertElement(connection,"resi",fields1));
            if (idReso.get()<=0){
                return false;
            }

            for (int i = 0; i<request.size();i++){
                Map<Integer,QueryFields<? extends Comparable<?>>> campiFrontEnd = request.get(i);
                String queryTaglia = "SELECT * FROM taglia WHERE taglia_Eu = ?";
                Map<Integer, QueryFields<? extends Comparable<?>>> tmp = new HashMap<>();
                try{
                    tmp.put(0,new QueryFields<>("taglia_Eu",campiFrontEnd.get(3),TipoVariabile.string));
                }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }

                List<Taglia> taglie = Database.executeGenericQuery(connection,queryTaglia,tmp,new Taglia());
                if (taglie.isEmpty()){
                    return false;
                }

                Map<Integer, QueryFields<? extends Comparable<?>>> fields2 = new HashMap<>();
                try{
                    fields2.put(0,new QueryFields<>("id_reso",idReso.get(), TipoVariabile.longNumber));
                    fields2.put(1,new QueryFields<>("id_prodotto",campiFrontEnd.get(2),TipoVariabile.longNumber));
                    fields2.put(2,new QueryFields<>("id_taglia",taglie.getFirst().getId(),TipoVariabile.longNumber));
                    fields2.put(3,new QueryFields<>("quantita",campiFrontEnd.get(4),TipoVariabile.longNumber));
                    fields2.put(4,new QueryFields<>("prezzo_unitario",campiFrontEnd.get(5),TipoVariabile.realNumber));
                }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
                int idResoProdotto = Database.insertElement(connection,"resi_has_prodotti",fields2);
                if (idResoProdotto<=0){
                    return false;
                }
            }
            return true;
        });

        if (success && idReso.get()>0){
            return idReso.get();
        }
        return -1;

    }

    @Override
    public int insertObject(Map<Integer, QueryFields<? extends Comparable<?>>> request) {
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

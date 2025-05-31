package controllers;

import models.Resi;
import models.ResiProdotti;
import models.Taglia;
import utility.Database;
import utility.QueryFields;
import utility.TipoVariabile;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ResiProdottiController implements Controllers<ResiProdotti>{

    public int insertObject(List<Map<Integer, QueryFields<? extends Comparable<?>>>> request){

        double totale = 0.0;
        for (Map<Integer, QueryFields<? extends Comparable<?>>> map : request){
                Double prezzoUnitario = (Double) (map.get(5).getFieldValue());
                Integer quantita = (Integer) (map.get(4).getFieldValue());
                totale += (prezzoUnitario*quantita);
        }

        Map<Integer, QueryFields<? extends Comparable<?>>> fields1 = new HashMap<>();
        try{
            fields1.put(0,new QueryFields<>("id_utente",(Integer) request.getFirst().get(1).getFieldValue(), TipoVariabile.longNumber));
            fields1.put(1,new QueryFields<>("id_ordine", (Integer) request.getFirst().get(0).getFieldValue(),TipoVariabile.longNumber));
            fields1.put(2,new QueryFields<>("motivo", (String) request.getFirst().get(6).getFieldValue(),TipoVariabile.string));
            fields1.put(3,new QueryFields<>("importo",totale,TipoVariabile.realNumber));
            fields1.put(4,new QueryFields<>("valuta",(String) request.getFirst().get(7).getFieldValue(),TipoVariabile.string));
            fields1.put(5,new QueryFields<>("stato_reso","IN ATTESA DI APPROVAZIONE",TipoVariabile.string));
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }

        Map<Integer, QueryFields<? extends Comparable<?>>> fields2 = new HashMap<>();
        try{
           fields2.put(0,new QueryFields<>("stato_ordine","RICHIESTA RESO",TipoVariabile.string));
           fields2.put(1,new QueryFields<>("id",(Integer) request.getFirst().get(0).getFieldValue(),TipoVariabile.longNumber));
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
            String query1 = "UPDATE ordine SET stato_ordine = ?, data_aggiornamento_stato_ordine = NOW() WHERE id = ?";
            boolean risultatoQuery1 = Database.executeGenericUpdate(connection,query1,fields2);
            if (!risultatoQuery1){
                return false;
            }

            for (int i = 0; i<request.size();i++){
                Map<Integer,QueryFields<? extends Comparable<?>>> campiFrontEnd = request.get(i);
                String query2 = "SELECT * FROM taglia WHERE taglia_Eu = ?";
                Map<Integer, QueryFields<? extends Comparable<?>>> tmp = new HashMap<>();
                try{
                    tmp.put(0,new QueryFields<>("taglia_Eu",(String) campiFrontEnd.get(3).getFieldValue(),TipoVariabile.string));
                }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }

                List<Taglia> taglie = Database.executeGenericQuery(connection,query2,tmp,new Taglia());
                if (taglie.isEmpty()){
                    return false;
                }

                Map<Integer, QueryFields<? extends Comparable<?>>> fields3 = new HashMap<>();
                try{
                    fields3.put(0,new QueryFields<>("id_reso", idReso.get(), TipoVariabile.longNumber));
                    fields3.put(1,new QueryFields<>("id_prodotto",(Integer) campiFrontEnd.get(2).getFieldValue(),TipoVariabile.longNumber));
                    fields3.put(2,new QueryFields<>("id_taglia",taglie.getFirst().getId(),TipoVariabile.longNumber));
                    fields3.put(3,new QueryFields<>("quantita",(Integer) campiFrontEnd.get(4).getFieldValue(),TipoVariabile.longNumber));
                    fields3.put(4,new QueryFields<>("prezzo_unitario", (Double) campiFrontEnd.get(5).getFieldValue(),TipoVariabile.realNumber));
                }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
                int idResoProdotto = Database.insertElement(connection,"resi_has_prodotti",fields3);
                if (idResoProdotto<=0){
                    return false;
                }

                String query3 = "UPDATE dettagli_ordine SET stato_reso_prodotto = 'RESO_RICHIESTO' WHERE id_ordine = ? AND id_prodotto = ?";
                Map<Integer, QueryFields<? extends Comparable<?>>> fields4 = new HashMap<>();
                try{
                    fields4.put(0,new QueryFields<>("id_ordine", (Integer) campiFrontEnd.get(0).getFieldValue(), TipoVariabile.longNumber));
                    fields4.put(1,new QueryFields<>("id_prodotto",(Integer) campiFrontEnd.get(2).getFieldValue(),TipoVariabile.longNumber));
                   }catch (SQLException e){
                    e.printStackTrace();
                    return false;
                }
                boolean risultatoQuery2 = Database.executeGenericUpdate(connection,query3,fields4);
                if (!risultatoQuery2){
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
        return new LinkedList<>();
    }

    @Override
    public List<ResiProdotti> executeQuery(String query, Map<Integer, QueryFields<? extends Comparable<?>>> fields) {
        return null;
    }
}

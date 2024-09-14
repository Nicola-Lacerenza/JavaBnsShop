package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class Brand implements Oggetti<Brand>{
    private final int id;
    private final String nome;
    private final String descrizione;

    public Brand(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public Brand(){
        this(0,"","");
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public Brand createObject() {
        return new Brand();
    }

    @Override
    public Optional<Brand> convertDBToJava(ResultSet rs) {
        try{
            int id1 = rs.getInt("id");
            String nome1 = rs.getString("nome");
            String descrizione1 = rs.getString("descrizione");
            return Optional.of(new Brand(id1,nome1,descrizione1));
        }catch (SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}

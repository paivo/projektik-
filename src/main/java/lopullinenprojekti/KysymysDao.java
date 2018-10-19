/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lopullinenprojekti;

import java.util.*;
import java.sql.*;

/**
 *
 * @author Päivö Niska
 */
public class KysymysDao implements Dao<Kysymys, Integer> {

    private Database database;

    public KysymysDao(Database database) {
        this.database = database;
    }
     
    /**
     *
     * @return @throws SQLException
     */
    @Override
    public List<Kysymys> findAll() throws SQLException {
        List<Kysymys> kysymykset = new ArrayList();
        Connection connection = database.getConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kysymys");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Kysymys k = new Kysymys(rs.getString("kurssi"), rs.getString("aihe"), rs.getString("kysymysteksti"));
            k.setId(rs.getInt("id"));
            kysymykset.add(k);
        }
        rs.close();
        stmt.close();
        connection.close();

        if (kysymykset.isEmpty()) {
            return null;
        }

        return kysymykset;
    }

    @Override
    public void save(Kysymys kysymys) throws SQLException {
        if ( (kysymys.getKurssi().isEmpty() || kysymys.getAihe().isEmpty() || kysymys.getKysymysteksti().isEmpty()) ) {
            return;
        }
        if (findOne(kysymys)!=null) {
            return;
        }
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Kysymys (kurssi, aihe, kysymysteksti) VALUES (?, ?, ?)");
        stmt.setString(1, kysymys.getKurssi());
        stmt.setString(2, kysymys.getAihe());
        stmt.setString(3, kysymys.getKysymysteksti());

        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
    
    @Override
    public Kysymys findOne(Integer id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE id = ?");
            stmt.setInt(1, id);

            ResultSet result = stmt.executeQuery();
            result.next();
            Kysymys kysymys = new Kysymys( result.getString("kurssi"), result.getString("aihe"), result.getString("kysymysteksti"));
            kysymys.setId(id);
            return kysymys;
        }
    }
    
    @Override
    public Vastaus findOne(Kysymys kysymys) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE kurssi = ? AND aihe = ? AND kysymysteksti = ?");
            stmt.setString(1, kysymys.getKurssi());
            stmt.setString(2, kysymys.getAihe());
            stmt.setString(3, kysymys.getKysymysteksti());

            ResultSet result = stmt.executeQuery();
            result.next();
            Kysymys kysymys2 = new Kysymys( result.getString("kurssi"), result.getString("aihe"), result.getString("kysymysteksti"));
            kysymys2.setId(result.getInt("id"));
            return kysymys2;
        }
    }

    public void delete(Kysymys kysymys) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE kurssi = ? AND aihe = ? AND kysymysteksti = ?");
            stmt.setString(1, kysymys.getAihe());
            stmt.setString(2, kysymys.getKurssi());
            stmt.setString(3, kysymys.getKysymysteksti());
            stmt.executeUpdate();
        }
    }
    
    public void delete(Integer id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public List<Kysymys> getKysymykset(String kurssi, String aihe) throws SQLException {
        List<Kysymys> kysymykset = new ArrayList();
        for (Kysymys kysymys: findAll()){
            if ( ( kysymys.getAihe().equals(aihe) )&&( !kysymykset.contains(kysymys) )&&( kysymys.getKurssi().equals(kurssi)) ){
                //valitaan vain kyseisen aiheen kysymykset ja lisätään ne vain kerran
                kysymykset.add(kysymys);
            }
        }
        return kysymykset;
    }
    
    public List<Kysymys> findKysymysPerAihe(String kurssi) throws SQLException {
        List<Kysymys> kysymykset = new ArrayList();
        //Tarkistetaan onko kysymykset listassa jo kysymystä joilla sama kurssi.
        for(Kysymys kysymys: findAll()){
            int i = 0;
            for (Kysymys kysymys2: kysymykset){
                if (kysymys.getAihe().equals(kysymys2.getAihe())){
                    i=1;
                }
            }
            
            if ( (i==0) && (kysymys.getKurssi().equals(kurssi)) ){
                kysymykset.add(kysymys);
            }
        }
        
        return kysymykset;
    }
    
    public List<Kysymys> findKysymysPerKurssi() throws SQLException {
        List<Kysymys> kysymykset = new ArrayList();
        //Tarkistetaan onko kysymykset listassa jo kysymystä jolla sama kurssi.
        for(Kysymys kysymys: findAll()){
            int i = 0;
            for (Kysymys kysymys2: kysymykset){
                if (kysymys.getKurssi().equals(kysymys2.getKurssi())){
                    i++;
                }
            }
            if (i==0){
                kysymykset.add(kysymys);
            }
        }
        
        return kysymykset;
    }
}

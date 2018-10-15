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
        if (findOne(kysymys)) {
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
    public Boolean findOne(Kysymys kysymys) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Kysymys WHERE kurssi = ? AND aihe = ? AND kysymysteksti = ?");
            stmt.setString(1, kysymys.getKurssi());
            stmt.setString(2, kysymys.getAihe());
            stmt.setString(3, kysymys.getKysymysteksti());

            ResultSet result = stmt.executeQuery();
            return result.next();
        }
    }

    @Override
    public void delete(Kysymys kysymys) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Kysymys WHERE kurssi = ? AND aihe = ? AND kysymysteksti = ?");
            stmt.setString(1, kysymys.getAihe());
            stmt.setString(2, kysymys.getKurssi());
            stmt.setString(3, kysymys.getKysymysteksti());
        }
    }
    
    public List<String> getKysymykset(String aihe) throws SQLException {
        List<String> kysymykset = new ArrayList();
        for (Kysymys kysymys: findAll()){
            String teksti = kysymys.getKysymysteksti();
            if ( ( !teksti.isEmpty() )&&( kysymys.getAihe().equals(aihe) )&&( !kysymykset.contains(teksti) ) ){
                //valitaan vain kyseisen aiheen epätyhjät kysymykset ja lisätään ne vain kerran
                kysymykset.add(teksti);
            }
        }
        return kysymykset;
    }
    
    public List<String> getAiheet(String kurssi) throws SQLException{
        List<String> aiheet = new ArrayList();
        for (Kysymys kysymys: findAll()){
            String teksti = kysymys.getAihe();
            if ( ( !teksti.isEmpty() )&&( kysymys.getKurssi().equals(kurssi) )&&( !aiheet.contains(teksti)   ) ){
                //valitaan vain kyseisen kurssin epätyhjät aiheet ja lisätään ne vain kerran
                aiheet.add(teksti);
            }
                
        }
        return aiheet;
    }
    
    public List<String> getKurssit() throws SQLException{
        List<String> kurssit = new ArrayList();
        for (Kysymys kysymys: findAll()){
            if (!kurssit.contains(kysymys.getKurssi())){
                kurssit.add(kysymys.getKurssi());
            }
                
        }
        return kurssit;
    }

}

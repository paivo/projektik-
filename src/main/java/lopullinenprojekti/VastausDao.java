/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lopullinenprojekti;

import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Päivö Niska
 */
public class VastausDao implements Dao<Vastaus, Integer> {

    private Database database;

    public VastausDao(Database database) {
        this.database = database;
    }

    /**
     *
     * @return @throws SQLException
     */
    @Override
    public List<Vastaus> findAll() throws SQLException {
        List<Vastaus> vastaukset = new ArrayList();
        Connection connection;
        try {
            connection = database.getConnection();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vastaus v = new Vastaus(rs.getInt("kysymys_id"), rs.getString("vastausteksti"), rs.getBoolean("oikein"));
                v.setId(rs.getInt("id"));
                vastaukset.add(v);
            }
            rs.close();
            stmt.close();
            connection.close();

            if (vastaukset.isEmpty()) {
                return null;
            }

        } catch (Exception ex) {
            Logger.getLogger(VastausDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return vastaukset;
    }

    @Override
    public void save(Vastaus vastaus) throws SQLException {
        if (findOne(vastaus)) {
            return;
        }
        Connection conn;
        try {
            conn = database.getConnection();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Vastaus (kysymys_id, vastausteksti, oikein) VALUES (?, ?, ?)");
            stmt.setInt(1, vastaus.getKysymysId());
            stmt.setString(2, vastaus.getVastausteksti());
            if (vastaus.getOikein()) {
                stmt.setInt(3, 1);
            } else {
                stmt.setInt(3, 0);
            }

            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            Logger.getLogger(VastausDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Boolean findOne(Vastaus vastaus) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE kysymys_id = ? AND vastausteksti = ? AND oikein = ?");
            stmt.setInt(1, vastaus.getKysymysId());
            stmt.setString(2, vastaus.getVastausteksti());
            stmt.setBoolean(3, vastaus.getOikein());
            ResultSet result = stmt.executeQuery();
            return result.next();
        } catch (Exception ex) {
            Logger.getLogger(VastausDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public Vastaus findOne(Integer id) throws SQLException, Exception {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Vastaus WHERE id = ?");
            stmt.setInt(1, id);

            ResultSet result = stmt.executeQuery();
            result.next();
            Vastaus vastaus = new Vastaus(result.getInt("kysymys_id"), result.getString("vastausteksti"), result.getBoolean("oikein"));
            vastaus.setId(id);
            return vastaus;
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(VastausDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteKysymyksenVastaukset(Integer kysymys_id) throws SQLException, Exception {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Vastaus WHERE kysymys_id = ?");
            stmt.setInt(1, kysymys_id);
            stmt.executeUpdate();
        }
    }

    public List<Vastaus> getKysymyksenVastaukset(Kysymys kysymys) throws SQLException, Exception {
        List<Vastaus> vastaukset = new ArrayList();
        Connection connection = database.getConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Vastaus WHERE = ?");
        stmt.setInt(1, kysymys.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Vastaus v = new Vastaus(rs.getInt("kysymys_id"), rs.getString("vastausteksti"), rs.getBoolean("oikein"));
            v.setId(rs.getInt("id"));
            vastaukset.add(v);
        }
        rs.close();
        stmt.close();
        connection.close();

        if (vastaukset.isEmpty()) {
            return null;
        }

        return vastaukset;
    }
}

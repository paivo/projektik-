/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lopullinenprojekti;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }
    
    public Connection getConnection() throws Exception {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }

        return DriverManager.getConnection("jdbc:sqlite:huonekalut.db");
    }
    
    public void init() {
        List<String> lauseet = sqlLauseet();

        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqlLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        lista.add("CREATE TABLE Kysymys(id integer PRIMARY KEY, kurssi varchar(200), aihe varchar(200), kysymysteksti varchar(200));");
        lista.add("CREATE TABLE Vastaus(id integer PRIMARY KEY, kysymys_id integer, vastausteksti varchar(200), oikein integer, FOREIGN KEY (kysymys_id) REFERENCES Kysymys(id));");
        lista.add("INSERT INTO Kysymys (kurssi, aihe, kysymysteksti) VALUES ('tika', 'qsl', 'milloin tentti?');");
        lista.add("INSERT INTO Kysymys (kurssi, aihe, kysymysteksti) VALUES ('linis', 'vektori', 'mikä on vektori?');");

        return lista;
    }

    
}
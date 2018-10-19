package lopullinenprojekti;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    /**
     *
     * @param object
     * @return
     * @throws SQLException
     */
    Object findOne(T object) throws SQLException;
    
    Object findOne(Integer id) throws SQLException;

    List<T> findAll() throws SQLException;

    void save(T object) throws SQLException;

    void delete(Integer id) throws SQLException;
}

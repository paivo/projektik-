package lopullinenprojekti;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    Boolean findOne(T object) throws SQLException;

    List<T> findAll() throws SQLException;

    void save(T object) throws SQLException;

    void delete(T object) throws SQLException;
}

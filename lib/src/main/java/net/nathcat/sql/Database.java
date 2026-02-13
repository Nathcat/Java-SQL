package net.nathcat.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

public class Database {
  private Connection conn;
  private final DBConfig config;

  public Database(DBConfig config) {
    this.config = config;
  }

  /**
   * Connect to the configured database. See {@link DBConfig} for configuration.
   */
  private void connect() throws SQLException {
    conn = DriverManager.getConnection(config.uri, config.username, config.password);
  }

  /**
   * Create a new query on this connection
   *
   * @param sql SQL form of the query
   * @throws SQLException
   */
  public Query newQuery(String sql) throws SQLException {
    try {
      return new Query(conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS));
    } catch (CommunicationsException e) {
      connect();
      return newQuery(sql);
    }
  }

  /**
   * Execute a query which should return a result set.
   *
   * @param q The query to execute
   */
  public ResultSet select(Query q) throws SQLException {
    q.execute();
    return q.getResultSet();
  }

  /**
   * Execute a query which should not return a result set.
   *
   * @param q The query
   */
  public void update(Query q) throws SQLException {
    q.execute();
  }
}

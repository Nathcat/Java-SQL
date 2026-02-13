package net.nathcat.sql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query {
  private final PreparedStatement stmt;

  protected Query(PreparedStatement stmt) {
    this.stmt = stmt;
  }

  /**
   * Get the setter method on {@link PreparedStatement} for a given type
   *
   * @param c The type
   * @throws NoSuchMethodException Thrown if the method cannot be found, or there
   *                               is no method for the given type.
   */
  private <T> Method getSetter(Class<T> c) throws NoSuchMethodException {
    if (c == Integer.class) {
      return PreparedStatement.class.getMethod("setInt", Integer.class, Integer.class);
    } else if (c == String.class) {
      return PreparedStatement.class.getMethod("setString", Integer.class, String.class);
    } else if (c == Boolean.class) {
      return PreparedStatement.class.getMethod("setBoolean", Integer.class, Boolean.class);
    } else if (c == Long.class) {
      return PreparedStatement.class.getMethod("setLong", Integer.class, Long.class);
    } else {

      throw new NoSuchMethodException("No setter found for type " + c.getName());
    }
  }

  /**
   * Set a parameter in the query
   *
   * @param paramIndex The index of the parameter, starting from 1
   * @param c          The class type of the parameter
   * @param v          The parameter value
   * @throws NoSuchMethodException Thrown if the setter cannot be found for the
   *                               given type
   */
  public <T> Query set(int paramIndex, Class<T> c, T v) throws NoSuchMethodException {
    try {
      getSetter(c).invoke(stmt, paramIndex, v);
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    return this;
  }

  public void execute() throws SQLException {
    stmt.execute();
  }

  /**
   * Execute an update query.
   *
   * @return If any keys were generated for the query, this key will be returned.
   */
  public int executeUpdate() throws SQLException {
    stmt.executeUpdate();
    ResultSet rs = stmt.getGeneratedKeys();
    if (rs.next()) {
      return rs.getInt(1);
    }

    return -1;
  }

  public ResultSet getResultSet() throws SQLException {
    return stmt.getResultSet();
  }

  public void close() throws SQLException {
    stmt.close();
  }
}

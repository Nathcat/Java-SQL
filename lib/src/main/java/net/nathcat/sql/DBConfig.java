package net.nathcat.sql;

/**
 * Configuration for the database
 *
 */
public final class DBConfig {
  /**
   * The driver URI pointing to the database
   */
  public String uri;
  /**
   * The username to use to login to the database
   */
  public String username;
  /**
   * The password to use to login to the database
   */
  public String password;

  public DBConfig(String uri, String username, String password) {
    this.uri = uri;
    this.username = username;
    this.password = password;
  }
}

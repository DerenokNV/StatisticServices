package org.stock.impl.db;

import java.sql.*;

public class JDBCPostgreSQL {

  private static JDBCPostgreSQL instance;

  private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5433/app_db";
  private static final String USER = "user";
  private static final String PASS = "123";

  private Connection connection = null;

  private JDBCPostgreSQL() {
  }

  public static JDBCPostgreSQL getInstance() {
    if ( instance == null ) {
      instance = new JDBCPostgreSQL();
      instance.init();
    }
    return instance;
  }

  private void init() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch ( ClassNotFoundException e ) {
      System.out.println( "PostgreSQL JDBC Driver is not found. Include it in your library path " );
      return;
    }

    System.out.println( "PostgreSQL JDBC Driver successfully connected" );

    try {
      connection = DriverManager.getConnection( DB_URL, USER, PASS );

    } catch ( SQLException e ) {
      System.out.println( "Connection Failed" );
      return;
    }

    if ( connection != null ) {
      System.out.println( "You successfully connected to database now" );
    } else {
      System.out.println( "Failed to make connection to database" );
    }
  }

  public Connection getConnection() {
    return connection;
  }

  public void closeResultSet( ResultSet rSet ) {
    try {
      if ( rSet != null ) {
        rSet.close();
      }
    } catch ( SQLException ex ) {
      System.out.println( "Error closeResultSet:" + ex );
    }
  }

  public void closeStatement( Statement statement ) {
    try {
      if ( statement != null ) {
        statement.close();
      }
    } catch ( SQLException ex ) {
      System.out.println( "Error closeStatement:" + ex );
    }
  }
}

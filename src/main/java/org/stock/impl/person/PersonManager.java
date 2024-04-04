package org.stock.impl.person;

import javafx.util.Pair;
import org.stock.impl.api.Person;
import org.stock.impl.db.JDBCPostgreSQL;
import org.stock.impl.utils.QueryLoader;
import org.stock.impl.utils.Toolkit;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * Класс для обработки данных по статистике Человеков
 */
public class PersonManager {

  private static int SEX_M = 1;
  private static int SEX_G = 0;

  private static int AGE_MIN = 18;
  private static int AGE_M_MAX = 60;
  private static int AGE_G_MAX = 55;

  public static boolean addPersonTable( Person person ) {
    boolean result = true;

    Connection conn = JDBCPostgreSQL.getInstance().getConnection();
    PreparedStatement statement = null;
    try {
      String query = QueryLoader.getInstance().getQuery( "query.xml", person.getSex() == 1 ? "insertPersonM" : "insertPersonG" );
      if ( query.isEmpty() ) {
        System.out.println( Toolkit.ERROR_GET_QUERY );
        return false;
      }

      statement = conn.prepareStatement( query  );
      statement.setLong( 1, person.getId() );
      statement.setLong( 2, person.getDtOfBirth() );
      statement.setInt( 3, person.getRegion() );
      if ( person.getIncome() != null ) {
        statement.setDouble( 4, person.getIncome() );
      }  else {
        statement.setNull( 4, Types.NUMERIC );
      }
      statement.execute();

    } catch( SQLException ex ) {
      System.out.println( "Что то пошло не так" + ex );
      result = false;
    } finally {
      JDBCPostgreSQL.getInstance().closeStatement( statement );
    }

    return result;
  }

  /**
   * Перерасчет статистики
   */
  public synchronized static void calcStatistics() {
    // 1. Забираем данные по персонам
    Map<Integer,List<Person>> perosns = getPersonGroupRegion();
    if ( perosns == null ) {
      return;
    }

    // 2. Делаем пересчет таб регионы
    updateTableStatRegion( perosns );

    // 3. Пересчет таб РФ
  }


  /**
   * Обновляем таблицу Стат регион
   * @param persons - Данные для расчетов
   */
  private static void updateTableStatRegion( Map<Integer,List<Person>> persons )  {
    int allPerson = 0;
    int allAbleBod = 0;
    int allJobless = 0;
    double allIncomeMax = 0.0;
    double allIncomeSum = 0.0;
    int countAvgIncome = 0;

    // это считаемые данные, все удалим и заново зальем
    Connection conn = JDBCPostgreSQL.getInstance().getConnection();
    PreparedStatement statementDelRegion = null;
    PreparedStatement statementDelRu = null;
    PreparedStatement statementInsert = null;
    PreparedStatement statementRuInsert = null;
    try {
      conn.setAutoCommit( false );

      // 1. Удалим
      String query = QueryLoader.getInstance().getQuery( "query.xml", "deleteStatRegion" );
      statementDelRegion = conn.prepareStatement( query  );
      statementDelRegion.execute();
      query = QueryLoader.getInstance().getQuery( "query.xml", "deleteStatRu" );
      statementDelRu = conn.prepareStatement( query  );
      statementDelRu.execute();

      // 2. Зальем СтатРегион
      String queryStat = QueryLoader.getInstance().getQuery( "query.xml", "insertStatRegion" );
      statementInsert = conn.prepareStatement( queryStat  );
      for ( Map.Entry<Integer,List<Person>> regionPersons : persons.entrySet() ) {
        int countPerson = regionPersons.getValue().size();

        statementInsert.setLong( 1, regionPersons.getKey() );
        statementInsert.setInt( 2, countPerson );

        Pair<Integer,Integer> countAandJ = calcCountAbleBodAndJobless( regionPersons.getValue() );
        statementInsert.setInt( 3, countAandJ.getKey() );
        statementInsert.setDouble( 4, Toolkit.roundDouble( countAandJ.getKey() / (double)regionPersons.getValue().size(), 2 ) );

        OptionalDouble optavg = regionPersons.getValue().stream()
                                                        .filter( x -> x.getIncome() != null )
                                                        .mapToDouble( Person::getIncome ).average();
        if ( optavg.isPresent() ) {
          allIncomeSum = allIncomeSum + optavg.getAsDouble();
          countAvgIncome++;
          statementInsert.setDouble( 5, optavg.getAsDouble() );
        } else {
          statementInsert.setNull( 5, Types.DOUBLE );
        }

        OptionalDouble optmax = regionPersons.getValue().stream()
                                                        .filter( x -> x.getIncome() != null )
                                                        .mapToDouble( Person::getIncome ).max();
        if ( optmax.isPresent() ) {
          if ( allIncomeMax < optmax.getAsDouble() ) {
            allIncomeMax = optmax.getAsDouble();
          }
          statementInsert.setDouble( 6, optmax.getAsDouble() );
        } else {
          statementInsert.setNull( 6, Types.DOUBLE );
        }

        statementInsert.setInt( 7, countAandJ.getValue() );
        if ( countAandJ.getKey() != 0 ) {
          statementInsert.setDouble( 8, Toolkit.roundDouble( countAandJ.getValue() / (double)countAandJ.getKey(), 2 ) );
        } else {
          statementInsert.setNull( 8, Types.DOUBLE );
        }

        // небольшой блок для общей статистики
        allPerson = allPerson + countPerson;
        allAbleBod = allAbleBod + countAandJ.getKey();
        allJobless = allJobless + countAandJ.getValue();

        statementInsert.addBatch();
      }

      // 3. Зальем СтатРФ
      String queryStatRu = QueryLoader.getInstance().getQuery( "query.xml", "insertStatRu" );
      statementRuInsert = conn.prepareStatement( queryStatRu  );
      statementRuInsert.setInt( 1, allPerson );
      statementRuInsert.setInt( 2, allAbleBod );
      statementRuInsert.setDouble( 3, Toolkit.roundDouble(allAbleBod / (double)allPerson, 2 ) );
      statementRuInsert.setDouble( 4, Toolkit.roundDouble( allIncomeSum / countAvgIncome, 2 ) );
      statementRuInsert.setDouble( 5, allIncomeMax );
      statementRuInsert.setInt( 6, allJobless );
      statementRuInsert.setDouble( 7, Toolkit.roundDouble(allJobless / (double)allAbleBod, 2 ) );

      statementDelRegion.execute();
      statementDelRu.execute();
      statementInsert.executeBatch();
      statementRuInsert.execute();
      conn.commit();
    } catch ( Exception ex ) {
      System.out.println( Toolkit.ERROR_JDBC + ex );
      try {
        conn.rollback();
      } catch( Exception e ) {
        System.out.println( Toolkit.ERROR_JDBC + e );
      }
    } finally {
      JDBCPostgreSQL.getInstance().closeStatement( statementDelRegion );
      JDBCPostgreSQL.getInstance().closeStatement( statementDelRu );
      JDBCPostgreSQL.getInstance().closeStatement( statementInsert );
      JDBCPostgreSQL.getInstance().closeStatement( statementRuInsert );
    }
  }

  /**
   * Подсчет трудоспособных и не очень
   * @param persons - список персон по региону
   * @return - key - трудоспособны, value - безработны
   */
  private static Pair<Integer,Integer> calcCountAbleBodAndJobless( List<Person> persons ) {
    int ableBond = 0;
    int jobless = 0;

    for ( Person person : persons ) {
      if ( isJobless( person ) ) {
        ableBond++;
        if ( person.getIncome() == null ) {
          jobless++;
        }
      }
    }

    return new Pair<>( ableBond, jobless );
  }

  private static boolean isJobless( Person person ) {
    Period period = Period.between( Toolkit.fromMillisUTC( person.getDtOfBirth() ).toLocalDate(), LocalDate.now() );
    return ( person.getSex() == SEX_M  && period.getYears() >= AGE_MIN && period.getYears() <= AGE_M_MAX ) ||
           ( person.getSex() == SEX_G && period.getYears() >= AGE_MIN && period.getYears() <= AGE_G_MAX );
  }

  /**
   * Получаем информацию по людям
   * @return - key - регион, value - кол-во
   */
  private static Map<Integer,List<Person>> getPersonGroupRegion() {
    Map<Integer,List<Person>> result = new HashMap<>();

    Connection conn = JDBCPostgreSQL.getInstance().getConnection();
    Statement statement = null;
    ResultSet resultSet = null;
    try {
      String query = QueryLoader.getInstance().getQuery( "query.xml", "getPerson" );
      if ( query.isEmpty() ) {
        return null;
      }

      statement = conn.createStatement();
      resultSet = statement.executeQuery( query );
      while ( resultSet.next() ) {
        addResultPersonGroupRegion( result, resultSet );
      }

    } catch( SQLException ex ) {
      System.out.println( Toolkit.ERROR_JDBC + ex );
    } finally {
      JDBCPostgreSQL.getInstance().closeResultSet( resultSet );
      JDBCPostgreSQL.getInstance().closeStatement( statement );
    }

    return result;
  }

  private static void addResultPersonGroupRegion( Map<Integer,List<Person>> result, ResultSet resultSet ) throws SQLException {
    Person person = new Person();
    person.setId( resultSet.getLong( "ID" ) );
    person.setSex( resultSet.getInt( "SEX" ) );
    person.setDtOfBirth( resultSet.getLong( "DT" ) );
    person.setRegion( resultSet.getInt( "REGION_ID" ) );
    person.setIncome( resultSet.getObject( "INCOME" ) == null ? null : resultSet.getDouble( "INCOME" ) );

    if ( result.containsKey( person.getRegion() ) ) {
      result.get( person.getRegion() ).add( person );
    } else {
      List<Person> persons = new ArrayList<>();
      persons.add( person );
      result.put( person.getRegion(), persons );
    }
  }

}

package org.stock.impl.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class Toolkit {

  public static String MESSAGE_BAD_REST = "Входные данные некорректны";
  public static String ERROR_DT_PARSE = "Ошибка в дате";
  public static String ERROR_GET_QUERY = "Запрос не найден";

  public static String ERROR_JDBC = "Что то пошло не так";

  public static long toMillisUTC( String dt ) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern( "dd.MM.yyyy" );
    long millisecondsSinceEpoch = 0;
    try {
      millisecondsSinceEpoch = LocalDate.parse( dt, dateFormatter )
              .atStartOfDay( ZoneOffset.UTC )
              .toInstant()
              .toEpochMilli();
      System.out.println(millisecondsSinceEpoch);
    } catch ( Exception ex ) {
      System.out.println( ERROR_DT_PARSE );
    }

    return millisecondsSinceEpoch;
  }

  public static LocalDateTime fromMillisUTC( long dt ) {
    LocalDateTime result = LocalDateTime.now();
    try {
      Instant instant = Instant.ofEpochMilli( dt );
      result = LocalDateTime.ofInstant( instant, ZoneId.of( "UTC" ) );
    } catch( Exception ex ) {
      System.out.println( ERROR_JDBC + ex );
    }

    return result;
  }

  public static double roundDouble( double value, int decimal ) {
    int decimals = Double.valueOf( Math.pow( 10, decimal ) ).intValue();
    return Math.round( value * decimals ) / (double)decimals;
  }
}

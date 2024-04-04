package org.stock.impl.utils;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class QueryLoader {

  private Map<String,Map<String,String>> mapXml = new HashMap<>();

  public static QueryLoader instance;

  public static synchronized QueryLoader getInstance() {
    if (instance == null) {
      instance = new QueryLoader();
    }
    return instance;
  }

  private QueryLoader() {}

  public String getQuery( String nameFile, String nameQuery ) {
    if ( ! mapXml.containsKey( nameFile ) ) {
      parserXmlFaile( nameFile );
    }
    return searchInQueries( mapXml.get( nameFile ), nameQuery );
  }

  private String searchInQueries( Map<String,String> mapXmlQuery, String nameQuery ) {
    if ( mapXmlQuery == null || ! mapXmlQuery.containsKey( nameQuery ) ) {
      return null;
    } else {
      return mapXmlQuery.get( nameQuery );
    }
  }

  private void parserXmlFaile( String nameFile ) {
    try {
      SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
      ResourceHandler rHandler = new ResourceHandler( "query" );
      saxParser.parse( QueryLoader.class.getResource( String.valueOf("/" + nameFile ) ).getPath(), rHandler );
      mapXml.put( nameFile, Collections.synchronizedMap( rHandler.result ) );
    } catch ( ParserConfigurationException | SAXException | IOException e ) {
      System.out.println( "readQuery test error = " + e.getMessage() );
    }
  }

}

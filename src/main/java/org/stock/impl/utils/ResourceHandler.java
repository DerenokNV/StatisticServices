package org.stock.impl.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

public class ResourceHandler extends DefaultHandler {
  private StringBuilder currentValue = new StringBuilder();
  private String nameQuery;
  public Map<String,String> result = new HashMap<>();
  private String xmlNodeName;

  public ResourceHandler( String xmlNodeName ) {
    this.xmlNodeName = xmlNodeName;
  }

  @Override
  public void startDocument() {
  }

  @Override
  public void endDocument() {
  }

  @Override
  public void startElement( String uri, String localName, String qName, Attributes attributes ) {
    currentValue.setLength(0);
    nameQuery = null;
    if ( qName.equalsIgnoreCase( xmlNodeName ) ) {
      nameQuery = attributes.getValue("name" );
      currentValue = new StringBuilder();
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if ( nameQuery != null ) {
      result.put( nameQuery, currentValue.toString() );
    }
  }

  @Override
  public void characters(char ch[], int start, int length) throws SAXException {
    currentValue.append(new String(ch, start, length));
  }

}

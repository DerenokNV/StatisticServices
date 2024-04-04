package org.stock.impl.api;

import java.io.Serializable;
import java.util.UUID;

/**
 * Человечек
 */
public class Person implements Serializable {

  private long id;
  private int sex;
  private long dtOfBirth;

  private int region;

  private Double income;

  public Person() {
    this.setId( Math.abs( UUID.randomUUID().getMostSignificantBits() ) );
  }

  public Person( int sex, long dtOfBirth, int region, Double income ) {
    this.setId( Math.abs( UUID.randomUUID().getMostSignificantBits() ) );
    this.setSex( sex );
    this.setDtOfBirth( dtOfBirth );
    this.setRegion( region );
    this.setIncome( income );
  }

  public long getId() {
    return id;
  }

  public void setId( long id ) {
    this.id = id;
  }

  public int getSex() {
    return sex;
  }

  public void setSex( int sex ) {
    this.sex = sex;
  }

  public long getDtOfBirth() {
    return dtOfBirth;
  }

  public void setDtOfBirth( long dtOfBirth ) {
    this.dtOfBirth = dtOfBirth;
  }

  public int getRegion() {
    return region;
  }

  public void setRegion( int region ) {
    this.region = region;
  }

  public Double getIncome() {
    return income;
  }

  public void setIncome( Double income ) {
    this.income = income;
  }
}

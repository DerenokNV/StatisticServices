package org.stock.impl.api;

/**
 * Входящий с инфо по персонам
 */
public class PersonRequest {

  private int sex;
  private String dtOfBirth;
  private int region;
  private Double income;

  public int getSex() {
    return sex;
  }

  public String getDtOfBirth() {
    return dtOfBirth;
  }

  public int getRegion() {
    return region;
  }

  public Double getIncome() {
    return income;
  }

  public void setSex( int sex ) {
    this.sex = sex;
  }

  public void setDtOfBirth( String dtOfBirth ) {
    this.dtOfBirth = dtOfBirth;
  }

  public void setRegion( int region ) {
    this.region = region;
  }

  public void setIncome( Double income ) {
    this.income = income;
  }
}

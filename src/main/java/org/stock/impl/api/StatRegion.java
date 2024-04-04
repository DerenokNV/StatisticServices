package org.stock.impl.api;

import java.io.Serializable;

/**
 * Класс статистики раб/безработ по Региону
 */
public class StatRegion implements Serializable {
  private static final long serialUID = 12100023L;

  private int regionId;
  private Stat stat;

  public int getRegionId() {
    return regionId;
  }

  public void setRegionId( int regionId ) {
    this.regionId = regionId;
  }

  public Stat getStat() {
    return stat;
  }

  public void setStat( Stat stat ) {
    this.stat = stat;
  }
}

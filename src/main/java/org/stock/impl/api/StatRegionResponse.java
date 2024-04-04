package org.stock.impl.api;

import java.util.List;

/**
 * Ответ по статистике Регионы
 */
public class StatRegionResponse {

  private List<StatRegion> statRegion;

  public List<StatRegion> getStatRegion() {
    return statRegion;
  }

  public void setStatRegion( List<StatRegion> statRegion ) {
    this.statRegion = statRegion;
  }
}

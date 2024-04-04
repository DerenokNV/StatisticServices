package org.stock.impl.api;

import java.util.List;

/**
 * Ответ по статистике РФ
 */
public class StatResponse {

  private List<Stat> stat;

  public List<Stat> getStat() {
    return stat;
  }

  public void setStat( List<Stat> stat ) {
    this.stat = stat;
  }
}

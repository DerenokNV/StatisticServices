package org.stock.impl.api;

import java.io.Serializable;

/**
 * Класс статистики раб/безработ
 */
public class Stat implements Serializable {
  private static final long serialUID = 12120023L;

  // Всего человек
  private Integer pioples;

  // Трудоспособные кол-во
  private Integer ableBodies;

  // Процент трудоспособных от pioples
  private Double ableBodiesPercent;

  // Максимальная зп
  private Double incomeAvg;

  // Средняя по палате зп
  private Double incomeMax;

  // Безработные
  private Integer jobless;

  // Процент безработных от ableBodies
  private Double joblessPercent;

  public Integer getPioples() {
    return pioples;
  }

  public void setPioples( Integer pioples ) {
    this.pioples = pioples;
  }

  public Integer getAbleBodies() {
    return ableBodies;
  }

  public void setAbleBodies( Integer ableBodies ) {
    this.ableBodies = ableBodies;
  }

  public Double getAbleBodiesPercent() {
    return ableBodiesPercent;
  }

  public void setAbleBodiesPercent( Double ableBodiesPercent ) {
    this.ableBodiesPercent = ableBodiesPercent;
  }

  public Double getIncomeAvg() {
    return incomeAvg;
  }

  public void setIncomeAvg( Double incomeAvg ) {
    this.incomeAvg = incomeAvg;
  }

  public Double getIncomeMax() {
    return incomeMax;
  }

  public void setIncomeMax( Double incomeMax ) {
    this.incomeMax = incomeMax;
  }

  public Integer getJobless() {
    return jobless;
  }

  public void setJobless( Integer jobless ) {
    this.jobless = jobless;
  }

  public Double getJoblessPercent() {
    return joblessPercent;
  }

  public void setJoblessPercent( Double joblessPercent ) {
    this.joblessPercent = joblessPercent;
  }
}

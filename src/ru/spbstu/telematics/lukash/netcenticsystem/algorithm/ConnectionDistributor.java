package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Environment;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public abstract class ConnectionDistributor {

  protected Environment environment;
  
  /**
   * Distribites connections between firewalls
   * 
   * @param newCon new connection in net centric system
   * @return calculated dispersion
   */
  public abstract double distribute(TVC newCon);
  
  /**
   * @return the name of applied algorithm
   */
  public abstract String getName();

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  public Environment getEnvironment() {
    return environment;
  }
}

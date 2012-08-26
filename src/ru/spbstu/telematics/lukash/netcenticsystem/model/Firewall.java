package ru.spbstu.telematics.lukash.netcenticsystem.model;
import java.util.HashSet;
import java.util.Set;

public class Firewall implements Cloneable {
  public static final int NO_FIREWALL = -1;

  /**
   * Subset of connections which this firewall manages
   */
  private Set<TVC> managedConnections = new HashSet<TVC>();
  
  /**
   * What managed by firewall
   */
  private double managedIntencity;

  /**
   * unique identificator in firewall array (i.e. array index)
   */
  private final int id;

  private final Environment environment;

  /**
   * Initializes firewall by capacity=1000
   * @param environment 
   */
  public Firewall(int id, Environment environment) {
    this.id = id;
    this.environment = environment;
  }

  public double recalculateAndGetManagedIntensity() {
    managedIntencity = 0;
    for (TVC con : managedConnections) {
      managedIntencity += con.getIntencity();
    }
    return managedIntencity;
  }
  
  public double getManagedIntencity() {
    return managedIntencity;
  }

  
  /**
   * Method assigns connection to current firewall
   * @param con connection object
   */
  public void manageConnection(TVC con) {
    manageConnection(con, environment.getFirewalls(), true);
  }
  
  public void manageConnection(TVC con, Firewall[] fws, boolean validate) {
    if (validate && con.getManagerFirewallId() != NO_FIREWALL) { //somebody managed this connection before
      fws[con.getManagerFirewallId()].removeManagedConnection(con);
    }
    
    managedConnections.add(con);
    con.setManagerFirewallId(this.id);
    managedIntencity += con.getIntencity();
  }
  
  /**
   * removes specified connection from managed list
   * @param con
   */
  public void removeManagedConnection(TVC con) {
    boolean wasInList = managedConnections.remove(con);
    
    if(!wasInList) {
      throw new RuntimeException("Tried to remove connection which wasn't in list for firewall: con=" + con +" fw=" +this);
    }
    
    managedIntencity -= con.getIntencity();
    con.setManagerFirewallId(NO_FIREWALL);
  }

  public int getId() {
    return id;
  }
  
  public boolean isManagesConnection(TVC con) {
    return managedConnections.contains(con);
  }
  
  @Override
  public String toString() {
    return "{id=" + id +"}";
  }
  
  static double getFirewallsAverageIntencity(Firewall[] fws) {
    double average = 0;
    for (Firewall fw : fws) {
      average += fw.getManagedIntencity();
    }
    average /= fws.length;
    return average;
  }
}

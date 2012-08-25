package ru.spbstu.telematics.lukash.netcenticsystem.model;
import java.util.HashSet;
import java.util.Set;

import ru.spbstu.telematics.lukash.netcenticsystem.World;


public class Firewall {
  public static final int NO_FIREWALL = -1;

  /**
   * These connections are going through this firewall
   */
  private Set<TVC> connections = new HashSet<>();
  
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

  /**
   * Other firewalls in net centric system
   */
  private final static Firewall[] firewalls = World.getFirewalls();
  
  /**
   * Initializes firewall by capacity=1000
   * @param firewalls 
   */
  public Firewall(int id) {
    this.id = id;
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

  public void addConnection(TVC con) {
    connections.add(con);
  }
  
  /**
   * Method assigns connection to current firewall
   * @param con connection object
   */
  public void manageConnection(TVC con) {
    if (con.getManagerFirewallId() != NO_FIREWALL) { //somebody managed this connection before
      firewalls[con.getManagerFirewallId()].removeManagedConnection(con);
    }
    
    connections.add(con);
    con.setManagerFirewallId(this.id);
    managedIntencity += con.getIntencity();
  }
  
  /**
   * removes specified connection from managed list
   * @param con
   */
  public void removeManagedConnection(TVC con) {
    boolean wasInList = connections.remove(con);
    
    if(!wasInList) {
      throw new RuntimeException("Tried to remove connection which wasn't in list for firerewall: con=" + con +" fw=" +this);
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
  
  public static double getFirewallsAverageIntencity() {
    double average = 0;
    for (Firewall fw : firewalls) {
      average += fw.getManagedIntencity();
    }
    average /= firewalls.length;
    return average;
  }
  
  /**
   * Calculates dispersion of firewalls workload
   * 
   * @return
   */
  public static double dispersion() {

    double average = Firewall.getFirewallsAverageIntencity();

    double d = 0;
    for (Firewall fw : firewalls) {
      d += Math.pow(fw.getManagedIntencity() - average, 2);
    }
    return d / firewalls.length;
  }
}

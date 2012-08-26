package ru.spbstu.telematics.lukash.netcenticsystem.model;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import ru.spbstu.telematics.lukash.netcenticsystem.World;

public class Environment {
  private final Firewall[] firewalls = new Firewall[World.NUM_FIREWALLS];

  {
    for (int i = 0; i < firewalls.length; i++) {
      firewalls[i] = new Firewall(i, this);
    }
  }

  private SortedSet<TVC> connectionsSortedUp = new TreeSet<>();
  private SortedSet<TVC> connectionsSortedDown = new TreeSet<>(new Comparator<TVC>() {

    @Override
    public int compare(TVC o1, TVC o2) {
      return o2.compareTo(o1);
    }

  });

  public Firewall[] getFirewalls() {
    return firewalls;
  }

  public SortedSet<TVC> getConnectionsSortedUp() {
    return connectionsSortedUp;
  }

  public SortedSet<TVC> getConnectionsSortedDown() {
    return connectionsSortedDown;
  }

  public void addTVC(TVC tvc) {
    connectionsSortedUp.add(tvc);
    connectionsSortedDown.add(tvc);
  }
  
  public void validateModel() {
    for (TVC c : connectionsSortedUp) {
      if (!c.validate()) {
        throw new RuntimeException("Validation failed: connection is managed by wrong firewall, c=" + c);
      }

      int managedFw = c.getManagerFirewallId();
      for (Firewall fw : firewalls) {
        if (fw.getId() != managedFw && fw.isManagesConnection(c)) {
          throw new RuntimeException("Validation failed: Two firewalls manage one connection: " + managedFw + ", " + fw.getId());
        }
      }
    }
  }

  /**
   * Calculates dispersion of firewalls workload
   * 
   * @return
   */
  private static double dispersion(Firewall[] fws) {
    double average = getFirewallsAverageIntencity(fws);
    
    double d = 0;
    for (Firewall fw : fws) {
      d += Math.pow(fw.getManagedIntencity() - average, 2);
    }
    return d / fws.length;
  }
  
  private static double getFirewallsAverageIntencity(Firewall[] fws) {
    double average = 0;
    for (Firewall fw : fws) {
      average += fw.getManagedIntencity();
    }
    average /= fws.length;
    return average;
  }
  
  public double dispersion() {
    return dispersion(firewalls);
  }
  
  public double getFirewallsAverageIntencity() {
    return getFirewallsAverageIntencity(firewalls);
  }
}

package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.SortedSet;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class GreedyAlgorithmFromScratchUp implements ConnectionDistributor {

  private static final String NAME = "greedy redistribution (sorted up)";

  /**
   * Distributes connections using greedy algorithm, connections sorted from small to big capacity
   */
  @Override
  public long distribute(SortedSet<TVC> connectionsSortedUp, SortedSet<TVC> connectionsSortedDown, Firewall[] firewalls, TVC newCon) {
    return distribute(connectionsSortedUp, firewalls, newCon);
  }
  
  protected long distribute(SortedSet<TVC> connections, Firewall[] firewalls, TVC newCon) {
    long t = System.currentTimeMillis();
    
    for (TVC c : connections) {
      Firewall min = null;
      int i;
      //select first appropriate
      for (i = 0; i < firewalls.length; i++) {
        if (c.mightBeManagedBy(i)) {
          min = firewalls[i];
          break;
        }
      }
      
      // continue and find most optimal
      for (i = i + 1; i < firewalls.length; i++) {
        if (firewalls[i].getManagedIntencity() < min.getManagedIntencity() && c.mightBeManagedBy(i)) {
          min = firewalls[i];
        }
      }
      min.manageConnection(c);
    }
    return System.currentTimeMillis() - t;
  }

  @Override
  public String getName() {
    return NAME;
  }

}
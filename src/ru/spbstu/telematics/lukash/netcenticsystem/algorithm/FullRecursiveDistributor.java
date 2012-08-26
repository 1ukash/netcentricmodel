package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.SortedSet;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class FullRecursiveDistributor extends ConnectionDistributor {

  private static final String NAME = "full distribution (recursive)";
  private long stepsCount;

  @Override
  public double distribute(TVC newCon) {
    try {
      stepsCount = 0;
      SortedSet<TVC> conns = environment.getConnectionsSortedUp();
      return walkThrough(conns.toArray(new TVC[conns.size()]), environment.getFirewalls());
    } catch (Exception e) {
      throw new RuntimeException("fatal error", e);
    } finally {
//      System.err.println("Steps count:" + stepsCount);
    }
  }
  
  private double walkThrough(TVC[] connections, Firewall[] firewalls) throws Exception {

    int num = connections.length;
    
    double d1 = Double.MAX_VALUE;
    double d2 = Double.MAX_VALUE;
    
    for (TVC c : connections) {
      
      // Left branch
      firewalls[c.getFirewallId1()].manageConnection(c);  
      
      TVC[] subset = copySetWithout(c, connections);
      if (num > 1) {
        d1 = walkThrough(subset, firewalls);
      } else {
        d1 = environment.dispersion();
        stepsCount ++;
      }
      
      //right branch
      firewalls[c.getFirewallId2()].manageConnection(c);
      if (num > 1) {
        d2 = walkThrough(subset, firewalls);
      } else {
        d2 = environment.dispersion();
        stepsCount ++;
      }
    }
    
    return Math.min(d1, d2);
  }
  
  private TVC[] copySetWithout(TVC withoutIt, TVC[] connections) throws CloneNotSupportedException {
    TVC[] r = new TVC[connections.length - 1];
    int idx = 0;
    for (TVC c : connections) {
      if (c != withoutIt) {
        r[idx++] = c;
      }
    }
    return r;
  }
  
  @Override
  public String getName() {
    return NAME;
  }
}

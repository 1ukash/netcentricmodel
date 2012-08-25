package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.SortedSet;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class FullDistributor implements ConnectionDistributor {

  private static final String NAME = "full distribution";

  @Override
  public double distribute(SortedSet<TVC> connectionsSortedUp, SortedSet<TVC> connectionsSortedDown, TVC newCon) {
    try {
      return walkThrough(connectionsSortedUp.toArray(new TVC[connectionsSortedUp.size()]));
    } catch (Exception e) {
      throw new RuntimeException("fatal error", e);
    }
  }
  
  private double walkThrough(TVC[] connections) throws Exception {

    double optimalDispersion = Double.MAX_VALUE;
    int num = connections.length;
    for (TVC c : connections) {
      
      // Left branch
      optimalDispersion = Math.min(optimalDispersion, getDispersionForConnection(c, true));
      
      if (num > 1) {
        optimalDispersion = Math.min(walkThrough(copySetWithout(c, connections)), optimalDispersion);
      }
      
      //right branch
      optimalDispersion = Math.min(optimalDispersion, getDispersionForConnection(c, false));
      
      if (num > 1) {
        optimalDispersion = Math.min(walkThrough(copySetWithout(c, connections)), optimalDispersion);
      }
    }
    
    return optimalDispersion;
  }
  
  private TVC[] copySetWithout(TVC withoutIt, TVC[] connections) throws CloneNotSupportedException {
    TVC[] r = new TVC[connections.length - 1];
    int idx = 0;
    for (TVC c : connections) {
      if (c != withoutIt) {
        r[idx++] = (TVC) c.clone();
      }
    }
    return r;
  }

  private double getDispersionForConnection(TVC con, boolean trueForIdx1) {
    int id = trueForIdx1 ? con.getFirewallId1() : con.getFirewallId2();
    firewalls[id].manageConnection(con);
    return Firewall.dispersion();
  }

  @Override
  public String getName() {
    return NAME;
  }
}

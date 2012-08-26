package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.Set;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class FullDistributor extends ConnectionDistributor {

  private static final String NAME = "full distribution";
  private Set<TVC> originalSet;

  @Override
  public double distribute(TVC newCon) {
    try {
      return walkThrough(null, null);
    } catch (Exception e) {
      throw new RuntimeException("fatal error", e);
    }
  }
  
  private double walkThrough(TVC[] connections, Firewall[] firewalls) throws Exception {

    double optimalDispersion = Double.MAX_VALUE;
    int num = connections.length;
    for (TVC c : connections) {
      
      // Left branch
      optimalDispersion = Math.min(optimalDispersion, getDispersionForConnection(c, true, firewalls));
      
      TVC[] subset = copySetWithout(c, connections);
      if (num > 1) {
//        Firewall[] fwCopy = copyFirewalls(subset, firewalls);
        optimalDispersion = Math.min(walkThrough(subset, firewalls), optimalDispersion);
      }
      
      //right branch
      optimalDispersion = Math.min(optimalDispersion, getDispersionForConnection(c, false, firewalls));
      
      if (num > 1) {
//        Firewall[] fwCopy = copyFirewalls(subset, firewalls);
        optimalDispersion = Math.min(walkThrough(subset, firewalls), optimalDispersion);
      }
    }
    
    return optimalDispersion;
  }
  
//  private Firewall[] copyFirewalls(TVC[] subset, Firewall[] original) throws CloneNotSupportedException {
//    Firewall[] newCopy = new Firewall[original.length];
//    Set<Integer> ids = new HashSet<>();
//    for (int i = 0; i < original.length; i++) {
//      newCopy[i] = new Firewall(i);
//    }
//    //these connections are cloned and will changed
//    for (TVC s : subset) {
//      newCopy[s.getManagerFirewallId()].manageConnection((TVC) s.clone());
//      ids.add(s.getId());
//    }
//
//    // these connections are from already checked subset
//    for (TVC s : originalSet) {
//      if (!ids.contains(s.getId())) {
//        newCopy[s.getManagerFirewallId()].manageConnection(s);
//      }
//    }
//    return newCopy;
//  }

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
  
  private TVC[] copyArray(TVC[] connections) throws CloneNotSupportedException {
    TVC[] r = new TVC[connections.length];
    int idx = 0;
    for (TVC c : connections) {
      r[idx++] = (TVC) c.clone();
    }
    return r;
  }

  //TODO
  private double getDispersionForConnection(TVC con, boolean trueForIdx1, Firewall[] firewalls) {
    int id = trueForIdx1 ? con.getFirewallId1() : con.getFirewallId2();
    firewalls[id].manageConnection(con, firewalls, false);
    return environment.dispersion();
  }

  @Override
  public String getName() {
    return NAME;
  }
}

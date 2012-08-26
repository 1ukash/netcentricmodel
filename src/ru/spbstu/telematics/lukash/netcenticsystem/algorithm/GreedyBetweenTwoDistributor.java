package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.LinkedList;
import java.util.List;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class GreedyBetweenTwoDistributor extends ConnectionDistributor {

  private static final String NAME = "Greedy distributor between two firewalls";

  @Override
  public double distribute(TVC newCon) {
    int id1 = newCon.getFirewallId1();
    int id2 = newCon.getFirewallId2();
    List<TVC> conns = new LinkedList<>();
    
    for (TVC c : environment.getConnectionsSortedDown()) {
      if ( c.mightBeManagedBy(id1) && c.mightBeManagedBy(id2)) {
        conns.add(c);
        if ( !c.equals(newCon)) {
          environment.getFirewalls()[c.getManagerFirewallId()].removeManagedConnection(c);
        }
      }
    }
    
    Firewall fw1 = environment.getFirewalls()[id1];
    Firewall fw2 = environment.getFirewalls()[id2];
    
    for (TVC c : conns) {
      Firewall fw = (fw2.getManagedIntencity() < fw1.getManagedIntencity()) ? fw2 : fw1;
      fw.manageConnection(c);
    }
    return environment.dispersion();
  }

  @Override
  public String getName() {
    return NAME;
  }

}

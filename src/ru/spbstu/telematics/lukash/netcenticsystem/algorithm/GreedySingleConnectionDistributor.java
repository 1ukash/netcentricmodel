package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class GreedySingleConnectionDistributor extends ConnectionDistributor {

  private static final String NAME = "Select firewall only for new";

  @Override
  public double distribute(TVC newCon) {
    
    TVC con = null;
    // We cloned connections to environment before, so we have to find our environment specific connection in our list. :(
    for (TVC c: environment.getConnectionsSortedUp()) {
      if (c.equals(newCon)) {
        con = c;
      }
    }
    
    Firewall fw1 = environment.getFirewalls()[con .getFirewallId1()];
    Firewall fw2 = environment.getFirewalls()[con.getFirewallId2()];
    
    Firewall fw = (fw2.getManagedIntencity() < fw1.getManagedIntencity()) ? fw2 : fw1;
    fw.manageConnection(con);
    
    return environment.dispersion();
  }

  @Override
  public String getName() {
    return NAME;
  }
}

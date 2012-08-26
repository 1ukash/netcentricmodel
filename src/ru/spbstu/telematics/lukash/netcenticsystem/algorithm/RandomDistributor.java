package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.Random;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class RandomDistributor extends ConnectionDistributor {

  private static final String NAME = "random algorithm";

  @Override
  public double distribute(TVC newCon) {
    Random r = new Random();
    Firewall[] firewalls = environment.getFirewalls();
    // forget about all links and redistribute connections from scratch
    for (TVC c : environment.getConnectionsSortedUp()) {
      int managerFirewallId = r.nextBoolean() ? c.getFirewallId1() : c.getFirewallId2();
      firewalls[managerFirewallId].manageConnection(c);
    }
    
    return environment.dispersion();
  }

  @Override
  public String getName() {
    return NAME;
  }

}

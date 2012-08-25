package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.Random;
import java.util.SortedSet;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class RandomDistributor implements ConnectionDistributor {

  private static final String NAME = "random algorithm";

  @Override
  public long distribute(SortedSet<TVC> connectionsSortedUp, SortedSet<TVC> connectionsSortedDown, Firewall[] firewalls, TVC newCon) {
    long start = System.currentTimeMillis();
    Random r = new Random();
    // forget about all links and redistribute connections from scratch
    for (TVC c : connectionsSortedUp) {
      int managerFirewallId = r.nextBoolean() ? c.getFirewallId1() : c.getFirewallId2();
      firewalls[managerFirewallId].manageConnection(c);
    }
    
    return System.currentTimeMillis() - start;
  }

  @Override
  public String getName() {
    return NAME;
  }

}

package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.SortedSet;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class GreedyAlgorithmFromScratch implements ConnectionDistributor {

  private static final String NAME = "greedy redistribution";

  @Override
  public long distribute(SortedSet<TVC> connections, Firewall[] firewalls, TVC newCon) {
    long t = System.currentTimeMillis();
    
    double average = Firewall.getFirewallsAverageIntencity();
    for (TVC c : connections) {
      
    }
    return System.currentTimeMillis() - t;
  }

  @Override
  public String getName() {
    return NAME;
  }

}

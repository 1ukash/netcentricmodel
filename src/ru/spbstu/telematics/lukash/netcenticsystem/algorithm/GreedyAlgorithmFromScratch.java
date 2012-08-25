package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.SortedSet;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class GreedyAlgorithmFromScratch implements ConnectionDistributor {

  private static final String NAME = "greedy redistribution";

  @Override
  public long distribute(SortedSet<TVC> connections, Firewall[] firewalls, TVC newCon) {
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

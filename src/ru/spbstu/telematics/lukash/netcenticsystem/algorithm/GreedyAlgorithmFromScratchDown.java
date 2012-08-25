package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.SortedSet;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class GreedyAlgorithmFromScratchDown extends GreedyAlgorithmFromScratchUp {

  private static final String NAME = "greedy redistribution (sorted down)";

  /**
   * Distributes connections using greedy algorithm, connections sorted from small to big capacity
   */
  @Override
  public long distribute(SortedSet<TVC> connectionsSortedUp, SortedSet<TVC> connectionsSortedDown, Firewall[] firewalls, TVC newCon) {
    return distribute(connectionsSortedDown, firewalls, newCon);
  }

  @Override
  public String getName() {
    return NAME;
  }
}

package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.SortedSet;

import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class GreedyAlgorithmFromScratchDown extends GreedyAlgorithmFromScratchUp {

  private static final String NAME = "greedy redistribution (sorted down)";

  /**
   * Distributes connections using greedy algorithm, connections sorted from small to big capacity
   */
  @Override
  public double distribute(SortedSet<TVC> connectionsSortedUp, SortedSet<TVC> connectionsSortedDown, TVC newCon) {
    return distribute(connectionsSortedDown, newCon);
  }

  @Override
  public String getName() {
    return NAME;
  }
}

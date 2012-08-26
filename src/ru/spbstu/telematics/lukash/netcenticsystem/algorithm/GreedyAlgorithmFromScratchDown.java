package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class GreedyAlgorithmFromScratchDown extends GreedyAlgorithmFromScratchUp {

  private static final String NAME = "greedy redistribution (sorted down)";

  /**
   * Distributes connections using greedy algorithm, connections sorted from small to big capacity
   */
  @Override
  public double distribute(TVC newCon) {
    return distribute(environment.getConnectionsSortedDown(), newCon);
  }

  @Override
  public String getName() {
    return NAME;
  }
}

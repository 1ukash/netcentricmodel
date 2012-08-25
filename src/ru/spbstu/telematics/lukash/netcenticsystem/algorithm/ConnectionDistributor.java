package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.SortedSet;

import ru.spbstu.telematics.lukash.netcenticsystem.World;
import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public interface ConnectionDistributor {

  static final Firewall[] firewalls = World.getFirewalls();
  
  /**
   * Distribites connections between firewalls
   * 
   * @param connectionsSortedUp unmodifiable sorted set of connections from small to big
   * @param connectionsSortedDown  unmodifiable sorted set of connections from big to small
   * @param firewalls array of firewalls
   * @param newCon new connection in net centric system
   * @return calculated dispersion
   */
  double distribute(final SortedSet<TVC> connectionsSortedUp, SortedSet<TVC> connectionsSortedDown, TVC newCon);
  
  /**
   * @return the name of applied algorithm
   */
  String getName();
}

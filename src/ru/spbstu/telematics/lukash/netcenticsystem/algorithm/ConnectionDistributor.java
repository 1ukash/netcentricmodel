package ru.spbstu.telematics.lukash.netcenticsystem.algorithm;

import java.util.SortedSet;

import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public interface ConnectionDistributor {
  /**
   * Distribites connections between firewalls
   * 
   * @param connections unmodifiable list of connections
   * @param firewalls array of firewalls
   * @param newCon new connection in net centric system
   * @return time in milliseconds
   */
  long distribute(final SortedSet<TVC> connections, final Firewall[] firewalls, TVC newCon);
  
  String getName();
}

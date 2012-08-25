package ru.spbstu.telematics.lukash.netcenticsystem;

import java.util.Collections;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.ConnectionDistributor;
import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.GreedyAlgorithmFromScratch;
import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.RandomDistributor;
import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class World {

  private static final int MAX_CONNECTIONS = 100;
  private static final int NUM_FIREWALLS = 10;

  private final static Firewall[] firewalls = new Firewall[NUM_FIREWALLS];
  
  static {
    for (int i = 0; i < firewalls.length; i++) {
      firewalls[i] = new Firewall(i);
    }
  }

  private SortedSet<TVC> connections = new TreeSet<>();
  private final ConnectionDistributor[] distributor = new ConnectionDistributor[] { new RandomDistributor(), new GreedyAlgorithmFromScratch() };
  private final Random r = new Random();

  public static void main(String[] args) {
    new World().live();
  }

  private void live() {

    long start = System.currentTimeMillis();
    
    // Print header
    System.out.print("#,");
    for (ConnectionDistributor d : distributor) {
      System.out.print("Dispersion of " + d.getName() + ", Time " + d.getName());
    }
    System.out.println();

    // Generate connections
    for (int i = 0; i < MAX_CONNECTIONS; i++) {
      TVC con = generateTVC();

      StringBuilder b = new StringBuilder();
      b.append(i + 1);

      for (ConnectionDistributor d : distributor) {
        // select firewall
        long t = d.distribute(Collections.unmodifiableSortedSet(connections), firewalls, con);
        // run state tests
        validate();
        // calculate dispersion
        double dispersion = Firewall.dispersion();
        b.append(',').append(dispersion).append(',').append(t);
      }
      System.out.println(b.toString());
    }
    System.out.println("DONE. Modelling time=" + (System.currentTimeMillis() - start));
  }



  private void validate() {
    for (TVC c : connections) {
      if (!c.validate()) {
        throw new RuntimeException("Validation failed: connection is managed by wrong firewall, c=" + c);
      }

      int managedFw = c.getManagerFirewallId();
      for (Firewall fw : firewalls) {
        if (fw.getId() != managedFw && fw.isManagesConnection(c)) {
          throw new RuntimeException("Validation failed: Two firewall manages one connection: " + managedFw + ", " + fw.getId());
        }
      }
    }

  }

  /**
   * Generates connection between two randomly selected <b>different</b> firewalls
   * @return connection object
   */
  private TVC generateTVC() {
    TVC tvc = new TVC(r.nextDouble());

    int idx1, idx2;

    idx1 = r.nextInt(firewalls.length);

    do {
      idx2 = r.nextInt(firewalls.length);
    } while (idx2 == idx1);

    tvc.setFirewallIndexes(idx1, idx2);
    connections.add(tvc);
    firewalls[tvc.getFirewallId1()].addConnection(tvc);
    firewalls[tvc.getFirewallId2()].addConnection(tvc);
    
    return tvc;
  }

  public static Firewall[] getFirewalls() {
    return firewalls;
  }
}

package ru.spbstu.telematics.lukash.netcenticsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.ConnectionDistributor;
import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.FullDistributor;
import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.GreedyAlgorithmFromScratchDown;
import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.GreedyAlgorithmFromScratchUp;
import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.RandomDistributor;
import ru.spbstu.telematics.lukash.netcenticsystem.model.Firewall;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class World {

  private static final String OUTPUT_FOLDER = "/home/lukash/tmp/modelling";
  private static final int MAX_CONNECTIONS = 10000;
  private static final int NUM_FIREWALLS = 10;

  private final static Firewall[] firewalls = new Firewall[NUM_FIREWALLS];
  
  static {
    for (int i = 0; i < firewalls.length; i++) {
      firewalls[i] = new Firewall(i);
    }
  }

  private SortedSet<TVC> connectionsSortedUp = new TreeSet<>();
  private SortedSet<TVC> connectionsSortedDown = new TreeSet<>(new Comparator<TVC>() {

    @Override
    public int compare(TVC o1, TVC o2) {
      return o2.compareTo(o1);
    }
  
  });
  
  
  private final ConnectionDistributor[] distributor = new ConnectionDistributor[] { 
                                                          new RandomDistributor(), 
                                                          new GreedyAlgorithmFromScratchUp(), 
                                                          new GreedyAlgorithmFromScratchDown(),
                                                          new FullDistributor()
                                                      };
  private final Random r = new Random();

  public static void main(String[] args) throws IOException {
    new World().live();
  }

  private void live() throws IOException {

    long start = System.currentTimeMillis();
    
    FileWriter wr = new FileWriter(OUTPUT_FOLDER + "/model" + System.currentTimeMillis() + ".csv");
    
    StringBuilder b = new StringBuilder();
    b.append("#,");
    // Print header
    for (ConnectionDistributor d : distributor) {
      b.append("Dispersion of ").append(d.getName()).append(',')/*.append("Time ").append(d.getName())*/;
    }
    
    writeMsg(wr, b.toString());

    // Generate connections
    for (int i = 0; i < MAX_CONNECTIONS; i++) {
      TVC con = generateTVC();

      b = new StringBuilder();
      b.append(i + 1);

      for (ConnectionDistributor d : distributor) {
        // select firewall
        long t = System.currentTimeMillis();
        double dispersion = d.distribute(Collections.unmodifiableSortedSet(connectionsSortedUp), Collections.unmodifiableSortedSet(connectionsSortedDown), con);
        t = System.currentTimeMillis() - t;
        // run state tests
        validateModel();
        // calculate dispersion
        b.append(',').append(dispersion)/*.append(',').append(t)*/;
      }
      writeMsg(wr, b.toString());
    }
    writeMsg(wr, "DONE. Modelling time=" + (System.currentTimeMillis() - start));
    
    wr.flush();
    wr.close();
  }



  private void writeMsg(FileWriter wr, String msg) throws IOException {
//    System.out.println(msg);
    wr.write(msg);
    wr.write('\n');
  }

  private void validateModel() {
    for (TVC c : connectionsSortedUp) {
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
   * Generates connection between two rabndomly selected <b>different</b> firewalls
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
    connectionsSortedUp.add(tvc);
    connectionsSortedDown.add(tvc);
    firewalls[tvc.getFirewallId1()].addConnection(tvc);
    firewalls[tvc.getFirewallId2()].addConnection(tvc);
    
    return tvc;
  }

  public static Firewall[] getFirewalls() {
    return firewalls;
  }
}

package ru.spbstu.telematics.lukash.netcenticsystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.ConnectionDistributor;
import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.FullDistributor;
import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.GreedyAlgorithmFromScratchDown;
import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.GreedyAlgorithmFromScratchUp;
import ru.spbstu.telematics.lukash.netcenticsystem.algorithm.RandomDistributor;
import ru.spbstu.telematics.lukash.netcenticsystem.model.Environment;
import ru.spbstu.telematics.lukash.netcenticsystem.model.TVC;

public class World {

  private static final String OUTPUT_FOLDER = "/home/lukash/tmp/modelling";
  private static final int MAX_CONNECTIONS = 10000;
  public static final int NUM_FIREWALLS = 10;

  
  private final ConnectionDistributor[] distributors = new ConnectionDistributor[] { 
                                                          new RandomDistributor(), 
                                                          new GreedyAlgorithmFromScratchUp(), 
                                                          new GreedyAlgorithmFromScratchDown(),
                                                          new FullDistributor()
                                                      };
  private final Random r = new Random();
  private Environment[] environments;
  
  
  public World() {
    environments = new Environment[distributors.length];
    for (int i = 0; i< environments.length; i++) {
      environments[i] = new Environment();
      distributors[i].setEnvironment(environments[i]);
    }
  }

  public static void main(String[] args) throws Exception {
    new World().live();
  }

  private void live() throws Exception {

    long start = System.currentTimeMillis();
    
    FileWriter wr = new FileWriter(OUTPUT_FOLDER + "/model" + System.currentTimeMillis() + ".csv");
    
    StringBuilder b = new StringBuilder();
    b.append("#,");
    // Print header
    for (ConnectionDistributor d : distributors) {
      b.append("Dispersion of ").append(d.getName()).append(',')/*.append("Time ").append(d.getName())*/;
    }
    
    writeMsg(wr, b.toString());

    // Generate connections
    for (int i = 0; i < MAX_CONNECTIONS; i++) {
      TVC con = generateTVC(i + 1);

      b = new StringBuilder();
      b.append(i + 1);

      for (ConnectionDistributor d : distributors) {
        // select firewall
        long t = System.currentTimeMillis();
        double dispersion = d.distribute(con);
        t = System.currentTimeMillis() - t;
        // run state tests
        d.getEnvironment().validateModel();
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
    System.out.println(msg);
    wr.write(msg);
    wr.write('\n');
  }

  /**
   * Generates connection between two rabndomly selected <b>different</b> firewalls
   * @return connection object
   * @throws CloneNotSupportedException 
   */
  private TVC generateTVC(int id) throws CloneNotSupportedException {
    TVC tvc = new TVC(id, r.nextDouble());

    int idx1, idx2;

    idx1 = r.nextInt(NUM_FIREWALLS);

    do {
      idx2 = r.nextInt(NUM_FIREWALLS);
    } while (idx2 == idx1);

    tvc.setFirewallIndexes(idx1, idx2);
    
    for (Environment e : environments) {
      e.addTVC(tvc.clone()); //TODO probably avoid clone in the fututure?
    }
    return tvc;
  }
}

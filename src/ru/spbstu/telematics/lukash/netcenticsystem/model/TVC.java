package ru.spbstu.telematics.lukash.netcenticsystem.model;

public class TVC implements Comparable<TVC> {
  
  private Double intencity;

  /**
   * These firewalls might manage this connection 
   */
  private int firewallId1, firewallId2;
  
  /**
   * manager firewall
   */
  private int managerFirewallId = Firewall.NO_FIREWALL;

  public TVC(double intencity) {
    this.intencity = intencity;
  }


  public double getIntencity() {
    return intencity;
  }

  public void setFirewallIndexes(int idx1, int idx2) {
    this.firewallId1=idx1;
    this.firewallId2 = idx2;
  }

  public int getFirewallId1() {
    return firewallId1;
  }


  public int getFirewallId2() {
    return firewallId2;
  }
  

  public int getManagerFirewallId() {
    return managerFirewallId;
  }

  public void setManagerFirewallId(int managerFirewallId) {
    this.managerFirewallId = managerFirewallId;
  }


  @Override
  public int compareTo(TVC o) {
    return intencity.compareTo(o.intencity);
  }
  
  public boolean validate() {
    return managerFirewallId == firewallId1 || managerFirewallId == firewallId2;
  }
  
  @Override
  public String toString() {
    return "{" + "intencity=" + intencity + ",fw1=" + firewallId1 +",fw2=" + firewallId2 + ",managerFw=" + managerFirewallId + "}";
  }

}
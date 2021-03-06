package ru.spbstu.telematics.lukash.netcenticsystem.model;

public class TVC implements Comparable<TVC>, Cloneable {
  
  private Double intencity;

  /**
   * These firewalls might manage this connection 
   */
  private int firewallId1, firewallId2;
  
  /**
   * manager firewall
   */
  private int managerFirewallId = Firewall.NO_FIREWALL;
  
  /**
   * The unique id
   */
  private int id;

  public TVC(int id, double intencity) {
    this.id = id;
    this.intencity = intencity;
  }

  public int getId() {
    return id;
  }

  public double getIntencity() {
    return intencity;
  }

  public void setFirewallIndexes(int idx1, int idx2) {
    // keep the order of numbers
    firewallId1 = Math.min(idx1, idx2);
    firewallId2 = Math.max(idx1, idx2);
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

  protected void setManagerFirewallId(int managerFirewallId) {
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
    return "{id=" + id + ",intencity=" + intencity + ",fw1=" + firewallId1 +",fw2=" + firewallId2 + ",managerFw=" + managerFirewallId + "}";
  }


  public boolean mightBeManagedBy(int firewallId) {
    return firewallId == firewallId1 || firewallId == firewallId2;
  }
  
  @Override
  public TVC clone() {
    try {
      return (TVC) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("fatal", e);
    }
  }
  
  @Override
  public int hashCode() {
    return id;
  }
  
  @Override
  public boolean equals(Object obj) {
    
    if (!(obj instanceof TVC)) {
      return false;
    }
    
    return id == ((TVC) obj).id;
  }
}

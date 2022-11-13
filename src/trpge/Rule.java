package trpge;

public abstract class Rule extends TRPGEObject {

  public Rule() {
    TRPGE.rules.add(this);
  }

  public abstract boolean check();

  public abstract void consequence();

}

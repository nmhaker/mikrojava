// generated with ast extension for cup
// version 0.8
// 3/5/2019 20:23:31


package src.rs.ac.bg.etf.pp1.ast;

public class FactorDerived5 extends Factor {

    private Instantiation Instantiation;

    public FactorDerived5 (Instantiation Instantiation) {
        this.Instantiation=Instantiation;
        if(Instantiation!=null) Instantiation.setParent(this);
    }

    public Instantiation getInstantiation() {
        return Instantiation;
    }

    public void setInstantiation(Instantiation Instantiation) {
        this.Instantiation=Instantiation;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Instantiation!=null) Instantiation.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Instantiation!=null) Instantiation.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Instantiation!=null) Instantiation.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDerived5(\n");

        if(Instantiation!=null)
            buffer.append(Instantiation.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDerived5]");
        return buffer.toString();
    }
}

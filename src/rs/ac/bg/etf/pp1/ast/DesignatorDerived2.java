// generated with ast extension for cup
// version 0.8
// 3/5/2019 20:23:31


package src.rs.ac.bg.etf.pp1.ast;

public class DesignatorDerived2 extends Designator {

    private Enum Enum;

    public DesignatorDerived2 (Enum Enum) {
        this.Enum=Enum;
        if(Enum!=null) Enum.setParent(this);
    }

    public Enum getEnum() {
        return Enum;
    }

    public void setEnum(Enum Enum) {
        this.Enum=Enum;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Enum!=null) Enum.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Enum!=null) Enum.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Enum!=null) Enum.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorDerived2(\n");

        if(Enum!=null)
            buffer.append(Enum.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorDerived2]");
        return buffer.toString();
    }
}

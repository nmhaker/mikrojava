// generated with ast extension for cup
// version 0.8
// 3/5/2019 20:23:31


package src.rs.ac.bg.etf.pp1.ast;

public class InstantiationDerived1 extends Instantiation {

    private InstPrimitive InstPrimitive;

    public InstantiationDerived1 (InstPrimitive InstPrimitive) {
        this.InstPrimitive=InstPrimitive;
        if(InstPrimitive!=null) InstPrimitive.setParent(this);
    }

    public InstPrimitive getInstPrimitive() {
        return InstPrimitive;
    }

    public void setInstPrimitive(InstPrimitive InstPrimitive) {
        this.InstPrimitive=InstPrimitive;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InstPrimitive!=null) InstPrimitive.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InstPrimitive!=null) InstPrimitive.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InstPrimitive!=null) InstPrimitive.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InstantiationDerived1(\n");

        if(InstPrimitive!=null)
            buffer.append(InstPrimitive.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InstantiationDerived1]");
        return buffer.toString();
    }
}

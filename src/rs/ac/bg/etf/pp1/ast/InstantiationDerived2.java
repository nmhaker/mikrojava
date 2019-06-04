// generated with ast extension for cup
// version 0.8
// 3/5/2019 20:23:31


package src.rs.ac.bg.etf.pp1.ast;

public class InstantiationDerived2 extends Instantiation {

    private InstArray InstArray;

    public InstantiationDerived2 (InstArray InstArray) {
        this.InstArray=InstArray;
        if(InstArray!=null) InstArray.setParent(this);
    }

    public InstArray getInstArray() {
        return InstArray;
    }

    public void setInstArray(InstArray InstArray) {
        this.InstArray=InstArray;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InstArray!=null) InstArray.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InstArray!=null) InstArray.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InstArray!=null) InstArray.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InstantiationDerived2(\n");

        if(InstArray!=null)
            buffer.append(InstArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InstantiationDerived2]");
        return buffer.toString();
    }
}

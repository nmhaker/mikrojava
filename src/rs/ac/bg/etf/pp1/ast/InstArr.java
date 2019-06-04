// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class InstArr extends Instantiation {

    private InstArray InstArray;

    public InstArr (InstArray InstArray) {
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
        buffer.append("InstArr(\n");

        if(InstArray!=null)
            buffer.append(InstArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InstArr]");
        return buffer.toString();
    }
}

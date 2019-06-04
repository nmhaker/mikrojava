// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class ArrayVarDecl extends VarDecl {

    private ArrayDecl ArrayDecl;

    public ArrayVarDecl (ArrayDecl ArrayDecl) {
        this.ArrayDecl=ArrayDecl;
        if(ArrayDecl!=null) ArrayDecl.setParent(this);
    }

    public ArrayDecl getArrayDecl() {
        return ArrayDecl;
    }

    public void setArrayDecl(ArrayDecl ArrayDecl) {
        this.ArrayDecl=ArrayDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArrayDecl!=null) ArrayDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrayDecl!=null) ArrayDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrayDecl!=null) ArrayDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ArrayVarDecl(\n");

        if(ArrayDecl!=null)
            buffer.append(ArrayDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ArrayVarDecl]");
        return buffer.toString();
    }
}

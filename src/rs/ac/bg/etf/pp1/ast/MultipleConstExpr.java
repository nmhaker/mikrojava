// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class MultipleConstExpr extends ConstInitializationList {

    private ConstExpr ConstExpr;
    private ConstInitializationList ConstInitializationList;

    public MultipleConstExpr (ConstExpr ConstExpr, ConstInitializationList ConstInitializationList) {
        this.ConstExpr=ConstExpr;
        if(ConstExpr!=null) ConstExpr.setParent(this);
        this.ConstInitializationList=ConstInitializationList;
        if(ConstInitializationList!=null) ConstInitializationList.setParent(this);
    }

    public ConstExpr getConstExpr() {
        return ConstExpr;
    }

    public void setConstExpr(ConstExpr ConstExpr) {
        this.ConstExpr=ConstExpr;
    }

    public ConstInitializationList getConstInitializationList() {
        return ConstInitializationList;
    }

    public void setConstInitializationList(ConstInitializationList ConstInitializationList) {
        this.ConstInitializationList=ConstInitializationList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstExpr!=null) ConstExpr.accept(visitor);
        if(ConstInitializationList!=null) ConstInitializationList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstExpr!=null) ConstExpr.traverseTopDown(visitor);
        if(ConstInitializationList!=null) ConstInitializationList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstExpr!=null) ConstExpr.traverseBottomUp(visitor);
        if(ConstInitializationList!=null) ConstInitializationList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultipleConstExpr(\n");

        if(ConstExpr!=null)
            buffer.append(ConstExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstInitializationList!=null)
            buffer.append(ConstInitializationList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultipleConstExpr]");
        return buffer.toString();
    }
}

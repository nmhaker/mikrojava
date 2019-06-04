// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class MultipleEnumExpr extends EnumExprList {

    private EnumExpr EnumExpr;
    private EnumExprList EnumExprList;

    public MultipleEnumExpr (EnumExpr EnumExpr, EnumExprList EnumExprList) {
        this.EnumExpr=EnumExpr;
        if(EnumExpr!=null) EnumExpr.setParent(this);
        this.EnumExprList=EnumExprList;
        if(EnumExprList!=null) EnumExprList.setParent(this);
    }

    public EnumExpr getEnumExpr() {
        return EnumExpr;
    }

    public void setEnumExpr(EnumExpr EnumExpr) {
        this.EnumExpr=EnumExpr;
    }

    public EnumExprList getEnumExprList() {
        return EnumExprList;
    }

    public void setEnumExprList(EnumExprList EnumExprList) {
        this.EnumExprList=EnumExprList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumExpr!=null) EnumExpr.accept(visitor);
        if(EnumExprList!=null) EnumExprList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumExpr!=null) EnumExpr.traverseTopDown(visitor);
        if(EnumExprList!=null) EnumExprList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumExpr!=null) EnumExpr.traverseBottomUp(visitor);
        if(EnumExprList!=null) EnumExprList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultipleEnumExpr(\n");

        if(EnumExpr!=null)
            buffer.append(EnumExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumExprList!=null)
            buffer.append(EnumExprList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultipleEnumExpr]");
        return buffer.toString();
    }
}

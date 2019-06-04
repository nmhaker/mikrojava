// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class EnumDeclProduction extends EnumDecl {

    private String I1;
    private EnumExprList EnumExprList;

    public EnumDeclProduction (String I1, EnumExprList EnumExprList) {
        this.I1=I1;
        this.EnumExprList=EnumExprList;
        if(EnumExprList!=null) EnumExprList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
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
        if(EnumExprList!=null) EnumExprList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumExprList!=null) EnumExprList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumExprList!=null) EnumExprList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDeclProduction(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(EnumExprList!=null)
            buffer.append(EnumExprList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumDeclProduction]");
        return buffer.toString();
    }
}

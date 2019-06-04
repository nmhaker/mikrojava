// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class EnumExprProduction extends EnumExpr {

    private String I1;
    private EnumInst EnumInst;

    public EnumExprProduction (String I1, EnumInst EnumInst) {
        this.I1=I1;
        this.EnumInst=EnumInst;
        if(EnumInst!=null) EnumInst.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public EnumInst getEnumInst() {
        return EnumInst;
    }

    public void setEnumInst(EnumInst EnumInst) {
        this.EnumInst=EnumInst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumInst!=null) EnumInst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumInst!=null) EnumInst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumInst!=null) EnumInst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumExprProduction(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(EnumInst!=null)
            buffer.append(EnumInst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumExprProduction]");
        return buffer.toString();
    }
}

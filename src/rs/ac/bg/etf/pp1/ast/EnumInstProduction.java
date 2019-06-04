// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class EnumInstProduction extends EnumInst {

    private Integer N1;

    public EnumInstProduction (Integer N1) {
        this.N1=N1;
    }

    public Integer getN1() {
        return N1;
    }

    public void setN1(Integer N1) {
        this.N1=N1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumInstProduction(\n");

        buffer.append(" "+tab+N1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumInstProduction]");
        return buffer.toString();
    }
}

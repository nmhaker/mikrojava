// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class OptionalReturnDerived1 extends OptionalReturn {

    public OptionalReturnDerived1 () {
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
        buffer.append("OptionalReturnDerived1(\n");

        buffer.append(tab);
        buffer.append(") [OptionalReturnDerived1]");
        return buffer.toString();
    }
}

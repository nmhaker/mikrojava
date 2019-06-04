// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class MulopTerm extends Term {

    private Factor Factor;
    private Mulop Mulop;
    private Factor Factor1;

    public MulopTerm (Factor Factor, Mulop Mulop, Factor Factor1) {
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
        this.Mulop=Mulop;
        if(Mulop!=null) Mulop.setParent(this);
        this.Factor1=Factor1;
        if(Factor1!=null) Factor1.setParent(this);
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public Mulop getMulop() {
        return Mulop;
    }

    public void setMulop(Mulop Mulop) {
        this.Mulop=Mulop;
    }

    public Factor getFactor1() {
        return Factor1;
    }

    public void setFactor1(Factor Factor1) {
        this.Factor1=Factor1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Factor!=null) Factor.accept(visitor);
        if(Mulop!=null) Mulop.accept(visitor);
        if(Factor1!=null) Factor1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
        if(Mulop!=null) Mulop.traverseTopDown(visitor);
        if(Factor1!=null) Factor1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        if(Mulop!=null) Mulop.traverseBottomUp(visitor);
        if(Factor1!=null) Factor1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MulopTerm(\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mulop!=null)
            buffer.append(Mulop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factor1!=null)
            buffer.append(Factor1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MulopTerm]");
        return buffer.toString();
    }
}

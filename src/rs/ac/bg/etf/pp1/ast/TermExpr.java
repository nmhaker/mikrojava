// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class TermExpr extends Expr {

    private SignedTerm SignedTerm;

    public TermExpr (SignedTerm SignedTerm) {
        this.SignedTerm=SignedTerm;
        if(SignedTerm!=null) SignedTerm.setParent(this);
    }

    public SignedTerm getSignedTerm() {
        return SignedTerm;
    }

    public void setSignedTerm(SignedTerm SignedTerm) {
        this.SignedTerm=SignedTerm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SignedTerm!=null) SignedTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SignedTerm!=null) SignedTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SignedTerm!=null) SignedTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("TermExpr(\n");

        if(SignedTerm!=null)
            buffer.append(SignedTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [TermExpr]");
        return buffer.toString();
    }
}

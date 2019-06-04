// generated with ast extension for cup
// version 0.8
// 3/5/2019 20:23:31


package src.rs.ac.bg.etf.pp1.ast;

public class ExprDerived1 extends Expr {

    private SignedExpr SignedExpr;

    public ExprDerived1 (SignedExpr SignedExpr) {
        this.SignedExpr=SignedExpr;
        if(SignedExpr!=null) SignedExpr.setParent(this);
    }

    public SignedExpr getSignedExpr() {
        return SignedExpr;
    }

    public void setSignedExpr(SignedExpr SignedExpr) {
        this.SignedExpr=SignedExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SignedExpr!=null) SignedExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SignedExpr!=null) SignedExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SignedExpr!=null) SignedExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprDerived1(\n");

        if(SignedExpr!=null)
            buffer.append(SignedExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprDerived1]");
        return buffer.toString();
    }
}

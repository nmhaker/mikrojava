// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class AddopExpr extends Expr {

    private SignedTerm SignedTerm;
    private Addop Addop;
    private Term Term;

    public AddopExpr (SignedTerm SignedTerm, Addop Addop, Term Term) {
        this.SignedTerm=SignedTerm;
        if(SignedTerm!=null) SignedTerm.setParent(this);
        this.Addop=Addop;
        if(Addop!=null) Addop.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public SignedTerm getSignedTerm() {
        return SignedTerm;
    }

    public void setSignedTerm(SignedTerm SignedTerm) {
        this.SignedTerm=SignedTerm;
    }

    public Addop getAddop() {
        return Addop;
    }

    public void setAddop(Addop Addop) {
        this.Addop=Addop;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SignedTerm!=null) SignedTerm.accept(visitor);
        if(Addop!=null) Addop.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SignedTerm!=null) SignedTerm.traverseTopDown(visitor);
        if(Addop!=null) Addop.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SignedTerm!=null) SignedTerm.traverseBottomUp(visitor);
        if(Addop!=null) Addop.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AddopExpr(\n");

        if(SignedTerm!=null)
            buffer.append(SignedTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Addop!=null)
            buffer.append(Addop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AddopExpr]");
        return buffer.toString();
    }
}

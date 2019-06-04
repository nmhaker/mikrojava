// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class InstArrayProduction extends InstArray {

    private InstPrimitive InstPrimitive;
    private Expr Expr;

    public InstArrayProduction (InstPrimitive InstPrimitive, Expr Expr) {
        this.InstPrimitive=InstPrimitive;
        if(InstPrimitive!=null) InstPrimitive.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public InstPrimitive getInstPrimitive() {
        return InstPrimitive;
    }

    public void setInstPrimitive(InstPrimitive InstPrimitive) {
        this.InstPrimitive=InstPrimitive;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InstPrimitive!=null) InstPrimitive.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InstPrimitive!=null) InstPrimitive.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InstPrimitive!=null) InstPrimitive.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InstArrayProduction(\n");

        if(InstPrimitive!=null)
            buffer.append(InstPrimitive.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InstArrayProduction]");
        return buffer.toString();
    }
}

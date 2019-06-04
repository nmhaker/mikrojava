// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class PrintStmtProduction extends PrintStmt {

    private Expr Expr;
    private PrintParam PrintParam;

    public PrintStmtProduction (Expr Expr, PrintParam PrintParam) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.PrintParam=PrintParam;
        if(PrintParam!=null) PrintParam.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public PrintParam getPrintParam() {
        return PrintParam;
    }

    public void setPrintParam(PrintParam PrintParam) {
        this.PrintParam=PrintParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(PrintParam!=null) PrintParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(PrintParam!=null) PrintParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(PrintParam!=null) PrintParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStmtProduction(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PrintParam!=null)
            buffer.append(PrintParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintStmtProduction]");
        return buffer.toString();
    }
}

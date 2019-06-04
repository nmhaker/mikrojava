// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclProduction extends ConstDecl {

    private Type Type;
    private ConstInitializationList ConstInitializationList;

    public ConstDeclProduction (Type Type, ConstInitializationList ConstInitializationList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ConstInitializationList=ConstInitializationList;
        if(ConstInitializationList!=null) ConstInitializationList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ConstInitializationList getConstInitializationList() {
        return ConstInitializationList;
    }

    public void setConstInitializationList(ConstInitializationList ConstInitializationList) {
        this.ConstInitializationList=ConstInitializationList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ConstInitializationList!=null) ConstInitializationList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ConstInitializationList!=null) ConstInitializationList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ConstInitializationList!=null) ConstInitializationList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclProduction(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstInitializationList!=null)
            buffer.append(ConstInitializationList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclProduction]");
        return buffer.toString();
    }
}

// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class MethDeclProduction extends MethDecl {

    private Type Type;
    private String I2;
    private ParameterList ParameterList;
    private VarDeclList VarDeclList;
    private StatementList StatementList;
    private OptionalReturn OptionalReturn;

    public MethDeclProduction (Type Type, String I2, ParameterList ParameterList, VarDeclList VarDeclList, StatementList StatementList, OptionalReturn OptionalReturn) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.I2=I2;
        this.ParameterList=ParameterList;
        if(ParameterList!=null) ParameterList.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
        this.OptionalReturn=OptionalReturn;
        if(OptionalReturn!=null) OptionalReturn.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public ParameterList getParameterList() {
        return ParameterList;
    }

    public void setParameterList(ParameterList ParameterList) {
        this.ParameterList=ParameterList;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public OptionalReturn getOptionalReturn() {
        return OptionalReturn;
    }

    public void setOptionalReturn(OptionalReturn OptionalReturn) {
        this.OptionalReturn=OptionalReturn;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ParameterList!=null) ParameterList.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
        if(OptionalReturn!=null) OptionalReturn.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ParameterList!=null) ParameterList.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
        if(OptionalReturn!=null) OptionalReturn.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ParameterList!=null) ParameterList.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        if(OptionalReturn!=null) OptionalReturn.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethDeclProduction(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(ParameterList!=null)
            buffer.append(ParameterList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalReturn!=null)
            buffer.append(OptionalReturn.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethDeclProduction]");
        return buffer.toString();
    }
}

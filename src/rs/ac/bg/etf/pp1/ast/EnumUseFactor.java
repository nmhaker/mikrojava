// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class EnumUseFactor extends Factor {

    private EnumUse EnumUse;

    public EnumUseFactor (EnumUse EnumUse) {
        this.EnumUse=EnumUse;
        if(EnumUse!=null) EnumUse.setParent(this);
    }

    public EnumUse getEnumUse() {
        return EnumUse;
    }

    public void setEnumUse(EnumUse EnumUse) {
        this.EnumUse=EnumUse;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumUse!=null) EnumUse.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumUse!=null) EnumUse.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumUse!=null) EnumUse.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumUseFactor(\n");

        if(EnumUse!=null)
            buffer.append(EnumUse.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumUseFactor]");
        return buffer.toString();
    }
}

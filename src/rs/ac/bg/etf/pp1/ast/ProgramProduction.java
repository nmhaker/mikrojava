// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class ProgramProduction extends Program {

    private ProgName ProgName;
    private GlobalDeclList GlobalDeclList;
    private MethDeclList MethDeclList;

    public ProgramProduction (ProgName ProgName, GlobalDeclList GlobalDeclList, MethDeclList MethDeclList) {
        this.ProgName=ProgName;
        if(ProgName!=null) ProgName.setParent(this);
        this.GlobalDeclList=GlobalDeclList;
        if(GlobalDeclList!=null) GlobalDeclList.setParent(this);
        this.MethDeclList=MethDeclList;
        if(MethDeclList!=null) MethDeclList.setParent(this);
    }

    public ProgName getProgName() {
        return ProgName;
    }

    public void setProgName(ProgName ProgName) {
        this.ProgName=ProgName;
    }

    public GlobalDeclList getGlobalDeclList() {
        return GlobalDeclList;
    }

    public void setGlobalDeclList(GlobalDeclList GlobalDeclList) {
        this.GlobalDeclList=GlobalDeclList;
    }

    public MethDeclList getMethDeclList() {
        return MethDeclList;
    }

    public void setMethDeclList(MethDeclList MethDeclList) {
        this.MethDeclList=MethDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ProgName!=null) ProgName.accept(visitor);
        if(GlobalDeclList!=null) GlobalDeclList.accept(visitor);
        if(MethDeclList!=null) MethDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ProgName!=null) ProgName.traverseTopDown(visitor);
        if(GlobalDeclList!=null) GlobalDeclList.traverseTopDown(visitor);
        if(MethDeclList!=null) MethDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ProgName!=null) ProgName.traverseBottomUp(visitor);
        if(GlobalDeclList!=null) GlobalDeclList.traverseBottomUp(visitor);
        if(MethDeclList!=null) MethDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgramProduction(\n");

        if(ProgName!=null)
            buffer.append(ProgName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalDeclList!=null)
            buffer.append(GlobalDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethDeclList!=null)
            buffer.append(MethDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgramProduction]");
        return buffer.toString();
    }
}

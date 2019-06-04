// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class MultipleParameterListProduction extends ParameterList {

    private Parameter Parameter;
    private ParameterList ParameterList;

    public MultipleParameterListProduction (Parameter Parameter, ParameterList ParameterList) {
        this.Parameter=Parameter;
        if(Parameter!=null) Parameter.setParent(this);
        this.ParameterList=ParameterList;
        if(ParameterList!=null) ParameterList.setParent(this);
    }

    public Parameter getParameter() {
        return Parameter;
    }

    public void setParameter(Parameter Parameter) {
        this.Parameter=Parameter;
    }

    public ParameterList getParameterList() {
        return ParameterList;
    }

    public void setParameterList(ParameterList ParameterList) {
        this.ParameterList=ParameterList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Parameter!=null) Parameter.accept(visitor);
        if(ParameterList!=null) ParameterList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Parameter!=null) Parameter.traverseTopDown(visitor);
        if(ParameterList!=null) ParameterList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Parameter!=null) Parameter.traverseBottomUp(visitor);
        if(ParameterList!=null) ParameterList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultipleParameterListProduction(\n");

        if(Parameter!=null)
            buffer.append(Parameter.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ParameterList!=null)
            buffer.append(ParameterList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultipleParameterListProduction]");
        return buffer.toString();
    }
}

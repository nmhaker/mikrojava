// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class SingleParameterListProduction extends ParameterList {

    private Parameter Parameter;

    public SingleParameterListProduction (Parameter Parameter) {
        this.Parameter=Parameter;
        if(Parameter!=null) Parameter.setParent(this);
    }

    public Parameter getParameter() {
        return Parameter;
    }

    public void setParameter(Parameter Parameter) {
        this.Parameter=Parameter;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Parameter!=null) Parameter.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Parameter!=null) Parameter.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Parameter!=null) Parameter.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleParameterListProduction(\n");

        if(Parameter!=null)
            buffer.append(Parameter.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleParameterListProduction]");
        return buffer.toString();
    }
}

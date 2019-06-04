// generated with ast extension for cup
// version 0.8
// 4/5/2019 16:7:44


package rs.ac.bg.etf.pp1.ast;

public class ArrayFactor extends Factor {

    private MyArray MyArray;

    public ArrayFactor (MyArray MyArray) {
        this.MyArray=MyArray;
        if(MyArray!=null) MyArray.setParent(this);
    }

    public MyArray getMyArray() {
        return MyArray;
    }

    public void setMyArray(MyArray MyArray) {
        this.MyArray=MyArray;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MyArray!=null) MyArray.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MyArray!=null) MyArray.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MyArray!=null) MyArray.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ArrayFactor(\n");

        if(MyArray!=null)
            buffer.append(MyArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ArrayFactor]");
        return buffer.toString();
    }
}

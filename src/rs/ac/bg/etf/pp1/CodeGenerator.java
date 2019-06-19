package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.BoolFactor;
import rs.ac.bg.etf.pp1.ast.CharFactor;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.FuncCall;
import rs.ac.bg.etf.pp1.ast.IdentDesignator;
import rs.ac.bg.etf.pp1.ast.MethBegin;
import rs.ac.bg.etf.pp1.ast.MethDecl;
import rs.ac.bg.etf.pp1.ast.MyArray;
import rs.ac.bg.etf.pp1.ast.MyArrayDesignator;
import rs.ac.bg.etf.pp1.ast.NumberFactor;
import rs.ac.bg.etf.pp1.ast.PrintCall;
import rs.ac.bg.etf.pp1.ast.PrintParamProduction;
import rs.ac.bg.etf.pp1.ast.PrintStmtProduction;
import rs.ac.bg.etf.pp1.ast.ReadCall;
import rs.ac.bg.etf.pp1.ast.ReturnProduction;
import rs.ac.bg.etf.pp1.ast.VarUse;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class CodeGenerator extends VisitorAdaptor {
	
	private int mainPc = 0;
	public int getMainPc() {
		return mainPc;
	}
	
	public void visit(PrintStmtProduction printStmtProd) {
		if(printStmtProd.getExpr().struct == Tab.intType) {
			Code.loadConst(printParam);
			Code.put(Code.print);
		}else {
			Code.loadConst(printParam);
			Code.put(Code.bprint);
		}
	}
	
	int printParam = 0;
	public void visit(PrintParamProduction printParamProd) {
		printParam = printParamProd.getN1();
	}
	
	public void visit(ReadCall readCall) {
		if(readCall.getDesignator().obj.getType() == Tab.intType) {
			Code.put(Code.read);
		}else {
			Code.put(Code.bread);
		}
		Code.store(readCall.getDesignator().obj);
	}

	/*
	public void visit(IdentDesignator identDesignator) {
		Code.load(identDesignator.obj);
	}
	
	public void visit(MyArrayDesignator myArrayDesignator) {
		Code.load(myArrayDesignator.obj);
	}
	*/	
	
	public void visit(NumberFactor numberFactor) {
		Code.loadConst(numberFactor.getN1());
	}
	
	public void visit(VarUse varUse) {
		Code.load(Tab.find(varUse.getVarName()));
	}

	public void visit(MyArray myArray) {
		Code.load(Tab.find(myArray.getArrName()));
	}
	
	public void visit(CharFactor charFactor) {
		Code.loadConst(Integer.parseInt(charFactor.getC1()));
	}
	
	public void visit(BoolFactor boolFactor) {
		Code.loadConst(boolFactor.getB1() == true ? 1 : 0);
	}
	
	public void visit(MethBegin methBegin) {
		if(methBegin.getMethodName().equalsIgnoreCase("main")) {
			mainPc = Code.pc;
		}
		methBegin.obj.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(methBegin.obj.getLevel());
		Code.put(methBegin.obj.getLocalSymbols().size());
	}
	
	public void visit(MethDecl methDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(FuncCall funcCall) {
		Code.put(Code.call);
		Code.put(Tab.find(funcCall.getFuncName()).getAdr() - Code.pc);
	}
	
}

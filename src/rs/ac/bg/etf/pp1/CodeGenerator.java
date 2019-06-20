package rs.ac.bg.etf.pp1;

import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.AddopExpr;
import rs.ac.bg.etf.pp1.ast.BoolFactor;
import rs.ac.bg.etf.pp1.ast.CharFactor;
import rs.ac.bg.etf.pp1.ast.DesStatAssignment;
import rs.ac.bg.etf.pp1.ast.DesStatDec;
import rs.ac.bg.etf.pp1.ast.DesStatInc;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.DivMulop;
import rs.ac.bg.etf.pp1.ast.EnumUse;
import rs.ac.bg.etf.pp1.ast.FuncCall;
import rs.ac.bg.etf.pp1.ast.FuncCallName;
import rs.ac.bg.etf.pp1.ast.IdentDesignator;
import rs.ac.bg.etf.pp1.ast.InstArrayProduction;
import rs.ac.bg.etf.pp1.ast.MethBegin;
import rs.ac.bg.etf.pp1.ast.MethCall;
import rs.ac.bg.etf.pp1.ast.MethDecl;
import rs.ac.bg.etf.pp1.ast.MinusAddop;
import rs.ac.bg.etf.pp1.ast.ModMulop;
import rs.ac.bg.etf.pp1.ast.MulMulop;
import rs.ac.bg.etf.pp1.ast.MulopTerm;
import rs.ac.bg.etf.pp1.ast.MyArray;
import rs.ac.bg.etf.pp1.ast.MyArrayDesignator;
import rs.ac.bg.etf.pp1.ast.MyArrayName;
import rs.ac.bg.etf.pp1.ast.NumberFactor;
import rs.ac.bg.etf.pp1.ast.PlusAddop;
import rs.ac.bg.etf.pp1.ast.PrintCall;
import rs.ac.bg.etf.pp1.ast.PrintParamProduction;
import rs.ac.bg.etf.pp1.ast.PrintStmtProduction;
import rs.ac.bg.etf.pp1.ast.ReadCall;
import rs.ac.bg.etf.pp1.ast.ReturnProduction;
import rs.ac.bg.etf.pp1.ast.VarUse;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

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
	
	private int varBytes = 0;
	public void visit(VarUse varUse) {
		if(varUse.obj.getKind() == Obj.Var) {
			int oldPc = Code.pc;
			Code.load(varUse.obj);
			varBytes = Code.pc - oldPc;
		}else if(varUse.obj.getKind() == Obj.Elem) {
			varBytes = 0;
			Code.load(new Obj(Obj.Var, varUse.obj.getName(), varUse.obj.getType(), varUse.obj.getAdr(), varUse.obj.getLevel()));
		}else 
			Code.load(varUse.obj);
	}
	
	public void visit(MyArrayName myArrayName) {
		Obj arrayObjName = new Obj(Obj.Var, myArrayName.obj.getName(), myArrayName.obj.getType().getElemType(), myArrayName.obj.getAdr(), myArrayName.obj.getLevel());
		Code.load(arrayObjName);
	}

	private int arrayBytes = 0;
	private Struct myArrayType = null;
	public void visit(MyArray myArray) {
		int oldPc = Code.pc;
		Code.load(myArray.obj);
		varBytes = Code.pc - oldPc;
		myArrayType = myArray.obj.getType();
	}
	
	public void visit(CharFactor charFactor) {
		Code.loadConst(charFactor.getC1().charAt(0));
	}
	
	public void visit(BoolFactor boolFactor) {
		Code.loadConst(boolFactor.getB1() == true ? 1 : 0);
	}
	

	private byte operator = 0;
	public void visit(PlusAddop plusAddop) {
		operator = Code.add;
	}
	public void visit(MinusAddop minusAddop) {
		operator = Code.sub;
	}
	public void visit(MulMulop mulMulop) {
		operator = Code.mul;
	}
	public void visit(DivMulop divMulop) {
		operator = Code.div;
	}
	public void visit(ModMulop modMulop) {
		operator = Code.rem;
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
	
	private String funcName = null;
	public void visit(FuncCallName funcCallName) {
		funcName = funcCallName.getFuncName();
	}
	
	public void visit(FuncCall funcCall) {
		int offset = funcCall.obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	public void visit(MethCall methCall) {
		if(methCall.getFuncCall().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}
	
	public void visit(MulopTerm mulopTerm) {
		Code.put(operator);
	}
	
	public void visit(AddopExpr addopExpr) {
		Code.put(operator);
	}
	
	public void visit(DesStatAssignment designatorStatAssign) {
		Obj designatorObj = designatorStatAssign.getDesignator().obj;
		if(!instantiationHappened)
			Code.store(designatorObj);
		else {
			Code.store(new Obj(Obj.Var, designatorObj.getName(), designatorObj.getType(), designatorObj.getAdr(), designatorObj.getLevel()));
			instantiationHappened = false;
		}
	}
	
	public void visit(DesStatInc desStatInc) {
		Code.pc+=varBytes;
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.store(desStatInc.getDesignator().obj);
	}

	public void visit(DesStatDec desStatDec) {
		Code.pc+=varBytes;
		Code.put(Code.const_1);
		Code.put(Code.sub);
		Code.store(desStatDec.getDesignator().obj);
	}
	
	public void visit(EnumUse enumUse) {
		Code.load(enumUse.obj);
	}
	
	public void visit(IdentDesignator identDesignator) {
		Code.pc-=varBytes;
	}
	
	
	public void visit(MyArrayDesignator myArrDesignator) {
		Code.pc-=varBytes;
	}

	private boolean instantiationHappened = false;
	public void visit(InstArrayProduction instArrayProduction) {
		Code.put(Code.newarray);
		if(myArrayType == Tab.intType)
			Code.put(1);
		else if(myArrayType == Tab.charType)
			Code.put(0);
		else
			Code.put(1);
		instantiationHappened = true;
	}
	
}

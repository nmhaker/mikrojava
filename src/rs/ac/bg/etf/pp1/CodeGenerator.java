package rs.ac.bg.etf.pp1;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.SemanticAnalyzer.RelOperators;
import rs.ac.bg.etf.pp1.ast.AbstractCondition;
import rs.ac.bg.etf.pp1.ast.AbstractConditionalStatementProduction;
import rs.ac.bg.etf.pp1.ast.AddopExpr;
import rs.ac.bg.etf.pp1.ast.BoolFactor;
import rs.ac.bg.etf.pp1.ast.BreakProduction;
import rs.ac.bg.etf.pp1.ast.CharFactor;
import rs.ac.bg.etf.pp1.ast.CondFactBoolProduction;
import rs.ac.bg.etf.pp1.ast.CondFactProduction;
import rs.ac.bg.etf.pp1.ast.CondTermListProduction;
import rs.ac.bg.etf.pp1.ast.CondTermProduction;
import rs.ac.bg.etf.pp1.ast.ConditionListProduction;
import rs.ac.bg.etf.pp1.ast.ConditionProduction;
import rs.ac.bg.etf.pp1.ast.ConditionalIfStatementProduction;
import rs.ac.bg.etf.pp1.ast.ContinueProduction;
import rs.ac.bg.etf.pp1.ast.ConditionalIfElseStatementProduction;
import rs.ac.bg.etf.pp1.ast.DesStatAssignment;
import rs.ac.bg.etf.pp1.ast.DesStatDec;
import rs.ac.bg.etf.pp1.ast.DesStatInc;
import rs.ac.bg.etf.pp1.ast.Designator;
import rs.ac.bg.etf.pp1.ast.DivMulop;
import rs.ac.bg.etf.pp1.ast.EnumUse;
import rs.ac.bg.etf.pp1.ast.ForLoopStatementProduction;
import rs.ac.bg.etf.pp1.ast.ElseStatementProduction;
import rs.ac.bg.etf.pp1.ast.FuncCall;
import rs.ac.bg.etf.pp1.ast.FuncCallName;
import rs.ac.bg.etf.pp1.ast.GroupFactor;
import rs.ac.bg.etf.pp1.ast.PreHookUpCallProduction;
import rs.ac.bg.etf.pp1.ast.IdentDesignator;
import rs.ac.bg.etf.pp1.ast.IfStatementProduction;
import rs.ac.bg.etf.pp1.ast.InstArrayInitNodeProduction;
import rs.ac.bg.etf.pp1.ast.InstArrayProduction;
import rs.ac.bg.etf.pp1.ast.InstPrimitiveTypeProduction;
import rs.ac.bg.etf.pp1.ast.MethBegin;
import rs.ac.bg.etf.pp1.ast.MethCall;
import rs.ac.bg.etf.pp1.ast.MethDecl;
import rs.ac.bg.etf.pp1.ast.MinusAddop;
import rs.ac.bg.etf.pp1.ast.MinusTerm;
import rs.ac.bg.etf.pp1.ast.ModMulop;
import rs.ac.bg.etf.pp1.ast.MulMulop;
import rs.ac.bg.etf.pp1.ast.MulopTerm;
import rs.ac.bg.etf.pp1.ast.MyArray;
import rs.ac.bg.etf.pp1.ast.MyArrayDesignator;
import rs.ac.bg.etf.pp1.ast.MyArrayName;
import rs.ac.bg.etf.pp1.ast.NumberFactor;
import rs.ac.bg.etf.pp1.ast.OptionalForConditionEpsilonProd;
import rs.ac.bg.etf.pp1.ast.OptionalForConditionProduction;
import rs.ac.bg.etf.pp1.ast.OptionalForPostconditionEpsilonProd;
import rs.ac.bg.etf.pp1.ast.OptionalForPostconditionProduction;
import rs.ac.bg.etf.pp1.ast.OptionalForPreconditionEpsilonProd;
import rs.ac.bg.etf.pp1.ast.OptionalForPreconditionProduction;
import rs.ac.bg.etf.pp1.ast.PlusAddop;
import rs.ac.bg.etf.pp1.ast.PrintCall;
import rs.ac.bg.etf.pp1.ast.PrintParamProduction;
import rs.ac.bg.etf.pp1.ast.PrintStmtProduction;
import rs.ac.bg.etf.pp1.ast.ReadCall;
import rs.ac.bg.etf.pp1.ast.RelOpEqProduction;
import rs.ac.bg.etf.pp1.ast.RelOpGrProduction;
import rs.ac.bg.etf.pp1.ast.RelOpGreProduction;
import rs.ac.bg.etf.pp1.ast.RelOpLsProduction;
import rs.ac.bg.etf.pp1.ast.RelOpLseProduction;
import rs.ac.bg.etf.pp1.ast.RelOpNeProduction;
import rs.ac.bg.etf.pp1.ast.ReturnProduction;
import rs.ac.bg.etf.pp1.ast.StartOfForLoopProduction;
import rs.ac.bg.etf.pp1.ast.StartOfIfStmtProduction;
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
		if(readCall.getDesignator().obj.getType() == Tab.intType || (readCall.getDesignator().obj.getType().getKind() == Struct.Array && readCall.getDesignator().obj.getType().getElemType() == Tab.intType)) {
			Code.put(Code.read);
		}else {
			Code.put(Code.bread);
		}
		Obj desigObj = readCall.getDesignator().obj;
		if(VAR_array)
			Code.store(desigObj);
		else
			Code.store(new Obj(Obj.Elem, desigObj.getName(), desigObj.getType(), desigObj.getAdr(), desigObj.getLevel()));
	}
	
	public void visit(NumberFactor numberFactor) {
		Code.loadConst(numberFactor.getN1());
	}
	
	private int varBytes = 0;
	public void visit(VarUse varUse) {
		if(varUse.obj.getKind() == Obj.Var && varUse.obj.getType().getKind() != Struct.Array) {
			int oldPc = Code.pc;
			Code.load(varUse.obj);
			varBytes = Code.pc - oldPc;
		}else if(varUse.obj.getKind() == Obj.Var && varUse.obj.getType().getKind() == Struct.Array) {
			varBytes = 0;
			Code.load(new Obj(Obj.Var, varUse.obj.getName(), varUse.obj.getType(), varUse.obj.getAdr(), varUse.obj.getLevel()));
		}else 
			Code.load(varUse.obj);
	}
	
	public void visit(MyArrayName myArrayName) {
		Obj arrayObjName = new Obj(Obj.Var, myArrayName.obj.getName(), myArrayName.obj.getType().getElemType(), myArrayName.obj.getAdr(), myArrayName.obj.getLevel());
		Code.load(arrayObjName);
	}

	public void visit(MyArray myArray) {
		int oldPc = Code.pc;
		Code.load(new Obj(Obj.Elem, myArray.obj.getName(), myArray.obj.getType(), myArray.obj.getAdr(), myArray.obj.getLevel()));
		varBytes = Code.pc - oldPc;
	}
	
	public void visit(CharFactor charFactor) {
		Code.loadConst(charFactor.getC1().charAt(1));
	}
	
	public void visit(BoolFactor boolFactor) {
		Code.loadConst(boolFactor.getB1() == true ? 1 : 0);
	}
	
	private Stack<Byte> addOpStack = new Stack<>();
	private Stack<Byte> mulOpStack = new Stack<>();

	public void visit(PlusAddop plusAddop) {
		addOpStack.add((byte) Code.add);
	}
	public void visit(MinusAddop minusAddop) {
		addOpStack.add((byte) Code.sub);
	}
	public void visit(MulMulop mulMulop) {
		mulOpStack.add((byte)Code.mul);
	}
	public void visit(DivMulop divMulop) {
		mulOpStack.add((byte)Code.div);
	}
	public void visit(ModMulop modMulop) {
		mulOpStack.add((byte)Code.rem);
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
	
	public void visit(MinusTerm minTerm) {
		Code.put(Code.neg);
	}
	
	public void visit(MulopTerm mulopTerm) {
		Code.put(mulOpStack.pop());
	}
	
	public void visit(AddopExpr addopExpr) {
		Code.put(addOpStack.pop());
	}
	
	private boolean VAR_array = true;
	public void visit(DesStatAssignment designatorStatAssign) {
		Obj designatorObj = designatorStatAssign.getDesignator().obj;
		if(!instantiationHappened) {
			if(!VAR_array) 
				Code.store(new Obj(Obj.Elem, designatorObj.getName(), designatorObj.getType(), designatorObj.getAdr(), designatorObj.getLevel()));
			else
				Code.store(designatorObj);
		} else {
			instantiationHappened = false;
		}
	}
	
	public void visit(DesStatInc desStatInc) {
		Code.pc+=varBytes;
		if(!VAR_array) {
			Code.pc-=1;
			Code.put(Code.dup2);
			Code.load(new Obj(Obj.Elem, desStatInc.getDesignator().obj.getName(), desStatInc.getDesignator().obj.getType(), desStatInc.getDesignator().obj.getAdr(), desStatInc.getDesignator().obj.getLevel()));
		}
		Code.put(Code.const_1);
		Code.put(Code.add);
		if(VAR_array)
			Code.store(desStatInc.getDesignator().obj);
		else 
			Code.store(new Obj(Obj.Elem, desStatInc.getDesignator().obj.getName(), desStatInc.getDesignator().obj.getType(), desStatInc.getDesignator().obj.getAdr(), desStatInc.getDesignator().obj.getLevel()));
	}

	public void visit(DesStatDec desStatDec) {
		Code.pc+=varBytes;
		if(!VAR_array) {
			Code.pc -= 1;
			Code.put(Code.dup2);
			Code.load(new Obj(Obj.Elem, desStatDec.getDesignator().obj.getName(), desStatDec.getDesignator().obj.getType(), desStatDec.getDesignator().obj.getAdr(), desStatDec.getDesignator().obj.getLevel()));
		}
		Code.put(Code.const_1);
		Code.put(Code.sub);
		if(VAR_array)
			Code.store(desStatDec.getDesignator().obj);
		else {
			Code.store(new Obj(Obj.Elem, desStatDec.getDesignator().obj.getName(), desStatDec.getDesignator().obj.getType(), desStatDec.getDesignator().obj.getAdr(), desStatDec.getDesignator().obj.getLevel()));
		}
	}
	
	public void visit(EnumUse enumUse) {
		Code.load(enumUse.obj);
	}
	
	Obj designatorObj = null;
	public void visit(IdentDesignator identDesignator) {
		Code.pc-=varBytes;
		VAR_array = true;
		designatorObj = identDesignator.obj;
	}
	
	
	public void visit(MyArrayDesignator myArrDesignator) {
		VAR_array = false;
		Code.pc-=varBytes;
		designatorObj = myArrDesignator.obj;
	}

	private int initializer_counter = 0;
	public void visit(PreHookUpCallProduction hookUpCallProduction) {
		Code.put(Code.getstatic);
		Code.put2(designatorObj.getAdr());
		Code.put(Code.const_);
		Code.put4(initializer_counter++);
	}
	
	public void visit(InstArrayInitNodeProduction instArrayInitNodeProduction) {
		if (designatorObj.getType().getElemType().getKind() == Struct.Char) Code.put(Code.bastore);
        else Code.put(Code.astore); 
	}
	
	public void visit(InstArrayProduction instArrayProduction) {
		initializer_counter = 0;
	}
	
	private boolean instantiationHappened = false;
	public void visit(InstPrimitiveTypeProduction instPrimTypeProd) {
		Code.put(Code.newarray);
		if(instPrimTypeProd.struct.getElemType().getKind() == Struct.Int)
			Code.put(1);
		else if(instPrimTypeProd.struct.getElemType().getKind() == Struct.Char)
			Code.put(0);
		else
			Code.put(1);
		Code.store(new Obj(Obj.Var, designatorObj.getName(), designatorObj.getType(), designatorObj.getAdr(), designatorObj.getLevel()));
		instantiationHappened = true;
	}
	
	
	// KONTROLNE STRUKTURE

	
	// OBRADA CONDITION-a 
		// Za AND se radi identicno kao i da nema AND, preskace if i ide u else blok ili van
	    // Za OR je potrebno izmeniti adrese kompletnog AND-a da skace na sledecu OR strukturu
	    // dok u slucaju da je AND struktura tacna onda posto smo u OR-u to znaci da
	    // ne treba da proveravamo dalje pa se poslednja naredba menja u skok na IF blok

		public void visit(AbstractCondition abstractCondition) {
			for(Integer addr: backPatchingForIfBlock) {
				Code.fixup(addr);
			}
			backPatchingForIfBlock.clear();
		}
		
		public void visit(AbstractConditionalStatementProduction abstractCondStmtProd) {
			if(!stackForLastBackPatchingOrAddresses.empty())
				stackForLastBackPatchingOrAddresses.pop();
			if(!stackForIfJumpOutAddresses.empty())
				stackForIfJumpOutAddresses.pop();
		}

		private Stack<Stack<Integer>> stackForIfJumpOutAddresses = new Stack<Stack<Integer>>();
		private Stack<List<Integer>> stackForLastBackPatchingOrAddresses = new Stack<List<Integer>>();

		public void visit(StartOfIfStmtProduction startOfIfStmtProd) {
			stackForLastBackPatchingOrAddresses.push(new LinkedList<Integer>());
			stackForIfJumpOutAddresses.push(new Stack<Integer>());
		}
		
		private List<Integer> lastBackPatchingOrAddressesRelOp = new LinkedList<Integer>();
		private List<Integer> backPatchingForIfBlock = new LinkedList<Integer>();
		
		// Here we handle always the one AND PRODUCTION before current one, because the last one should not be touched
		public void visit(ConditionListProduction conditionListProduction) {

			//Backpatch jump address of AND STATEMENT to NEXT OR CONDITION
			for(Integer addr : stackForLastBackPatchingOrAddresses.peek()) {
				Code.buf[addr] = (byte) ( (lastConditionPC-addr+1) >> 8);
				Code.buf[addr+1] = (byte) (lastConditionPC-addr+1);
			}

			//Also change last CONDTERM in AND STATEMENT to JUMP INTO IF SECTION AND REVERSE THE CONDITION
			Integer lastAndAddr = stackForLastBackPatchingOrAddresses.peek().get(0);
			int relOp = lastBackPatchingOrAddressesRelOp.get(0); // It is already inverted by putFalseJump, so place original		                         
			Code.buf[lastAndAddr-1] = (byte)( Code.jcc + relOp);
			Code.buf[lastAndAddr] = (byte) 0;
			Code.buf[lastAndAddr+1] = (byte) 0;
			// If this condition succeeds then we need to jump all other condition and go right into IF block
			backPatchingForIfBlock.add(lastAndAddr);
			
			// Change slots new => old
			stackForLastBackPatchingOrAddresses.peek().clear();
			lastBackPatchingOrAddressesRelOp.clear();
			while(backPatchingAddresses.size() > 0) { // This makes addresses reverse ordered
				int addr = backPatchingAddresses.pop();
				stackForLastBackPatchingOrAddresses.peek().add(addr);
				relOp = backPatchingOrAddressesRelOp.pop();
				lastBackPatchingOrAddressesRelOp.add(relOp);
			}
			lastConditionPC = Code.pc;
		}
		
		int lastConditionPC;
		// We delay to ConditionProductionList to handle
		public void visit(ConditionProduction conditionProduction) {
			stackForLastBackPatchingOrAddresses.peek().clear();
			lastBackPatchingOrAddressesRelOp.clear();
			while(backPatchingAddresses.size() > 0) {
				int addr = backPatchingAddresses.pop();
				stackForLastBackPatchingOrAddresses.peek().add(addr);
				int relOp = backPatchingOrAddressesRelOp.pop();
				lastBackPatchingOrAddressesRelOp.add(relOp);
			}
			lastConditionPC = Code.pc;
		}
	
		// Ovaj stack sluzi za backpatching svega unutar if-a
		private Stack<Integer> backPatchingAddresses = new Stack<Integer>();
		private Stack<Integer> backPatchingOrAddressesRelOp = new Stack<Integer>();
		public void visit(CondFactProduction condFactProduction) {
			Code.putFalseJump(relOp, 0);
			backPatchingAddresses.push(Code.pc-2);
			backPatchingOrAddressesRelOp.push(relOp);
		}
		
		public void visit(CondFactBoolProduction condFactBoolProd) {
			Code.loadConst(1);
			Code.putFalseJump(Code.eq, 0);
			backPatchingAddresses.push(Code.pc-2);
			backPatchingOrAddressesRelOp.push(Code.eq);
		}
		
		// backpatchujemo sve instrukcije koje treba da preskoce IF blok
		public void visit(IfStatementProduction ifStatProd) {
			for(Integer addr : stackForLastBackPatchingOrAddresses.peek()) {
				Code.fixup(addr);
			}
		}
		
		// Ovaj stack cuva adrese za backpatching svega unutar if-a za +3 jer gadja  jmp else-sa umesto instrukciju posle njega
		public void visit(ElseStatementProduction elseStatProd) {
			// Bezuslovni skok za preskakanje ELSE bloka
			Code.putJump(0);
			stackForIfJumpOutAddresses.peek().push(Code.pc-2);
			//Peglanje za +3 preko ovog jump-a
			for(Integer addr : stackForLastBackPatchingOrAddresses.peek()) {
				Code.buf[addr+1] += 3;
			}
		}
		
		// Ovde ispravimo instrukciju JMP koja preskace ELSE blok 
		public void visit(ConditionalIfElseStatementProduction p) {
			Code.fixup(stackForIfJumpOutAddresses.peek().pop());
		}
		
		int relOp = -1;
		public void visit(RelOpEqProduction r) {
			relOp = Code.eq;
		}
		public void visit(RelOpNeProduction r) {
			relOp = Code.ne;
		}
		public void visit(RelOpGrProduction r) {
			relOp = Code.gt;
		}
		public void visit(RelOpGreProduction r) {
			relOp = Code.ge;
		}
		public void visit(RelOpLsProduction r) {
			relOp = Code.lt;
		}
		public void visit(RelOpLseProduction r) {
			relOp = Code.le;
		}	
			
	// FOR LOOP
		
		public void visit(ForLoopStatementProduction forLoopStmtProd) {
			
			Code.putJump(postAddrStack.peek());
			
			for(Integer addr : stackForLastBackPatchingOrAddresses.peek()) {
				Code.fixup(addr);
			}
			
			for(Integer addr : stackForBreakStack.peek()) {
				Code.fixup(addr);
			}
			
			stackForLastBackPatchingOrAddresses.pop();
			stackForIfJumpOutAddresses.pop();					
			stackForBreakStack.pop();
			
			condAddrStack.pop();
			postAddrStack.pop();
		}
		
		public void visit(StartOfForLoopProduction startOfForLoopProd) {	
			stackForLastBackPatchingOrAddresses.push(new LinkedList<Integer>());
			stackForIfJumpOutAddresses.push(new Stack<Integer>());
			stackForBreakStack.push(new Stack<Integer>());
		}

		private Stack<Integer> condAddrStack = new Stack<Integer>();
		public void visit(OptionalForPreconditionProduction optForPrecProd) {
			condAddrStack.push(Code.pc);
		}
		
		public void visit(OptionalForPreconditionEpsilonProd optForPrecondEpsProd) {
			condAddrStack.push(Code.pc);
		}
		
		private Stack<Integer> postAddrStack = new Stack<Integer>();
		private Stack<Integer> jumpInForStack = new Stack<Integer>();
		public void visit(OptionalForConditionProduction optForCondProd) {
			
			// Za skakanje u petlju jer ce Condition vec da izgenerise Uslov za skok van petlje
			Code.putJump(0);
			jumpInForStack.push(Code.pc-2);

			postAddrStack.push(Code.pc);
		}
		
		public void visit(OptionalForConditionEpsilonProd optForCondEpsPro) {
			Code.putJump(0);
			jumpInForStack.push(Code.pc-2);

			postAddrStack.push(Code.pc);
		}
		
		public void visit(OptionalForPostconditionProduction optForPostProd) {
			
			Code.putJump(condAddrStack.peek());
			
			Code.fixup(jumpInForStack.pop());

		}	
		
		public void visit(OptionalForPostconditionEpsilonProd optForPostcondEpsProd) {

			Code.putJump(condAddrStack.peek());
			
			Code.fixup(jumpInForStack.pop());

		}
		

		private Stack<Stack<Integer>> stackForBreakStack = new Stack<Stack<Integer>>();
		public void visit(BreakProduction breakProd) {
			Code.putJump(0);
			stackForBreakStack.peek().push(Code.pc-2);
		}

		public void visit(ContinueProduction contProd) {
			Code.putJump(postAddrStack.peek());
		}
}

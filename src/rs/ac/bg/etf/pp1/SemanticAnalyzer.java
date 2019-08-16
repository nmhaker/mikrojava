package rs.ac.bg.etf.pp1;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

public class SemanticAnalyzer extends VisitorAdaptor {

	private Boolean errorDetected = false;
	
	Logger log = Logger.getLogger(getClass());
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if(line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}
	
	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if(line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public boolean passed() {
		return !errorDetected;
	}
		
	// KONSTRUKTOR ZA UBACIVANJE BOOL TIPA U UNIVERSE OPSEG
	public SemanticAnalyzer() {
		super();
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", new Struct(Struct.Bool), Obj.NO_VALUE, Obj.NO_VALUE));
	}
	
	private Obj methodScopeFind(String name) {
		Obj resultObj = null;
		Scope s = Tab.currentScope();
		if (s.getLocals() != null) {
				resultObj = s.getLocals().searchKey(name);
		}
		return (resultObj != null) ? resultObj : Tab.noObj;
	}
	
	// DEFINICIJE SIMBOLA
		
		// PROGRAM DEFINICIJA

			// Otvaram glavni scope
			public void visit(ProgName progName) {
				// Set program object to be available
				progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
				// Open program scope
				Tab.openScope();

				report_info("Deklarisano ime programa: " + progName.getProgName(), progName);
			}
			
			// Kada se obidje ovaj cvor, zavrsio se program
			boolean mainDefined = false;
			public void visit(Program program) {
				// Chain symbols and close program scope
				Tab.chainLocalSymbols(program.getProgName().obj);
				Tab.closeScope();
				
				if(!mainDefined) {
					report_error("Nije definisana main metoda", program);
					errorDetected = true;
				}
			}
			
		// PAMCENJE PODATAKA O TIPU NECEGA GENERALNO
			
			// Zapamtimo ime tipa i cvor kad god se nesto definise
			Struct currentType = null;
			String currentTypeName = null;
			public void visit(TypeProduction typeProduction) {
				Obj typeObj = Tab.find(typeProduction.getTypeName());
				currentTypeName = typeProduction.getTypeName();
				if(typeObj == Tab.noObj) {
					if(typeProduction.getTypeName().equals("void")) {
						currentType = Tab.noType;
					}else {
						report_error("Greska ne postoji tip: " + typeProduction.getTypeName(), typeProduction);
						errorDetected = true;
					}
				}else {
					currentType = typeObj.getType();
				}
			}
			
		// DEFINISANJE PROMENLJIVIH
		private int numOfGlobalVarsDefined = 0;
		private int numOfLocalVarsDefined = 0;
		
			// Detektujemo definicije promenljivih
			public void visit(SimpleVarDecl simpleVarDecl) {
				String varName = simpleVarDecl.getVarName();
				if(methodBeingDefined) {
					if(methodScopeFind(varName) == Tab.noObj) {
						Tab.insert(Obj.Var, varName, currentType);
						if(methName != null && methName.equals("main")) numOfLocalVarsDefined++;
						report_info("Definisana lokalna promenljiva: " + currentTypeName + " " + varName, simpleVarDecl);
					}else {
						report_error("Visestruko definisanje simbola: " + varName, simpleVarDecl);
						errorDetected = true;
					}
				}else {
					if(Tab.find(varName) == Tab.noObj) {
						Tab.insert(Obj.Var, varName, currentType);
						//Tab.find(varName).setAdr(numOfGlobalVarsDefined+numOfGlobalArraysDefined);
						numOfGlobalVarsDefined++;
						report_info("Definisana globalna promenljiva: " + currentTypeName + " " + varName, simpleVarDecl);
					}else {
						report_error("Visestruko definisanje simbola: " + varName, simpleVarDecl);
						errorDetected = true;
					}
				}
			}
			
		// DEFINISANJE METODA
		private int numOfMethodsDefined = 0;
		
		private boolean methodBeingDefined = false;

			// Zapamtimo tip kojim smo definisali metodu za kasnije
			private Struct methType = null;
			private String methTypeName = null;
			public void visit(VoidMethType vmt) {
				methTypeName = currentTypeName = "void";
				methType = Tab.noType;
				currentType = Tab.noType;
			}
			public void visit(NonVoidMethType nvmt){
				methTypeName = currentTypeName;
				methType = currentType;
			}
			
			private String methName = null;
			public void visit(MethBegin methBegin) {
				returnRegistered = false;
				numOfParameters = 0;
				methodBeingDefined = true;
				methName = methBegin.getMethodName();
				methBegin.obj = Tab.insert(Obj.Meth, methName, currentType);
				Tab.openScope();
			}
			
			private int numOfParameters = 0;
			
			public void visit(ParameterVarProduction paramVarProd) {
				Tab.insert(Obj.Var, paramVarProd.getParamName(), currentType);
				Tab.find(paramVarProd.getParamName()).setAdr(numOfParameters);
				numOfParameters++;
			}

			public void visit(ParameterArrayProduction paramArrayProd) {
				Tab.insert(Obj.Var, declaredArrayName, currentType);
				Tab.find(declaredArrayName).setAdr(numOfParameters);
				numOfParameters++;
			}
		
			public void visit(MethDecl methDecl) {
				Tab.chainLocalSymbols(methDecl.getMethBegin().obj);
				Tab.closeScope();
				// Main method must be void, and 0 parameters
				if(methName.equals("main")) {
					if(!methTypeName.equals("void")) {
						report_error("Metoda MAIN nije definisana kao VOID !", methDecl);
						errorDetected = true;
					}
					if(numOfParameters != 0) {
						report_error("Metoda MAIN ima argumente, ocekivano 0 !", methDecl);
						errorDetected = true;
					}
					mainDefined = true;
				}
				// Expecting return statement
				if(!methTypeName.equals("void")) {
					if(!returnRegistered) {
						report_error("Metoda "+methName+" ne sadrzi RETURN iskaz!", methDecl);
						errorDetected = true;
					}
				}else { // VOID type method
					if(returnRegistered) {
						if(returnExprRegistered) {
							report_error("Return iskaz vraca izraz u metodi tipa VOID ! ", methDecl);
							errorDetected = true;
						}
					}
				}
				// Set num of parameters in Obj node
				methDecl.getMethBegin().obj.setLevel(numOfParameters);
				methodBeingDefined = false;
				numOfMethodsDefined++;
				report_info("Deklarisana metoda: " + methTypeName +  " " + methName , methDecl);
			}
			
			// Detekcija RETURN iskaza
			boolean returnRegistered = false;
			public void visit(ReturnStatement retStmt) {
				returnRegistered = true;
			}
			
			boolean returnExprRegistered = false;
			public void visit(OptionalExprProduction optExprProd) {
				// Proveri tip povratne vrednosti da li se slaze sa return izrazom
				if(!methType.equals(exprType)) {
						report_error("Povratna vrednost metode se ne slaze sa return izrazom!", optExprProd);
						errorDetected = true;
				}
			}
			
		// DEFINISANJE NIZOVA
		private int numOfLocalArraysDefined = 0;
		private int numOfGlobalArraysDefined = 0;

			// Detektujemo definiciju niza
			private String declaredArrayName = null;
			public void visit(ArrayDecl arrayDecl) {
				String arrName = arrayDecl.getArrayName();
				declaredArrayName = arrName;
				if(methodBeingDefined) {
					if(methodScopeFind(arrName) != Tab.noObj ) {
						report_error("Visestruko definisanje simbola: " + arrName, arrayDecl);
						errorDetected = true;
						return;
					}
					Tab.insert(Obj.Var, arrName, new Struct(Struct.Array, currentType));
					//Tab.insert(Obj.Elem, arrName, new Struct(Struct.Array, currentType));
					if(methName != null && methName.equals("main")) numOfLocalArraysDefined++;
					report_info("Definisan lokalni niz: " + currentTypeName + " " + arrayDecl.getArrayName(), arrayDecl);
				}else {
					if(Tab.find(arrName) != Tab.noObj ) {
						report_error("Visestruko definisanje simbola: " + arrName, arrayDecl);
						errorDetected = true;
						return;
					}
					Tab.insert(Obj.Var, arrName, new Struct(Struct.Array, currentType));
					//Tab.insert(Obj.Elem, arrName, new Struct(Struct.Array, currentType));
					//Tab.find(arrName).setAdr(numOfGlobalArraysDefined+numOfGlobalVarsDefined);
					numOfGlobalArraysDefined++;
					report_info("Definisan globalni niz: " + currentTypeName + " " + arrayDecl.getArrayName(), arrayDecl);
				}
			}
		
		// DEFINISANJE ENUMERACIJA 
		private int numOfEnumerationsDefined = 0;

			// Dohvatam vrednost simbolicke konstante
			int enumConstantValue = 0;
			public void visit(EnumConstValue ecv){
				enumConstantValue = ecv.getEnumConstantValue();
			}

			List<Integer> queue = null;
			// Ubacam simbolicku konstantu kao novi objektni cvor u strukturni cvor
			public void visit(EnumExpr enumExpr) {
				if(queue.contains(enumConstantValue)) report_error("Greska pri definisanju konstante enum-a, konstanta: " + enumConstantValue +" vec postoji !", enumExpr);
				queue.add(enumConstantValue);
				Obj constant = new Obj(Obj.Con, enumExpr.getEnumConstantName(), Tab.intType, enumConstantValue++, 0);
				Tab.currentScope.addToLocals(constant);
			}
			
			// Otvaram opseg i kreiram objektni cvor kao i strukturni
			public void visit(EnumBegin enumBegin) {
				if(Tab.find(enumBegin.getEnumName()) != Tab.noObj) {
					report_error("Visestruko definisanje simbola: " + enumBegin.getEnumName(), enumBegin);
					errorDetected = true;
					return;
				}
				enumBegin.obj = new Obj(Obj.Type, enumBegin.getEnumName(), new Struct(Struct.Enum, Tab.intType), -1, 0);
				Tab.currentScope.addToLocals(enumBegin.obj);
				Tab.openScope();
				queue = new LinkedList<Integer>();
			}
			
			// Ulancavam simbolicke konstante 
			public void visit(EnumDecl enumDecl) {
				enumDecl.getEnumBegin().obj.getType().setMembers(Tab.currentScope().getLocals());
				Tab.closeScope();
				numOfEnumerationsDefined++;
				report_info("Definisano nabrajanje: " + enumDecl.getEnumBegin().getEnumName()  , enumDecl);
				queue.clear();
				enumConstantValue = 0;
			}
			
		//	DEFINISANJE KONSTANTI	
		private int numOfConstantsDefined = 0;
			
			// Ulancavanje konstanti
			String constIdent = null;

			public void visit(ConstExprNumber constExpr) {
				// Proveri postojece ime u tabeli simbola
				if(Tab.find(constExpr.getConstIdent()) != Tab.noObj) {
					report_error("Visestruko definisanje simbola: " + constExpr.getConstIdent(), constExpr);
					errorDetected = true;
					return;
				}
				constIdent = constExpr.getConstIdent();
				if(!currentType.assignableTo(Tab.intType)) {
					report_error("ERROR : greska prilikom definisanja konstanti, TYPE MISMATCH", constExpr);
				}else {
					numOfConstantsDefined++;
					report_info("Definisana konstanta: " + constIdent, constExpr);
				}
				Obj obj = new Obj(Obj.Con, constIdent, Tab.intType, constExpr.getConstValue(), 0);
				Tab.currentScope().addToLocals(obj);
			}
			public void visit(ConstExprBool constExpr) {
				// Proveri postojece ime u tabeli simbola
				if(Tab.find(constExpr.getConstIdent()) != Tab.noObj) {
					report_error("Visestruko definisanje simbola: " + constExpr.getConstIdent(), constExpr);
					errorDetected = true;
					return;
				}
				constIdent = constExpr.getConstIdent();
				if(!currentType.assignableTo(Tab.find("bool").getType())) {
					report_error("ERROR : greska prilikom definisanja konstanti, TYPE MISMATCH", constExpr);
				}else {
					numOfConstantsDefined++;
					report_info("Definisana konstanta: " + constIdent, constExpr);
				}
				Obj obj = new Obj(Obj.Con, constIdent, Tab.find("bool").getType(), constExpr.getConstValue() == true ? 1 : 0, 0);
				Tab.currentScope().addToLocals(obj);
			}
			public void visit(ConstExprChar constExpr) {
				// Proveri postojece ime u tabeli simbola
				if(Tab.find(constExpr.getConstIdent()) != Tab.noObj) {
					report_error("Visestruko definisanje simbola: " + constExpr.getConstIdent(), constExpr);
					errorDetected = true;
					return;
				}
				constIdent = constExpr.getConstIdent();
				if(!currentType.assignableTo(Tab.charType)) {
					report_error("ERROR : greska prilikom definisanja konstanti, TYPE MISMATCH", constExpr);
				}else {
					numOfConstantsDefined++;
					report_info("Definisana konstanta: " + constIdent, constExpr);
				}
				Obj obj = new Obj(Obj.Con, constIdent, Tab.charType, constExpr.getConstValue().charAt(1), 0);
				Tab.currentScope().addToLocals(obj);
			}
			

	// DETEKCIJA UPOTREBE SIMBOLA
			

		// Detekcija upotrebe promenljive 
		private String usingVarName = null;
		private Struct usingVarType = null;

		private boolean leftSideIsEnum = false;
		private Struct leftEnumType = null;
		private boolean varWasArrayType = false;

		public void visit(VarUse varUse) {
			MySTDump mystdump = new MySTDump();
			usingVarName = varUse.getVarName();
			Obj varObj = Tab.find(usingVarName);
			if(varObj == Tab.noObj) {
				report_error("Nedefinisan simbol: " + usingVarName, varUse);
				errorDetected = true;
			}
			usingVarType = varObj.getType();
			// To be aware if array identifier is being use without brackets
			if(usingVarType.getKind() == Struct.Array)
				varWasArrayType = true;
			else 
				varWasArrayType = false;
			//Za slucaj kad imamo niz koji je tipa Enum, potrebno je jedan nivo apstrakcije izvuci iz tipa
			if(usingVarType.getKind() == Struct.Array && usingVarType.getElemType().getKind()==Struct.Enum) {
				usingVarType = usingVarType.getElemType();
			}
			if(!isRightSideOfAssignment && (usingVarType.getKind() == Struct.Enum) ) {
				leftSideIsEnum = true;
				leftEnumType = usingVarType;
			}else if(isRightSideOfAssignment && (usingVarType.getKind() == Struct.Enum)) {
				rightEnumType = usingVarType;
			}
			if(usingVarType.getKind() == Struct.Enum) {
				usingVarType = Tab.intType;
			}
			varUse.obj = varObj;
			mystdump.visitObjNode(Tab.find(varUse.getVarName()));
			report_info("Detektovano koriscenje simbola: " + mystdump.getOutput() , varUse);
		}
			
		// Detekcija upotrebe niza
		private String usingArrName = null;
		private Struct usingArrType = null;
		public void visit(MyArray myArray) {
			MySTDump mystdump = new MySTDump();
			usingArrName = myArray.getMyArrayName().getArrName();
			Obj o = Tab.find(usingArrName);
			if(o == Tab.noObj) {
				report_error("Ne postoji definisan simbol: " , myArray);
				errorDetected = true;
			}
			usingArrType = o.getType();
			if(usingArrType.getKind() != Struct.Array) {
				report_error("Pokusavate da koristite promenljivu kao niz !", myArray);
				errorDetected = true;
			}else {
				usingArrType = usingArrType.getElemType();
				if(exprType.getKind() != Struct.Int) {
					report_error("Greska prilikom indeksiranja niza, konstanta nije Integer!" , myArray);
					errorDetected = true;
				}
				if(!isRightSideOfAssignment && (usingArrType.getKind() == Struct.Enum)) {
					leftEnumType = usingArrType;
					leftSideIsEnum = true;
				}else if(isRightSideOfAssignment && (usingArrType.getKind() == Struct.Enum)) {
					rightEnumType = usingArrType;
				}else
					leftSideIsEnum = false;
				if(usingArrType.getKind() == Struct.Enum) {
					usingArrType = Tab.intType;
				}
			}
			myArray.obj = Tab.find(myArray.getMyArrayName().getArrName());
			mystdump.visitObjNode(Tab.find(myArray.getMyArrayName().getArrName()));
			report_info("Detektovano koriscenje simbola: " + mystdump.getOutput(), myArray );
		}
		
		public void visit(MyArrayName myArrayName) {
			myArrayName.obj = Tab.find(myArrayName.getArrName());
		}

		// Detekcija upotreba Enum-a
		private String usingEnumName = null;
		private Struct usingEnumType = null;
		public void visit(EnumUse enumUse) {
			String enumName = enumUse.getEnumName();
			String enumValue = enumUse.getEnumConst();
			usingEnumName = enumName;
			usingEnumType = Tab.find(enumName).getType();
			enumUse.obj = Tab.find(enumName).getType().getMembersTable().searchKey(enumValue);
			MySTDump mystdump = new MySTDump();
			mystdump.visitObjNode(Tab.find(enumName));
			report_info("Detektovano koriscenje simbola: " + enumValue + " iz: " + mystdump.getOutput(), enumUse);
		}
		
	// SEMANTIKA ISKAZA
		
		private boolean isRightSideOfAssignment = false;
		// Provera ++ i -- operatora nad INT tipom
		private Struct designatorType = null;
		public void visit(IdentDesignator identDesignator) {
			designatorType = usingVarType;
			identDesignator.obj = Tab.find(usingVarName);
			isRightSideOfAssignment = true;
		}
		public void visit(MyArrayDesignator myArrayDesignator) {
			designatorType = usingArrType;
			myArrayDesignator.obj = Tab.find(usingArrName);
			isRightSideOfAssignment = true;
		}
		public void visit(DesStatInc desStatInc) {
			if(designatorType.getKind() != Struct.Int) {
				report_error("Koristite ,,++,, operator nad non-Integer tipom", desStatInc);
				errorDetected = true;
			}
		}
		public void visit(DesStatDec desStatDec) {
			if(designatorType.getKind() != Struct.Int) {
				report_error("Koristite ,,--,, operator nad non-Integer tipom", desStatDec);
				errorDetected = true;
			}
		}
		public void visit(DesStatAssignment desStatAssignment) {
			if(instantiationHappened) {
				if(leftSideIsEnum) {
					if(!leftEnumType.compatibleWith(instPrimExprType)) {
						report_error("Tip kojim se instancira niz nije isti kao tip niza", desStatAssignment);
						errorDetected = true;
					}
				}else if(!designatorType.getElemType().compatibleWith(instPrimExprType)) {
					report_error("Tip kojim se instancira niz nije isti kao tip niza", desStatAssignment);
					errorDetected = true;
				}
			}else if(!designatorType.compatibleWith(exprType)) {
				report_error("Designator nije kompatibilan sa Expr-om !", desStatAssignment);
				errorDetected = true;
			}
			if(leftSideIsEnum && !instantiationHappened) {
				if(!leftEnumType.equals(rightEnumType)) {
					report_error("Designator enum-a nije kompatibilan sa Expr-om", desStatAssignment);
					errorDetected = true;
				}
			}
			leftSideIsEnum = false;
			leftEnumType = null;
			rightEnumType = null;
			isRightSideOfAssignment = false;
			instantiationHappened = false;
		}
		
		private boolean instantiationHappened = false;
		public void visit(InstFactor instFactor) {
			instantiationHappened = true;
		}
		
		// readCall metoda ocekuje int/char/bool tip argumenta
		public void visit(ReadCall readCall) {
			if( (designatorType.getKind() != Struct.Int) && 
			   (designatorType.getKind() != Struct.Char) && 
			   (designatorType.getKind() != Struct.Bool) ){
				report_error("Argument readCall metode nije odgovarajuceg tipa; Podrzani su: int, char, bool", readCall);
				errorDetected = true;
			}
			report_info("Detektovan poziv READ metode", readCall);
		}
		
		// printCall metoda ocekuje int/char/bool Expr
		public void visit(PrintCall printCall) {
			if( (exprType.getKind() != Struct.Int) && 
			   (exprType.getKind() != Struct.Char) && 
			   (exprType.getKind() != Struct.Bool) ){
				report_error("Argument printCall metode nije odgovarajuceg tipa; Podrzani su: int, char, bool", printCall);
				errorDetected = true;
			}
			report_info("Detektovan poziv PRINT metode", printCall);
		}
	
		private Integer argCounter = 0;
		private Stack<Integer> argCounterStack = new Stack<Integer>();
		private List<Struct> argTypes = new LinkedList<Struct>();
		private Stack<List<Struct>> argTypesStack = new Stack<List<Struct>>();
		
		public void visit(ArgumentListExprProduction argListExprProd) {
			prepareArg();
		}
		
		public void visit(ArgumentListProduction argListProd) {
			prepareArg();
		}
		public void prepareArg() {
			argCounter = argCounterStack.pop();
			argCounter = argCounter + 1;
			argCounterStack.push(argCounter);
			argTypes = argTypesStack.pop();
			argTypes.add(exprType);
			argTypesStack.push(argTypes);
		}
		
		// Obrada poziva funkcije
		private Struct funcType = null;
		private String funcName = null;
		
		private Stack<String> funcNameStack = new Stack<String>();
		public void visit(FuncCall funcCall) {
			funcName = funcNameStack.pop();
			Obj funcObj = Tab.find(funcName);
			funcType = funcObj.getType();
			report_info("Detektovan poziv metode: " + funcName, funcCall);
			if(Tab.find(funcName) == Tab.noObj){
				report_error("Ne postoji definisana metoda: ", funcCall);
				errorDetected = true;
			}
			argCounter = argCounterStack.pop();
			if(Tab.find(funcName).getLevel()!=argCounter) {
				report_error("Broj argumenata se ne slaze sa definicijom metode", funcCall);
				errorDetected = true;
			}
			Object[] paramIter =  funcObj.getLocalSymbols().toArray();
			int i = 0;
			argTypes = argTypesStack.pop();
			for(Struct t : argTypes){
				Obj o = (Obj)paramIter[i++];
				if(!t.assignableTo(o.getType())){
					report_error("Argument nije dodeljiv parametru , tipovi se ne slazu!", funcCall);
					errorDetected = true;
				}
			}
			funcCall.obj = funcObj;
		}
		
		public void visit(FuncCallName funcCallName) {
			funcName = funcCallName.getFuncName();
			funcNameStack.push(funcName);
			argCounterStack.push(new Integer(0));
			argTypesStack.push(new LinkedList<Struct>());
		}
		
		// Obrada TERM-a 
		
		private Struct termType = null;
		public void visit(FactorTerm factorTerm) {
			termType = factorType;
			if(leftSideIsEnum && !enumFactorUsed) {
				report_error("Koristite ne nabrojivu konstantu u dodeli za enum", factorTerm);
				errorDetected = true;
			}
		}
		public void visit(MulopTerm mulopTerm) {
			// Za proveru nizova koji su tipa int ( Int, Enum )
			if(factorType.getKind() == Struct.Array) {
				if( (!factorType.getElemType().equals(Tab.intType)) && (!(factorType.getElemType().getKind() == Struct.Enum))) {
					report_error("Mnozenje sa non-Integer tipom", mulopTerm);
					errorDetected = true;
				}
			}else if(factorType.getKind() == Struct.Enum){
				// Ovo je u redu jer su nabrojive konstante uvek tipa Integer
				// Ali moram da ga izdvojim jer nije tip Int ali je u redu
			}else {
				// Za ostale slucajeve 
				if(!factorType.equals(Tab.intType)) {
					report_error("Mnozenje sa non-Integer tipom", mulopTerm);
					errorDetected = true;
				}
			}
			if(leftSideIsEnum && isRightSideOfAssignment) {
				report_error("Ne mogu se koristiti izrazi u dodeli Enum-u, samo je dozvoljena nabrojiva konstanta njegovog tipa", mulopTerm);
				errorDetected = true;
			}
		}
		
		public void visit(MinusTerm minusTerm) {
			if(!termType.equals(Tab.intType)) {
				report_error("Ne moze se koristiti ,,-,, za non-Integer tip", minusTerm);
				errorDetected = true;
			}
		}
		
		
		// Obrada EXPR-a
		
		private Struct exprType = null;
		
		public void visit(TermExpr termExpr) {
			exprType = termType;
			termExpr.struct = exprType;
		}
		
		public void visit(AddopExpr addopExpr) {
			// Za proveru nizova koji su tipa int ( Int, Enum )
			if(termType.getKind() == Struct.Array) {
				if(!termType.getElemType().equals(Tab.intType) && !(termType.getElemType().getKind() == Struct.Enum)) {
					report_error("Mnozenje sa non-Integer tipom", addopExpr);
					errorDetected = true;
				}
			}else if(termType.getKind() == Struct.Enum){
				// Ovo je u redu jer su nabrojive konstante uvek tipa Integer
				// Ali moram da ga izdvojim jer nije tip Int ali je u redu
			}else {
				// Za ostale slucajeve 
				if(!termType.equals(Tab.intType)) {
					report_error("Mnozenje sa non-Integer tipom", addopExpr);
					errorDetected = true;
				}
			}	
			if(leftSideIsEnum) {
				report_error("Ne mogu se koristiti izrazi za dodelu Enum-u", addopExpr);
				errorDetected = true;
			}
			addopExpr.struct = termType;
		}
		
		
		// ADDOP , MULOP
		
		public void visit(PlusAddop plusAddop)
		{
//			report_info("PLUS ADDOP", plusAddop);
		}
		public void visit(MinusAddop minusAddop)
		{
//			report_info("MINUS ADDOP", minusAddop);
		}
		public void visit(MulMulop mulMulop) 
		{
//			report_info("MUL MULOP", mulMulop);
		}
		public void visit(DivMulop divMulop) 
		{
//			report_info("DIV MULOP", divMulop);
		}
		public void visit(ModMulop modMulop)
		{
//			report_info("MOD MULOP", modMulop);
		}
		
	// POSTAVLJANJE TIPA FAKTORA

		private Struct factorType = null;
		@Override
		public void visit(IdentFactor identFactor) {
			factorType = usingVarType;
		}
		
		@Override
		public void visit(FuncCallFactor FuncCallFactor) {
			factorType = funcType;
		}

		private Struct rightEnumType = null;
		private boolean enumFactorUsed = false;
		@Override
		public void visit(EnumUseFactor EnumUseFactor) {
			factorType = Tab.intType;
			rightEnumType = usingEnumType;
			enumFactorUsed = true;
		}

		@Override
		public void visit(ArrayFactor ArrayFactor) {
			factorType = usingArrType;
		}
		
		private Integer numOfInitializators = 0;
		public void visit(InstArrayInitNodeProduction instArrInitNodeProd) {
			if(!exprType.assignableTo(instPrimExprType)) {
				report_error("Greska tip inicijalizatora se ne slaze sa tipom niza!", instArrInitNodeProd);
				errorDetected = true;
			}
			numOfInitializators++;
		}
		
		private boolean initializatorListExist = false;
		public void visit(InstArrayInitProduction instArrInitProd) {
			initializatorListExist = true;
		}

		private boolean instantiatedWithEnum = false;
		private Struct instPrimExprType = null;
		public void visit(InstPrimitiveProduction instPrim) {
			instPrimExprType = factorType = currentType;
			if(currentType.getKind() == Struct.Enum) {
				instantiatedWithEnum = true;
				enumFactorUsed = true;
			}
		}
		
		private Integer sizeOfArray = null;
		private Struct instPrimExprConstType = null;
		public void visit(InstPrimitiveTypeProduction instPrimitiveType) {
			instPrimExprConstType = factorType;
			sizeOfArray = valOfNumberFactor;
			factorType = new Struct(Struct.Array, currentType);
			instPrimitiveType.struct = factorType;
		}
		
		public void visit(InstArrayProduction instArrayProduction) {
			if(instPrimExprConstType.getKind() != Struct.Int) {
				report_error("Greska u instanciranju niza, konstanta za velicinu niza nije Integer! ", instArrayProduction);
				errorDetected = true;
			}
			if(initializatorListExist && (numOfInitializators != sizeOfArray)) {
				report_error("Greska broj inicijalizatora se ne slaze sa velicinom niza", instArrayProduction);
				errorDetected = true;
			}
			// Da bih mogao u CodeGenerator-u da znam koliko je veliki niz za inicijalizatorsku listu
			instArrayProduction.obj = new Obj(Obj.NO_VALUE, "value_holder", Tab.noType);
			instArrayProduction.obj.setFpPos(sizeOfArray);
			numOfInitializators = 0;
			initializatorListExist = false;
		}

		@Override
		public void visit(BoolFactor BoolFactor) {
			factorType = Tab.find("bool").getType();
		}

		@Override
		public void visit(CharFactor CharFactor) {
			factorType = Tab.charType;
		}

		private Integer valOfNumberFactor = null;
		@Override
		public void visit(NumberFactor numberFactor) {
			factorType = Tab.intType;
			valOfNumberFactor = numberFactor.getN1();
		}
		
		public void visit(GroupFactor groupFactor) {
			factorType = exprType;
		}
		
	// KONTROLNE STRUKTURE
		public void visit(CondFactBoolProduction condFactBoolProd) {
			if(exprType.getKind() != Struct.Bool) {
				report_error("Izraz iskoriscen za ispitivanje uslova nije promenljiva BOOL tipa", condFactBoolProd);
				errorDetected = true;
			}
		}
	
		private Struct firstCondFactType = null;
		private boolean firstCondWasArray = false;
		public void visit(FirstCondFactProduction firstCondFact) {
			firstCondFactType = exprType;
			firstCondWasArray = varWasArrayType;	
		}

		private Struct secondCondFactType = null;
		private boolean secondCondWasArray = false;
		public void visit(SecondCondFactProduction secondCondFact) {
			secondCondFactType = exprType;
			secondCondWasArray = varWasArrayType;
		}

		public void visit(CondFactProduction condFactProd) {
			if(!firstCondFactType.compatibleWith(secondCondFactType)) {
				report_error("Tipovi korisceni u IF-u nisu kompatibilni", condFactProd);
				errorDetected = true;
			}
			if(firstCondWasArray || secondCondWasArray) {
				if(!(firstCondWasArray && secondCondWasArray)) {
					report_error("Unutar IF-a potrebno je da oba iskaza budu nizovi", condFactProd);
					errorDetected = true;
				}else {
					if( !(relOp == RelOperators.EQ || relOp == RelOperators.NE) ) {
						report_error("Podrzani operatori za ispitivanje nizova su  ,,==,, i ,,!=,, ", condFactProd);
						errorDetected = true;
					}
				}
			}
		}
		
		
		// RELACIONI OPERATORI

			enum RelOperators { EQ, NE, LS, LSE, GR, GRE } 
			RelOperators relOp;

			public void visit(RelOpEqProduction r) {
				relOp = RelOperators.EQ;
			}
			public void visit(RelOpNeProduction r) {
				relOp = RelOperators.NE;
			}
			public void visit(RelOpGrProduction r) {
				relOp = RelOperators.GR;
			}
			public void visit(RelOpGreProduction r) {
				relOp = RelOperators.GRE;
			}
			public void visit(RelOpLsProduction r) {
				relOp = RelOperators.LS;
			}
			public void visit(RelOpLseProduction r) {
				relOp = RelOperators.LSE;
			}
	
	
	// Syntax info getters
	public int getNumOfGlobalVariables() {
		return numOfGlobalVarsDefined;
	}
	public int getNumOfLocalVariables() {
		return numOfLocalVarsDefined;
	}
	public int getNumOfGlobalArrays() {
		return numOfGlobalArraysDefined;
	}
	public int getNumOfEnumerations() {
		return numOfEnumerationsDefined;
	}
	public int getNumOfLocalArrays() {
		return numOfLocalArraysDefined;
	}
	public int getNumOfConstants() {
		return numOfConstantsDefined;
	}
	public int getNumOfMethods() {
		return numOfMethodsDefined;
	}

}


























































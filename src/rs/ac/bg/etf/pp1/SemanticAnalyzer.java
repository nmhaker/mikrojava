package rs.ac.bg.etf.pp1;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
				numOfParameters++;
			}

			public void visit(ParameterArrayProduction paramArrayProd) {
				Tab.insert(Obj.Var, declaredArrayName, currentType);
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
					if(methName != null && methName.equals("main")) numOfLocalArraysDefined++;
					report_info("Definisan lokalni niz: " + currentTypeName + " " + arrayDecl.getArrayName(), arrayDecl);
				}else {
					if(Tab.find(arrName) != Tab.noObj ) {
						report_error("Visestruko definisanje simbola: " + arrName, arrayDecl);
						errorDetected = true;
						return;
					}
					Tab.insert(Obj.Var, arrName, new Struct(Struct.Array, currentType));
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
				Obj obj = new Obj(Obj.Con, constIdent, new Struct(Struct.Bool), constExpr.getConstValue() == true ? 1 : 0, 0);
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
				Obj obj = new Obj(Obj.Con, constIdent, Tab.charType, constExpr.getConstValue().charAt(1), 0);
				Tab.currentScope().addToLocals(obj);
			}

			// Detektovanje definisane konstante
			public void visit(ConstDecl constDecl) {
				numOfConstantsDefined++;
				report_info("Definisana konstanta: " + constIdent, constDecl);
			}

	// DETEKCIJA UPOTREBE SIMBOLA
			

		// Detekcija upotrebe promenljive 
		private String usingVarName = null;
		private Struct usingVarType = null;

		private boolean leftSideIsEnum = false;
		private Struct leftEnumType = null;

		public void visit(VarUse varUse) {
			MySTDump mystdump = new MySTDump();
			usingVarName = varUse.getVarName();
			Obj varObj = Tab.find(usingVarName);
			if(varObj == Tab.noObj) {
				report_error("Nedefinisan simbol: " + usingVarName, varUse);
				errorDetected = true;
			}
			usingVarType = varObj.getType();
			if(!isRightSideOfAssignment && (usingVarType.getKind() == Struct.Enum) ) {
				leftSideIsEnum = true;
				leftEnumType = usingVarType;
			}else if(isRightSideOfAssignment && (usingVarType.getKind() == Struct.Enum)) {
				rightEnumType = usingVarType;
			}
			if(usingVarType.getKind() == Struct.Enum) {
				usingVarType = Tab.intType;
			}
			mystdump.visitObjNode(Tab.find(varUse.getVarName()));
			report_info("Detektovano koriscenje simbola: " + mystdump.getOutput() , varUse);
		}
			
		// Detekcija upotrebe niza
		private String usingArrName = null;
		private Struct usingArrType = null;
		public void visit(MyArray myArray) {
			MySTDump mystdump = new MySTDump();
			usingArrName = myArray.getArrName();
			usingArrType = Tab.find(usingArrName).getType();
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
				}
				if(usingArrType.getKind() == Struct.Enum) {
					usingArrType = Tab.intType;
				}
			}
			mystdump.visitObjNode(Tab.find(myArray.getArrName()));
			report_info("Detektovano koriscenje simbola: " + mystdump.getOutput(), myArray );
		}

		// Detekcija upotreba Enum-a
		private String usingEnumName = null;
		private Struct usingEnumType = null;
		public void visit(EnumUse enumUse) {
			String enumName = enumUse.getEnumName();
			String enumValue = enumUse.getEnumConst();
			usingEnumName = enumName;
			usingEnumType = Tab.find(enumName).getType();
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
			isRightSideOfAssignment = true;
		}
		public void visit(MyArrayDesignator myArrayDesignator) {
			designatorType = usingArrType;
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
			if(!designatorType.compatibleWith(exprType)) {
				report_error("Designator nije kompatibilan sa Expr-om !", desStatAssignment);
				errorDetected = true;
			}
			if(leftSideIsEnum) {
				if(!leftEnumType.equals(rightEnumType)) {
					report_error("Designator enum-a nije kompatibilan sa Expr-om", desStatAssignment);
					errorDetected = true;
				}
			}
			leftSideIsEnum = false;
			leftEnumType = null;
			rightEnumType = null;
			isRightSideOfAssignment = false;
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
		
		// Obrada poziva funkcije
		private Struct funcType = null;
		public void visit(FuncCall funcCall) {
			funcType = Tab.find(funcCall.getFuncName()).getType();
			report_info("Detektovan poziv metode: " + funcCall.getFuncName(), funcCall);
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
			if(leftSideIsEnum) {
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
		private boolean identFactorPassed = false;
		@Override
		public void visit(IdentFactor identFactor) {
			factorType = usingVarType;
			identFactorPassed = true;
		}
		
		private boolean funcCallFactorPassed = false;
		@Override
		public void visit(FuncCallFactor FuncCallFactor) {
			factorType = funcType;
			funcCallFactorPassed = true;
		}

		private Struct rightEnumType = null;
		private boolean enumFactorUsed = false;
		@Override
		public void visit(EnumUseFactor EnumUseFactor) {
			factorType = Tab.intType;
			rightEnumType = usingEnumType;
			enumFactorUsed = true;
		}

		private boolean arrayFactorPassed = false;
		@Override
		public void visit(ArrayFactor ArrayFactor) {
			factorType = usingArrType;
			arrayFactorPassed  = true;
		}

		private boolean instPrimitiveFactorPassed = false;
		public void visit(InstPrimitive instPrim) {
			factorType = currentType;
			instPrimitiveFactorPassed = true;
		}
		
		private boolean instArrayProdFactorPassed = false;
		public void visit(InstArrayProduction instArrayProduction) {
			if(exprType.getKind() != Struct.Int) {
				report_error("Greska u instanciranju niza, konstanta za velicinu niza nije Integer! ", instArrayProduction);
				errorDetected = true;
			}
			instArrayProdFactorPassed = true;
		}

		private boolean boolFactorPassed = false;
		@Override
		public void visit(BoolFactor BoolFactor) {
			factorType = new Struct(Struct.Bool);
			boolFactorPassed = true;
		}

		private boolean charFactorPassed = false;
		@Override
		public void visit(CharFactor CharFactor) {
			factorType = Tab.charType;
			charFactorPassed = true;
		}

		private boolean numberFactorPassed = false;
		@Override
		public void visit(NumberFactor NumberFactor) {
			factorType = Tab.intType;
			numberFactorPassed = true;
		}
		
		private boolean groupFactorPassed = false;
		public void visit(GroupFactor groupFactor) {
			factorType = exprType;
			groupFactorPassed = true;
		}

	// Syntax info getters
	public int getNumOfGlobalVariables() {
		return numOfGlobalVarsDefined;
	}
	public int getNumOfLocalVariables() {
		return numOfGlobalVarsDefined;
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


























































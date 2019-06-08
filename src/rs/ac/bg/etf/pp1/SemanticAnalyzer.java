package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

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
			private String methType = null;
			public void visit(VoidMethType vmt) {
				methType = currentTypeName = "void";
				currentType = Tab.noType;
			}
			public void visit(NonVoidMethType nvmt){
				methType = currentTypeName;
			}
			
			private String methName = null;
			public void visit(MethBegin methBegin) {
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
				if(methName.equals("main")) {
					if(!methType.equals("void")) {
						report_error("Metoda MAIN nije definisana kao VOID !", methDecl);
						errorDetected = true;
					}
					if(numOfParameters != 0) {
						report_error("Metoda MAIN ima argumente, ocekivano 0 !", methDecl);
						errorDetected = true;
					}
					mainDefined = true;
				}
				methodBeingDefined = false;
				numOfMethodsDefined++;
				report_info("Deklarisana metoda: " + methType +  " " + methName , methDecl);
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
					Tab.insert(Obj.Var, arrName, new Struct(Struct.Array, Tab.intType));
					if(methName != null && methName.equals("main")) numOfLocalArraysDefined++;
					report_info("Definisan lokalni niz: " + currentTypeName + " " + arrayDecl.getArrayName(), arrayDecl);
				}else {
					if(Tab.find(arrName) != Tab.noObj ) {
						report_error("Visestruko definisanje simbola: " + arrName, arrayDecl);
						errorDetected = true;
						return;
					}
					Tab.insert(Obj.Var, arrName, new Struct(Struct.Array, Tab.intType));
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

			// Ubacam simbolicku konstantu kao novi objektni cvor u strukturni cvor
			public void visit(EnumExpr enumExpr) {
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
				enumBegin.obj = new Obj(Obj.Con, enumBegin.getEnumName(), new Struct(Struct.Enum), -1, 0);
				Tab.currentScope.addToLocals(enumBegin.obj);
				Tab.openScope();
			}
			
			// Ulancavam simbolicke konstante 
			public void visit(EnumDecl enumDecl) {
				enumDecl.getEnumBegin().obj.getType().setMembers(Tab.currentScope().getLocals());
				Tab.closeScope();
				numOfEnumerationsDefined++;
				report_info("Definisano nabrajanje: " + enumDecl.getEnumBegin().getEnumName()  , enumDecl);
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
		public void visit(VarUse varUse) {
			MySTDump mystdump = new MySTDump();
			mystdump.visitObjNode(Tab.find(varUse.getVarName()));
			report_info("Detektovano koriscenje simbola: " + mystdump.getOutput() , varUse);
		}
			
		// Detekcija upotrebe niza
		public void visit(MyArray myArray) {
			MySTDump mystdump = new MySTDump();
			mystdump.visitObjNode(Tab.find(myArray.getArrName()));
			report_info("Detektovano koriscenje simbola: " + mystdump.getOutput(), myArray );
		}

		// Detekcija upotreba Enum-a
		public void visit(EnumUse enumUse) {
			String enumName = enumUse.getEnumName();
			String enumValue = enumUse.getEnumConst();
			MySTDump mystdump = new MySTDump();
			mystdump.visitObjNode(Tab.find(enumName));
			report_info("Detektovano koriscenje simbola: " + enumValue + " iz: " + mystdump.getOutput(), enumUse);
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


























































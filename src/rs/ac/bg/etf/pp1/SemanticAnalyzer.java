package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;

import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.structure.HashTableDataStructure;
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
	
	
		
	// Obilazan cvora imena programa
	// Otvara glavni scope
	public void visit(ProgName progName) {
		// Set program object to be available
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		// Open program scope
		Tab.openScope();

		report_info("Deklarisano ime programa: " + progName.getProgName(), progName);
	}
	
	// Kada se obidje ova cvor, zavrsio se program
	public void visit(Program program) {
		// Chain symbols and close program scope
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}
	
	// Nisu ubacili u tabelu simbola Bool tip...
	public SemanticAnalyzer() {
		super();
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", new Struct(Struct.Bool), Obj.NO_VALUE, Obj.NO_VALUE));
	}

	// Zapamtimo ime tipa i cvor kad god se nesto definise
	Struct currentType = null;
	String currentTypeName = null;
	public void visit(TypeProduction typeProduction) {
		Obj typeObj = Tab.find(typeProduction.getTypeName());
		currentTypeName = typeProduction.getTypeName();
		if(typeObj == Tab.noObj) {
			if(typeProduction.getTypeName().equals("void")) {
				currentType = Tab.noType;
			}else
				report_error("Greska ne postoji tip: " + typeProduction.getTypeName(), typeProduction);
		}else {
			currentType = typeObj.getType();
		}
	}
	
	// Detektujemo definicije promenljivih
	public void visit(SimpleVarDecl simpleVarDecl) {
		String varName = simpleVarDecl.getVarName();
		if(Tab.find(varName) == Tab.noObj) {
			Tab.insert(Obj.Var, varName, currentType);
			if(!globalVarsDefined) {
				report_info("Definisana globalna promenljiva: " + currentTypeName + " " + varName, simpleVarDecl);
			}else {
				report_info("Definisana lokalna promenljiva: " + currentTypeName + " " + varName, simpleVarDecl);
			}
		}else {
			report_error("Visestruko definisanje simbola: " + varName, simpleVarDecl);
		}
	}
	
	//	Produkcija koja oznacava kraj definisanja globalnih promenljivih
	Boolean globalVarsDefined = false;
	public void visit(EndOfGlobalDeclarationsProduction e) {
		globalVarsDefined = true;
	}
	
	//	Deklaracije metoda
	public void visit(MethDeclProduction methDeclProd) {
		report_info("Deklarisana metoda: " + methodTypeName +  " " + methDeclProd.getMethodName() , methDeclProd);
	}
	
	// Zapamtimo tip kojim smo definisali metodu za kasnije
	String methodTypeName = null;
	public void visit(NonVoidMethType nvmt) {
		methodTypeName = currentTypeName;
		methType();
	}
	public void visit(VoidMethType vmt) {
		methodTypeName = "void";
		methType();
	}
	private void methType() {
	
	}

	// Za logovanje definicija nizova
	public void visit(ArrayDeclProduction arrDeclProd) {
		if(!globalVarsDefined) {
			report_info("Definisan globalni niz: " + currentTypeName + " " + arrDeclProd.getArrayName(), arrDeclProd);
		}else {
			report_info("Definisan lokalni niz: " + currentTypeName + " " + arrDeclProd.getArrayName(), arrDeclProd);
		}
	}
	
	int enumConstantValue = 0;
	public void visit(EnumConstValue ecv){
		enumConstantValue = ecv.getEnumConstantValue();
	}

	public void visit(EnumExpr enumExpr) {
		Obj constant = new Obj(Obj.Con, enumExpr.getEnumConstantName(), Tab.intType, enumConstantValue++, 0);
		Tab.currentScope.addToLocals(constant);
	}
	
	public void visit(EnumBegin enumBegin) {
		enumBegin.obj = new Obj(Obj.Type, enumBegin.getEnumName(), Tab.intType);
		Tab.currentScope.addToLocals(enumBegin.obj);
		Tab.openScope();
	}
	
	// Detekcija enumeracija
	public void visit(EnumDecl enumDecl) {
		Tab.chainLocalSymbols(enumDecl.getEnumBegin().obj);
		Tab.closeScope();
		report_info("Definisano nabrajanje: " + enumDecl.getEnumBegin().getEnumName()  , enumDecl);
	}
	
	
	
}

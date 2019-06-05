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
	
	Struct currentType = null;
	public void visit(TypeProduction typeProduction) {
		Obj typeObj = Tab.find(typeProduction.getTypeName());
		if(typeObj == null) {
			report_error("Greska ne postoji tip: " + typeProduction.getTypeName(), typeProduction);
		}else {
			currentType = typeObj.getType();
		}
	}
	
	public void visit(SimpleVarDecl simpleVarDecl) {
		String varName = simpleVarDecl.getVarName();
		if(Tab.find(varName) == Tab.noObj) {
			Tab.insert(Obj.Var, varName, currentType);
		}else {
			report_error("Visestruko definisanje simbola: " + varName + " na liniji " + simpleVarDecl.getLine(), simpleVarDecl);
		}
	}
	
	public void visit(MultipleVariableDecl varDecl) {
		if(!globalVarsDefined)
			report_info("Visited VarDecl, defining global variables", varDecl);
		else
			report_info("Visited VarDecl, defining local variables", varDecl);
	}
	
	Boolean globalVarsDefined = false;
	public void visit(MethDeclListProduction methDeclListProd) {
		globalVarsDefined = true;
		report_info("Visited MethDeclListProduction", methDeclListProd);
	}
	
	public void visit(ProgName progName) {
		// Set program object to be available
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		// Open program scope
		Tab.openScope();

		report_info("Visited ProgName", progName);
	}
	
	public void visit(GlobalDeclListProduction globalDeclListProd) {
		report_info("Visited GlobalDeclListProduction", globalDeclListProd);
	}
	
	public void visit(Program program) {
		// Chain symbols and close program scope
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();

		report_info("Visited Program", program);
	}
	
	
}

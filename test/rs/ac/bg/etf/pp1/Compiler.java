package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Scope;

public class Compiler {
	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void testirajSintaksnuAnalizu(Logger log, Reader br) throws Exception {

			final int numOfFiles = 1;
			File sourceCode[] = new File[numOfFiles];
			sourceCode[0] = new File("test/TestiranjeSintaksneAnaliza/Test1.mj");
			
			int i = 0;
			while(i < numOfFiles) {
				log.info("Compiling source file: "+sourceCode[i].getAbsolutePath());
				
				br = new BufferedReader(new FileReader(sourceCode[i]));
				Yylex lexer = new Yylex(br);
				
				MJParser p = new MJParser(lexer);
				Symbol s = p.parse();
				
				Program prog = (Program)(s.value);
				Tab.init();
				log.info(prog.toString(""));
				log.info("====================");
				
				SemanticAnalyzer v = new SemanticAnalyzer();
				prog.traverseBottomUp(v);
				
				
				log.info("====================");
				tsdump();
				
				if(!p.errorDetected && v.passed()) {
					log.info("Parsiranje uspesno zavrseno!");
				}else {
					log.error("Parsiranje nije uspesno zavrseno!");
				}
				
				i++;
			}
	}
	
	public static void testirajSemantickuAnalizu(Logger log, Reader br) throws Exception {

		final int numOfFiles = 1;
		File sourceCode[] = new File[numOfFiles];
		sourceCode[0] = new File("test/TestiranjeSemantickeAnalize/Test1.mj");
		
		int i = 0;
		while(i < numOfFiles) {
			log.info("Compiling source file: "+sourceCode[i].getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode[i]));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
			Symbol s = p.parse();
			
			Program prog = (Program)(s.value);
			Tab.init();
			log.info(prog.toString(""));
			log.info("====================");
			
			SemanticAnalyzer v = new SemanticAnalyzer();
			prog.traverseBottomUp(v);
			
			
			log.info("====================");
			tsdump();
			
			if(!p.errorDetected && v.passed()) {
				log.info("Parsiranje uspesno zavrseno!");
			}else {
				log.error("Parsiranje nije uspesno zavrseno!");
			}
			
			i++;
		}
	}
	
	public static void testiraj(Logger log, Reader br) throws Exception {

		final int numOfFiles = 1;
		File sourceCode[] = new File[numOfFiles];
		sourceCode[0] = new File("test/program.mj");
	//	sourceCode[0] = new File("test/ziza_test.mj");
		int i = 0;
		while(i < numOfFiles) {
			log.info("Compiling source file: "+sourceCode[i].getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode[i]));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
			Symbol s = p.parse();
			
			Program prog = (Program)(s.value);
			Tab.init();
			log.info(prog.toString(""));
			log.info(" KRAJ TOKENIZACIJE \n ");
			SemanticAnalyzer v = new SemanticAnalyzer();
			prog.traverseBottomUp(v);
			
			log.info(" KRAJ PARSIRANJA \n");
			
			syntaxInfo(v);
			
			tsdump();

			if(!p.errorDetected && v.passed()) {
				File objFile = new File("test/program.obj");
				if(objFile.exists()) objFile.delete();
				
				CodeGenerator codeGenerator = new CodeGenerator();
				prog.traverseBottomUp(codeGenerator);
				Code.dataSize = v.getNumOfConstants() + v.getNumOfGlobalArrays() + v.getNumOfGlobalVariables();
				Code.mainPc = codeGenerator.getMainPc();
				Code.write(new FileOutputStream(objFile));
				log.info("Parsiranje USPESNO zavrseno!");
			}else {
				log.error("Parsiranje NEUSPESNO zavrseno!");
			}
			
			i++;
		}
	}
	
	public static void syntaxInfo(SemanticAnalyzer sa) {
		System.out.println("========= SINTAKSNA ANALIZA =========");
		System.out.println("Broj globalnih konstanti je: " + sa.getNumOfConstants());
		System.out.println("Broj globalnih enumeracija je: " + sa.getNumOfEnumerations());
		System.out.println("Broj globalnih promenljivih je: " + sa.getNumOfGlobalVariables());
		System.out.println("Broj globalnih nizova je: " + sa.getNumOfGlobalArrays());
		System.out.println("Broj lokalnih promenljivih (Main) je: " + sa.getNumOfLocalVariables());
		System.out.println("Broj lokalnih nizova (Main) je: " + sa.getNumOfLocalArrays());
		System.out.println("Broj metoda je: " + sa.getNumOfMethods());
		System.out.println("");
	}
	
	public static void main(String[] args) throws Exception {
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		
		try {
	
			//testirajSintaksnuAnalizu(log, br);
			//testirajSemantickuAnalizu(log, br);
			testiraj(log, br);

		}finally {
			if(br != null) try {br.close(); } catch (IOException e1) {log.error(e1.getMessage(), e1); }
		}
	}
	
	public static void tsdump() {
		System.out.println("=====================SYMBOL TABLE DUMP=========================");
		MySTDump stv = new MySTDump();
		for (Scope s = Tab.currentScope; s != null; s = s.getOuter()) {
			s.accept(stv);
		}
		System.out.println(stv.getOutput());
	}
}

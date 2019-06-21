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
	
	public static void testirajSintaksnuAnalizu(Logger log) {
		BufferedReader br = null;
		try {

			final int numOfFiles = 4;
			
			String path = "test/TestiranjeSintaksneAnalize/";
			String fajlovi[] = new String[numOfFiles];
			fajlovi[0] = "test_radi_1";
			fajlovi[1] = "test_radi_2";
			fajlovi[2] = "test_ne_radi_1";
			fajlovi[3] = "test_ne_radi_2";

			File sourceCode[] = new File[numOfFiles];
			int i = 0;
			for(String fajl : fajlovi) {
				sourceCode[i] = new File(path + fajlovi[i] + ".mj");
				i++;
			}

			i = 0;
			while(i < numOfFiles) {
				log.info("Compiling source file: "+sourceCode[i].getAbsolutePath());
				
				br = new BufferedReader(new FileReader(sourceCode[i]));
				Yylex lexer = new Yylex(br);
				
				MJParser p = new MJParser(lexer);
				Symbol s = p.parse();

				log.info("\n ========== KRAJ TOKENIZACIJE ========== \n ");
				
				Program prog = (Program)(s.value);

				

				if(!p.errorDetected) {

					log.info("\n ========= SINTAKSNO STABLO ========= \n");

					log.info(prog.toString(""));

					log.info("\n ========== SEMANTICKA ANALIZA ========== \n");
					Tab.init();
					SemanticAnalyzer v = new SemanticAnalyzer();
					prog.traverseBottomUp(v);
					
					log.info(" ========== KRAJ PARSIRANJA =========== \n");
					
					syntaxInfo(v);
					
					tsdump(); if(!p.errorDetected && v.passed()) {
						File objFile = new File("test/sintaksna_" + fajlovi[i] + ".obj");
						if(objFile.exists()) objFile.delete();
						
						CodeGenerator codeGenerator = new CodeGenerator();
						prog.traverseBottomUp(codeGenerator);
						Code.dataSize = v.getNumOfConstants() + v.getNumOfGlobalArrays() + v.getNumOfGlobalVariables();
						Code.mainPc = codeGenerator.getMainPc();
						Code.write(new FileOutputStream(objFile));
						log.info("Parsiranje USPESNO zavrseno!");
						resetCodeClass();
					}else {
						log.error("Parsiranje NEUSPESNO zavrseno!");
					}
					

				}
				i++;
			}	
		
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			if(br != null) try {br.close(); } catch (IOException e1) {log.error(e1.getMessage(), e1); }
		}
	}
	
	public static void testirajSemantickuAnalizu(Logger log) throws Exception {

		BufferedReader br = null;
		try {
			final int numOfFiles = 8;
			
			String path = "test/TestiranjeSemantickeAnalize/";
			String fajlovi[] = new String[numOfFiles];
			fajlovi[0] = "test_radi_1";
			fajlovi[1] = "test_radi_2";
			fajlovi[2] = "test_radi_3";
			fajlovi[3] = "test_radi_4";
			fajlovi[4] = "test_ne_radi_1";
			fajlovi[5] = "test_ne_radi_2";
			fajlovi[6] = "test_ne_radi_3";
			fajlovi[7] = "test_ne_radi_4";

			File sourceCode[] = new File[numOfFiles];
			int i = 0;
			for(String fajl : fajlovi) {
				sourceCode[i] = new File(path + fajlovi[i] + ".mj");
				i++;
			}

			i = 0;
			while(i < numOfFiles) {
				log.info("Compiling source file: "+sourceCode[i].getAbsolutePath());
				
				br = new BufferedReader(new FileReader(sourceCode[i]));
				Yylex lexer = new Yylex(br);
				
				MJParser p = new MJParser(lexer);
				Symbol s = p.parse();

				log.info("\n ========== KRAJ TOKENIZACIJE ========== \n ");
				
				Program prog = (Program)(s.value);

				

				if(!p.errorDetected) {

					log.info("\n ========= SINTAKSNO STABLO ========= \n");

					log.info(prog.toString(""));

					log.info("\n ========== SEMANTICKA ANALIZA ========== \n");
					Tab.init();
					SemanticAnalyzer v = new SemanticAnalyzer();
					prog.traverseBottomUp(v);
					
					log.info(" ========== KRAJ PARSIRANJA =========== \n");
					
					syntaxInfo(v);
					
					tsdump(); if(!p.errorDetected && v.passed()) {
						File objFile = new File("test/semanticka_" + fajlovi[i] + ".obj");
						if(objFile.exists()) objFile.delete();
						
						CodeGenerator codeGenerator = new CodeGenerator();
						prog.traverseBottomUp(codeGenerator);
						Code.dataSize = v.getNumOfConstants() + v.getNumOfGlobalArrays() + v.getNumOfGlobalVariables();
						Code.mainPc = codeGenerator.getMainPc();
						Code.write(new FileOutputStream(objFile));
						log.info("Parsiranje USPESNO zavrseno!");
						resetCodeClass();
					}else {
						log.error("Parsiranje NEUSPESNO zavrseno!");
					}
					

				}
				i++;
			}	
		
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(br != null) try {br.close(); } catch (IOException e1) {log.error(e1.getMessage(), e1); }
		}
	}
	
	public static void testiraj(Logger log) {

		BufferedReader br = null;
		try {
			final int numOfFiles = 1;
			
			String path = "test/";
			String fajlovi[] = new String[numOfFiles];
			fajlovi[0] = "ziza_test";

			File sourceCode[] = new File[numOfFiles];
			int i = 0;
			for(String fajl : fajlovi) {
				sourceCode[i] = new File(path + fajlovi[i] + ".mj");
				i++;
			}

			i = 0;
			while(i < numOfFiles) {
				log.info("Compiling source file: "+sourceCode[i].getAbsolutePath());
				
				br = new BufferedReader(new FileReader(sourceCode[i]));
				Yylex lexer = new Yylex(br);
				
				MJParser p = new MJParser(lexer);
				Symbol s = p.parse();

				log.info("\n ========== KRAJ TOKENIZACIJE ========== \n ");
				
				Program prog = (Program)(s.value);

				

				if(!p.errorDetected) {

					log.info("\n ========= SINTAKSNO STABLO ========= \n");

					log.info(prog.toString(""));

					log.info("\n ========== SEMANTICKA ANALIZA ========== \n");
					Tab.init();
					SemanticAnalyzer v = new SemanticAnalyzer();
					prog.traverseBottomUp(v);
					
					log.info(" ========== KRAJ PARSIRANJA =========== \n");
					
					syntaxInfo(v);
					
					tsdump(); 
					
					if(!p.errorDetected && v.passed()) {
						File objFile = new File("test/" + fajlovi[i] + ".obj");
						if(objFile.exists()) objFile.delete();
						
						CodeGenerator codeGenerator = new CodeGenerator();
						prog.traverseBottomUp(codeGenerator);
						Code.dataSize = v.getNumOfConstants() + v.getNumOfGlobalArrays() + v.getNumOfGlobalVariables();
						Code.mainPc = codeGenerator.getMainPc();
						Code.write(new FileOutputStream(objFile));
						log.info("Parsiranje USPESNO zavrseno!");
						resetCodeClass();
					}else {
						log.error("Parsiranje NEUSPESNO zavrseno!");
					}
					

				}
				i++;
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(br != null) try {br.close(); } catch (IOException e1) {log.error(e1.getMessage(), e1); }
		}
	}
	
	public static void resetCodeClass() {
		Code.buf = new byte[8192];
		Code.pc = 0;
		Code.mainPc = -1;
		Code.dataSize = 0;
		Code.greska = false;
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
		
		testirajSintaksnuAnalizu(log);
		testirajSemantickuAnalizu(log);
		testiraj(log);

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

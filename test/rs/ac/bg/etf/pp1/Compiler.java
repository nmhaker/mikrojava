package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.symboltable.Tab;

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
				Tab.dump();
				
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
			Tab.dump();
			
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
			Tab.dump();
			
			if(!p.errorDetected && v.passed()) {
				log.info("Parsiranje uspesno zavrseno!");
			}else {
				log.error("Parsiranje nije uspesno zavrseno!");
			}
			
			i++;
		}
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
}

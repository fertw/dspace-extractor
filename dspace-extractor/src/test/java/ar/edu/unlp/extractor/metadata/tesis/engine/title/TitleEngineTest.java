package ar.edu.unlp.extractor.metadata.tesis.engine.title;

import java.io.File;
import java.util.Properties;

import junit.framework.TestCase;

import org.docear.pdf.PdfDataExtractor;
import org.junit.Test;

import ar.edu.unlp.extractor.metadata.tesis.utils.PdfReaderEngine;

public class TitleEngineTest extends TestCase {

	private Properties prop;

	protected void setUp() throws Exception {
		super.setUp();
		prop = new Properties();
		prop.load(TitleEngineTest.class.getClassLoader().getResourceAsStream("config.properties"));
	}

	@Test
	public void testExtractKeywords() throws Exception {
		
		
		
		String path = prop.getProperty("pdf.abstract");
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().endsWith(".pdf")) {
				
				
				
				
				String page = PdfReaderEngine.extractPdfFirstPageText(file);
				String pageArea = PdfReaderEngine.extractTextByArea(file);
				
				System.out.println("---------------------------PDF: "+file.getName()+"---------------------------------------------:");
				System.out.println("El texto completo es:\n" + page);
				System.out.println("-------------------------------------------------------------------------------------\n");
				System.out.println("-------------------------------------------------------------------------------------\n");
				System.out.println("-------------------------------------------------------------------------------------\n");
				
				System.out.println("Area:\n" + pageArea);
				System.out.println("-------------------------------------------------------------------------------------\n");
				System.out.println("-------------------------------------------------------------------------------------\n");
				System.out.println("-------------------------------------------------------------------------------------\n");
				
	
				System.out.println("--------------------------------------TITLE-----------------------------------------------: ");
				
				
				PdfDataExtractor extractor = new PdfDataExtractor(file);
				String title = extractor.extractTitle();
				
				System.out.println("El TITLE es:\n" + title);
				System.out.println("-------------------------------------------------------------------------------------\n");
				System.out.flush();
			}

		}
		
		
		
		

	}

}

package ar.edu.unlp.extractor.metadata.tesis.engine.resume;

import java.io.File;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;

import ar.edu.unlp.extractor.metadata.tesis.utils.PdfReaderEngine;

public class AbstractEngineTest extends TestCase {

	private Properties prop;

	protected void setUp() throws Exception {
		super.setUp();
		prop = new Properties();
		prop.load(AbstractEngineTest.class.getClassLoader().getResourceAsStream("config.properties"));
	}

	@Test
	public void testExtractKeywords() throws Exception {

		String path = prop.getProperty("pdf.abstract");
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().endsWith(".pdf")) {
				//String page = PdfReaderEngine.getPdfFirstPageText(file.getAbsolutePath());
				List<String> page = PdfReaderEngine.extractPdfFirstPageTextByLines(file);
				for (String string : page) {
					System.out.println(string+"\n");
				}
				
				System.out.println("---------------------------PDF: "+file.getName()+"---------------------------------------------:");
				System.out.println("El texto completo es:\n" + page);
				String resumeText = AbstractEngine.extractAbstract(file);
				System.out.println("--------------------------------------ABSTRACT-----------------------------------------------: ");
				System.out.println("El Abstract es:\n" + resumeText);
				System.out.println("-------------------------------------------------------------------------------------\n");
			}

		}
	}

}

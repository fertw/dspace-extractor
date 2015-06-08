package ar.edu.unlp.extractor.metadata.tesis.engine.keyword;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;

import ar.edu.unlp.extractor.metadata.tesis.utils.PdfReaderEngine;

public class KeywordSentenceEngineTest extends TestCase {

	private Properties prop;

	protected void setUp() throws Exception {
		super.setUp();
		prop = new Properties();
		prop.load(KeywordSentenceEngineTest.class.getClassLoader().getResourceAsStream("config.properties"));
	}

	@Test
	public void testExtractKeywords() throws Exception {

		String path = prop.getProperty("pdf.resource.keyword");
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().endsWith(".pdf")) {
				System.out.println("NOMBRE DEL PDF "+file.getName()+"\n");					
				String page = PdfReaderEngine.extractPdfFirstPageText(file);				
				System.out.println(page);
				List<String> keywords = new ArrayList<String>();
				keywords = KeywordEngine.extractKeywordsFromFile(file);
				if (keywords.size() == 0) {
					System.out.println("-----------------------------------------------NO SE ENCONTRO--------------------------------: ");
					System.out.println("NOMBRE DEL PDF "+file.getName());
				} else {
					System.out.println("\n");
					System.out.println("--------------------------------------Keywords-----------------------------------------------: ");
					if (keywords != null) {
						for (String key : keywords) {
							System.out.println("Key: " + key);
						}
					}
					System.out.println("\n");
				}

			}

		}
	}

}

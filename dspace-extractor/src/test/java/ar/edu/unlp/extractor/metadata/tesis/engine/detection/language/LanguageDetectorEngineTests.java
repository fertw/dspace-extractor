package ar.edu.unlp.extractor.metadata.tesis.engine.detection.language;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import ar.edu.unlp.extractor.metadata.tesis.utils.PdfReaderEngine;
import com.cybozu.labs.langdetect.LangDetectException;

public class LanguageDetectorEngineTests {

	private Properties prop;

	@Before
	public void setUp() throws Exception {
		prop = new Properties();
		prop.load(LanguageDetectorEngineTests.class.getClassLoader().getResourceAsStream("config.properties"));
	}

	@Test
	public void test() throws LangDetectException, IOException {

		String pathPdf = prop.getProperty("pdf.resource.keyword");

		File folder = new File(pathPdf);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().endsWith(".pdf")) {
				String page = PdfReaderEngine.extractPdfFirstPageText(file);
				System.out.println("Contenido de la p�gina:\n\n" + page + "\n\n");
				String lang = LanguageEngine.detectLang(file);
				System.out.println("--------------------------------------Language-----------------------------------------------: ");
				System.out.println("El idioma del PDF es: " + lang + "\n\n");

			}

		}
	}

}

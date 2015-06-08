package ar.edu.unlp.extractor.metadata.tesis.engine.title;

import java.io.File;
import java.io.IOException;

import org.docear.pdf.PdfDataExtractor;

import com.cybozu.labs.langdetect.LangDetectException;

/**
 * Estrategia para identificar el titulo de un pdf
 * @author Fernando
 *
 */
public class TitleEngine  {	
	/**
	 * Metodo que extrae el titulo de un PDF
	 * @param file archivo PDF
	 * @return titulo del PDF
	 * @throws LangDetectException
	 */
    public static String extractTitle(File file) throws LangDetectException {
    	
    	PdfDataExtractor extractor = new PdfDataExtractor(file);
		try {
			String title = extractor.extractTitle();
			return title;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
   
}
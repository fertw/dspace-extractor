package ar.edu.unlp.extractor.metadata.tesis.engine.detection.language;

import java.io.File;
import java.io.IOException;

import ar.edu.unlp.extractor.metadata.tesis.engine.ExtractorEngine;
import ar.edu.unlp.extractor.metadata.tesis.utils.PdfReaderEngine;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

/**
 * Estrategia para identificar el idioma del document
 * @author Fernando
 *
 */
public class LanguageEngine extends ExtractorEngine {
	/**
	 * Metodo que determina el idioma del texto
	 * @param text
	 * @return
	 * @throws LangDetectException
	 * @throws IOException 
	 */
    public static String detectLang(File file) throws LangDetectException, IOException {
    	
    	String text = PdfReaderEngine.extractPdfFirstPageText(file);
    	if (DetectorFactory.getLangList().size()<=0)
    		DetectorFactory.loadProfile(getProfileDirectory());
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.detect();
    }
}
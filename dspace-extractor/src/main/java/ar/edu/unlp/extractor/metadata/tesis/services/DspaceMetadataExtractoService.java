package ar.edu.unlp.extractor.metadata.tesis.services;

import java.io.File;

import ar.edu.unlp.extractor.metadata.tesis.dtos.CompositeValueResultDto;
import ar.edu.unlp.extractor.metadata.tesis.dtos.SimpleValueResultDto;

import com.cybozu.labs.langdetect.LangDetectException;

/**
 * Interfaz para las estrategias de extraccion
 * @author Fernando
 *
 */
public interface DspaceMetadataExtractoService {

	/**
	 * Metodo que extrae el titulo de un PDF presente en el texto.
	 * @param file archivo PDF
	 * @return
	 * @throws LangDetectException
	 */
	public SimpleValueResultDto extractTitle (File file);
	
	/**
	 * Metodoq que extrae las keyword de un PDF prensentes en el texto.
	 * @param file archivo PDF
	 * @return Lista de keywords
	 */
	public CompositeValueResultDto  extractKeywords (File file);
	
	/**
	 * Metodo que extrae el Abstract de un PDF 
	 * @param file archivo PDF
	 * @return el abstract de un documento
	 */
	public SimpleValueResultDto  extractAbstract (File file);
	
	
	/**
	 * Metodo que determina el idioma de un PDF
	 * @param file archivo PDF
	 * @return idioma del PDF
	 * @throws LangDetectException 
	 */
	public SimpleValueResultDto  extractLanguage(File file) throws LangDetectException;
	
	/**
	 * Metodo que extrae los autores de un PDF contenido en el texto
	 * @param file
	 * @return lista de autores
	 */
	public CompositeValueResultDto  extractAuthor(File file);
    
}

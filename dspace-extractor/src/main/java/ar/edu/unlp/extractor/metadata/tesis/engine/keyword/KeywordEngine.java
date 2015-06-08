package ar.edu.unlp.extractor.metadata.tesis.engine.keyword;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;
import ar.edu.unlp.extractor.metadata.tesis.engine.ExtractorEngine;
import ar.edu.unlp.extractor.metadata.tesis.engine.detection.language.LanguageEngine;
import ar.edu.unlp.extractor.metadata.tesis.utils.PdfReaderEngine;

public class KeywordEngine extends ExtractorEngine {

	/**
	 * en-sent.bin
	 */
	private String modelSentence;

	private static String[] initStringsPatterns = { "palabras claves:", "palabras clave:", "palabras claves", "palabras clave", "keywords:",
			"keyword:", "keywords", "keyword", "key words:" ,"key words"};

	private static String[] othersPatterns = { "1 introduction", "1 introducción" ,".", "\n" };


	/**
	 * Metodo que extrae las palabras clave de un archivo PDF Se extraen las palabras clave suponiendo que estan en la primer pagina.
	 * 
	 * @param file archivo PDF
	 * @return lista de palabras clave
	 * @throws Exception
	 */
	public static List<String> extractKeywordsFromFile(File file) throws Exception {

		String text = PdfReaderEngine.extractPdfFirstPageText(file);

		if (LanguageEngine.detectLang(file).equals("es")) {
			return extractKeywordsForLanguague(text, "es-sent.bin");
		}
		if (LanguageEngine.detectLang(file).equals("en")) {
			return extractKeywordsForLanguague(text, "en-sent.bin");
		}
		return null;
	}

	/**
	 * Metodo que dependiendo del idioma procesa el texto en busqueda de palabras clave
	 * @param sentence
	 * @param sentBin
	 * @return lista de palabras clave
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private static List<String> extractKeywordsForLanguague(String sentence, String sentBin) throws FileNotFoundException, IOException,
			InvalidFormatException {

		InputStream modelSentence = new FileInputStream(new File(getModelsDirectory(), sentBin));
		SentenceModel model = new SentenceModel(modelSentence);
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		String sentences[] = sentenceDetector.sentDetect(sentence);
		modelSentence.close();

		String initWord;
		boolean keyLoc = false,endKeys = false;
		int si = 0;
		List<String> keywords = new ArrayList<>();
		while ((!keyLoc) && (si < sentences.length)) {
			initWord = detectKeywordStart(sentences[si]);
			if (!initWord.isEmpty()) {
				
				while ((!endKeys) && (si < sentences.length)) {
					keywords = obtainKeywords(sentences[si].toLowerCase(), initWord);
					if (keywords.size()>0){
						endKeys=true;	
						
					}
					else{
						si++;
					}
				}
				keyLoc=true;
				}
			si++;
		}
		return keywords;
	}

	/**
	 * Metodo que determina si una setencia empiza con alguno de los identificadores para las keywords
	 * 
	 * @param sentence
	 * @return
	 */
	private static String detectKeywordStart(String sentence) {

		for (String keyStart : initStringsPatterns) {
			if ((sentence.toLowerCase().contains(keyStart)) && (sentence.toLowerCase().startsWith(keyStart))) {
				return keyStart;
			}
		}
		return "";
	}

	private static List<String> obtainKeywords(String sentenceKeyword, String startWord) {
		String keywordList = "";
		String[] keys = {};
		List<String> resultKeys = new ArrayList<>();
		if (sentenceKeyword.contains(startWord)){
			//si la palabra clave "keyword" esta en la misma linea
			keywordList = substringBetween(sentenceKeyword, startWord, othersPatterns);
		}
		else{
			keywordList=substringEndsWith(sentenceKeyword,othersPatterns);
		}
		
		if (keywordList != null) {
			return processKeywords(keywordList, keys, resultKeys);
		}
		return null;
	}

	private static String substringEndsWith(String sentenceKeyword, String[] patterns) {
		for (String endTag : patterns) {
			if (sentenceKeyword.endsWith(endTag)){
				return sentenceKeyword.replace(endTag, "");
			}
		}
		return sentenceKeyword;
	}

	private static List<String> processKeywords(String keywordList, String[] keys, List<String> resultKeys) {
		if (keywordList.contains(",")) {
			keys = keywordList.split(",");
		}
		else{
			if (keywordList.contains(" - ")) {
				keys = keywordList.split("-");
			}
			else{
				if (keywordList.contains(";")) { 
					keys = keywordList.split(";");
				}
			}
		}

		for (String string : keys) {
			//string = string.replaceAll("[\\-\\+\\.\\^:,–]", ""); 
			string = string.replace("\n", "");
			string = string.trim();
			resultKeys.add(string); 
		}
		return resultKeys; 
	}

	public static String substringBetween(String str, String open, String[] pattern) {
		if (str == null || open == null || pattern == null) {
			return null;
		}
		int start = str.indexOf(open);
		if (start != -1) {
			for (String endTag : pattern) {
				int end = str.indexOf(endTag, start + open.length());
				if (end != -1) {
					return str.substring(start + open.length(), end);
				}
			}

		}
		return null;
	}

	public String getModelSentence() {
		return modelSentence;
	}

	public void setModelSentence(String modelSentence) {
		this.modelSentence = modelSentence;
	}

	public static String[] getInitStringsPatterns() {
		return initStringsPatterns;
	}

	public static void setInitStringsPatterns(String[] initStringsPatterns) {
		KeywordEngine.initStringsPatterns = initStringsPatterns;
	}

}

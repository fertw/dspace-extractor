package ar.edu.unlp.extractor.metadata.tesis.engine.resume;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.util.InvalidFormatException;
import ar.edu.unlp.extractor.metadata.tesis.engine.ExtractorEngine;
import ar.edu.unlp.extractor.metadata.tesis.utils.PdfReaderEngine;

public class AbstractEngine extends ExtractorEngine {

	/**
	 * en-sent.bin
	 */
	private String modelSentence;

	private static final String[] initStringsPatterns = { "resumen", "abstract", "resume", "abstract." };
	private static String[] initKeywordsPatterns = { "palabras claves:", "palabras clave:", "palabras claves", "palabras clave", "keywords:",
			"keyword:", "keywords", "keyword", "key words" };
	private static String[] othersPatterns = { "1 introduction", "1 introducción" };

	public AbstractEngine() {
		super();
	}

	/**
	 * Metodo que extrae el Abstract de un archivo
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String extractAbstract(File file) throws Exception {

		List<String> text = PdfReaderEngine.extractPdfFirstPageTextByLines(file);
		return extractAbstractMetadata(text);

	}

	
	/**
	 * Metodo que detecta y extrae el abstract de una lista de lineas de texto
	 * @param text
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private static String extractAbstractMetadata(List<String> text) throws InvalidFormatException, IOException {

		String initWord;
		boolean keyLoc = false;
		boolean endResume = false;
		int numberLine = 0;
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		String newLine;
		while ((!keyLoc) && (numberLine < text.size())) {
			initWord = detectResumeStart(text.get(numberLine)); // busco si en la linea esta la palabra clave
			if (!initWord.isEmpty()) {
				keyLoc = true;
				line = removeSpecialWord(text.get(numberLine)); // remuevo las palabras resume/abstract
				numberLine++;
				while ((!endResume) && (numberLine < text.size())) {
					if (!line.equals("")) {
						stringBuilder.append(line);
					}
					line = text.get(numberLine);
					if (line.equals("") || startsWithText(line, othersPatterns) || startsWithText(line, initKeywordsPatterns)) {
						newLine = detectWord(line, initKeywordsPatterns);
						if ((newLine.equals("")) && (!startsWithText(line, initKeywordsPatterns))) {
							newLine = detectWord(line, othersPatterns);
						}
						stringBuilder.append(newLine);
						endResume = true;
					} else {
						numberLine++;
					}
				}
			} else {
				numberLine++;
			}

		}
		return stringBuilder.toString();
	}

	// private static String extractAbstractForLanguague(String text, String modelName) throws InvalidFormatException, IOException {
	//
	// InputStream modelSentence = new FileInputStream(new File(getModelsDirectory(), modelName));
	// SentenceModel model = new SentenceModel(modelSentence);
	// SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
	// String sentences[] = sentenceDetector.sentDetect(text);
	// modelSentence.close();
	//
	// String initWord;
	// boolean keyLoc = false;
	// boolean endResume = false;
	// int si = 0;
	// StringBuilder stringBuilder = new StringBuilder();
	// String line;
	// String newLine;
	// while ((!keyLoc) && (si < sentences.length)) {
	// initWord = detectResumeStart(sentences[si]);
	// if (!initWord.isEmpty()) {
	// keyLoc = true;
	// line = removeSpecialWord(sentences[si]); // remuevo las palabras resume/abstract
	// si++;
	// while ((!endResume) && (si < sentences.length)) {
	// if (!line.equals("")) {
	// stringBuilder.append(line);
	// }
	// line = sentences[si];
	// if (line.equals("") || startsWithText(line, othersPatterns) || startsWithText(line, initKeywordsPatterns)) {
	// newLine=detectWord(line,initKeywordsPatterns);
	// if ((newLine.equals("")) && (!startsWithText(line, initKeywordsPatterns))){
	// newLine=detectWord(line,othersPatterns);
	// }
	// stringBuilder.append(newLine);
	// endResume = true;
	// } else {
	// si++;
	// }
	// }
	// } else {
	// si++;
	// }
	//
	// }
	// return stringBuilder.toString();
	// }

	/**
	 * Metodo que determina si una setencia empiza con alguno de los identificadores para el abstract
	 * 
	 * @param sentence
	 * @return
	 */
	private static String detectResumeStart(String sentence) {

		for (String keyStart : initStringsPatterns) {
			if ((sentence.toLowerCase().contains(keyStart)) && (sentence.toLowerCase().startsWith(keyStart))) {
				return keyStart;
			}
		}
		return "";
	}

	private static String detectWord(String sentence, String[] patterns) {

		for (String keyStart : patterns) {
			if (sentence.toLowerCase().contains(keyStart)) {
				int introduction = sentence.toLowerCase().indexOf(keyStart);
				String result = "";
				if (introduction != -1) {
					result = sentence.substring(0, introduction);
					return result;
				}
			}
		}
		return "";
	}

	private static String removeSpecialWord(String line) {
		List<String> words = Arrays.asList(initStringsPatterns);
		for (String word : words) {
			if (line.toLowerCase().contains(word)) {
				line = line.replaceAll("(?i)" + word, "");
				// line = line.toLowerCase().replace(word, "");
				line = line.replaceAll("[\\-\\+\\.\\^:,–]", "");
				line = line.replace("\n", "");
				return line;
			}
		}
		return "";
	}

	private static boolean startsWithText(String line, String[] pattern) {
		List<String> words = Arrays.asList(pattern);
		for (String word : words) {
			if (line.toLowerCase().trim().replaceAll(" +", " ").contains(word)) {
				return true;
			}
		}
		return false;
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

}

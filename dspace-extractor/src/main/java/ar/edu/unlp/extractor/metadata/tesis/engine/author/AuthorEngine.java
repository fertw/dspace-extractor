package ar.edu.unlp.extractor.metadata.tesis.engine.author;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;
import ar.edu.unlp.extractor.metadata.tesis.engine.ExtractorEngine;
import ar.edu.unlp.extractor.metadata.tesis.engine.detection.language.LanguageEngine;
import ar.edu.unlp.extractor.metadata.tesis.model.TextAnnotation;
import ar.edu.unlp.extractor.metadata.tesis.utils.PdfReaderEngine;

/**
 * Estrategia para extraer nombre y apellido de autores de un texto
 * 
 * @author fernando
 * 
 */
public class AuthorEngine extends ExtractorEngine {

	/**
	 * en-sent.bin
	 */
	private String modelSentence;
	/**
	 * es-ner-person.bin
	 */
	private String modelPerson;
	private Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

	public AuthorEngine() {
		super();
	}

	/**
	 * Metodo encargado de extraer nombres y apellidos de autores
	 * 
	 * @param sentence
	 *            texto a extraer los autores
	 * @return lista de autores
	 * @throws Exception
	 */
	public List<String> extractAuthors(File file) throws Exception {

		String text = PdfReaderEngine.extractPdfFirstPageText(file);
		List<String> authors = new ArrayList<>();

		this.setModelPerson("es-ner-person.bin");
		this.setModelSentence("es-sent.bin");
		authors.addAll(this.extract(text));

		this.setModelPerson("en-ner-person.bin");
		this.setModelSentence("en-sent.bin");
		authors.addAll(this.extract(text));
		return authors;
	}

	private List<String> extract(String sentence) throws FileNotFoundException, IOException, InvalidFormatException {
		InputStream modelSentence = new FileInputStream(new File(getModelsDirectory(), this.getModelSentence()));
		SentenceModel model = new SentenceModel(modelSentence);
		SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
		String sentences[] = sentenceDetector.sentDetect(sentence);

		TokenNameFinderModel modelPerson = new TokenNameFinderModel(new File(getModelsDirectory(), this.getModelPerson()));
		NameFinderME nameFinder = new NameFinderME(modelPerson);

		List<String> namedEntities = new ArrayList<String>();

		for (int si = 0; si < sentences.length; si++) {

			List<TextAnnotation> allTextAnnotations = new ArrayList<TextAnnotation>();
			String[] tokens = tokenizer.tokenize(sentences[si]);

			Span[] spans = nameFinder.find(tokens);
			double[] probs = nameFinder.probs(spans);
			for (int ni = 0; ni < spans.length; ni++) {
				allTextAnnotations.add(new TextAnnotation("person", spans[ni], probs[ni]));
			}

			if (!allTextAnnotations.isEmpty()) {
				removeConflicts(allTextAnnotations);
				convertTextAnnotationsToNamedEntities(tokens, allTextAnnotations, namedEntities);
			}
			nameFinder.clearAdaptiveData();
		}
		return namedEntities;
	}

	private void convertTextAnnotationsToNamedEntities(String[] tokens, List<TextAnnotation> TextAnnotations, List<String> namedEntities) {
		for (TextAnnotation TextAnnotation : TextAnnotations) {
			int start = TextAnnotation.getSpan().getStart();
			int end = TextAnnotation.getSpan().getEnd();
			String[] textAnnotationData = Arrays.copyOfRange(tokens, start, end);
			String fullName = "";
			for (String name : textAnnotationData) {
				fullName = fullName + " " + name;
			}
			namedEntities.add(fullName);
		}
	}

	private void removeConflicts(List<TextAnnotation> allTextAnnotations) {
		java.util.Collections.sort(allTextAnnotations);
		List<TextAnnotation> stack = new ArrayList<TextAnnotation>();
		stack.add(allTextAnnotations.get(0));
		for (int ai = 1; ai < allTextAnnotations.size(); ai++) {
			TextAnnotation curr = (TextAnnotation) allTextAnnotations.get(ai);
			boolean deleteCurr = false;
			for (int ki = stack.size() - 1; ki >= 0; ki--) {
				TextAnnotation prev = (TextAnnotation) stack.get(ki);
				if (prev.getSpan().equals(curr.getSpan())) {
					if (prev.getProb() > curr.getProb()) {
						deleteCurr = true;
						break;
					} else {
						allTextAnnotations.remove(stack.remove(ki));
						ai--;
					}
				} else if (prev.getSpan().intersects(curr.getSpan())) {
					if (prev.getProb() > curr.getProb()) {
						deleteCurr = true;
						break;
					} else {
						allTextAnnotations.remove(stack.remove(ki));
						ai--;
					}
				} else if (prev.getSpan().contains(curr.getSpan())) {
					break;
				} else {
					stack.remove(ki);
				}
			}
			if (deleteCurr) {
				allTextAnnotations.remove(ai);
				ai--;
				deleteCurr = false;
			} else {
				stack.add(curr);
			}
		}
	}

	public String getModelSentence() {
		return modelSentence;
	}

	public void setModelSentence(String modelSentence) {
		this.modelSentence = modelSentence;
	}

	public String getModelPerson() {
		return modelPerson;
	}

	public void setModelPerson(String modelPerson) {
		this.modelPerson = modelPerson;
	}

}

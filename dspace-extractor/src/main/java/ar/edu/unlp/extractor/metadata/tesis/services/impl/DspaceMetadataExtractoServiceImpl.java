package ar.edu.unlp.extractor.metadata.tesis.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ar.edu.unlp.extractor.metadata.tesis.dtos.CompositeValueResultDto;
import ar.edu.unlp.extractor.metadata.tesis.dtos.SimpleValueResultDto;
import ar.edu.unlp.extractor.metadata.tesis.engine.author.AuthorEngine;
import ar.edu.unlp.extractor.metadata.tesis.engine.detection.language.LanguageEngine;
import ar.edu.unlp.extractor.metadata.tesis.engine.keyword.KeywordEngine;
import ar.edu.unlp.extractor.metadata.tesis.engine.resume.AbstractEngine;
import ar.edu.unlp.extractor.metadata.tesis.engine.title.TitleEngine;
import ar.edu.unlp.extractor.metadata.tesis.services.DspaceMetadataExtractoService;
import ar.edu.unlp.extractor.metadata.tesis.utils.Constants;

import com.cybozu.labs.langdetect.LangDetectException;

@Service
public class DspaceMetadataExtractoServiceImpl implements DspaceMetadataExtractoService {


	@Override
	public SimpleValueResultDto extractTitle (File file) {
		String title = "";
		SimpleValueResultDto simpleValueResultDto = new SimpleValueResultDto("", "", "");
		simpleValueResultDto.setMetadataName(Constants.DC_TITLE);
		try {
			title = TitleEngine.extractTitle(file);			
			simpleValueResultDto.setValue(title);
		} catch (LangDetectException e) {
			simpleValueResultDto.setError(e.getMessage());
		}
		return simpleValueResultDto;		
	}
	
	@Override
	public CompositeValueResultDto  extractKeywords (File file){
		List<String> keywords= new ArrayList<String>();
		CompositeValueResultDto compositeValueResultDto = new CompositeValueResultDto(new ArrayList<String>(), "","");
		compositeValueResultDto.setMetadataName(Constants.DC_SUBJECT);
		
		try {
			keywords = KeywordEngine.extractKeywordsFromFile(file);
			compositeValueResultDto.setValue(keywords);
		} catch (Exception e) {
			compositeValueResultDto.setError(e.getMessage());
		}
		return compositeValueResultDto;
	}

	@Override
	public SimpleValueResultDto extractAbstract(File file) {
		String resume = "";
		SimpleValueResultDto simpleValueResultDto = new SimpleValueResultDto("", "", "");
		simpleValueResultDto.setMetadataName(Constants.DC_DESCRIPTION);
		try {
			resume = AbstractEngine.extractAbstract(file);
			simpleValueResultDto.setValue(resume);
		} catch (Exception e) {
			simpleValueResultDto.setError(e.getMessage());
		}
		return simpleValueResultDto;
	}

	@Override
	public SimpleValueResultDto extractLanguage(File file) throws LangDetectException {
		String language = "";
		SimpleValueResultDto simpleValueResultDto = new SimpleValueResultDto("", "", "");
		simpleValueResultDto.setMetadataName(Constants.DC_LANGUAGE);
		try {
			language = LanguageEngine.detectLang(file);
			simpleValueResultDto.setValue(language);
		} catch (IOException e) {
			simpleValueResultDto.setError(e.getMessage());
		}
		return simpleValueResultDto;
		
	}

	@Override
	public CompositeValueResultDto extractAuthor(File file) {
		AuthorEngine authorEngine = new AuthorEngine();
		CompositeValueResultDto compositeValueResultDto = new CompositeValueResultDto(new ArrayList<String>(), "","");
		compositeValueResultDto.setMetadataName(Constants.DC_CONTRIBUTOR);
		List<String> authors = new ArrayList<String>();
		try {
			authors = authorEngine.extractAuthors(file);
			compositeValueResultDto.setValue(authors);
		} catch (Exception e) {
			compositeValueResultDto.setError(e.getMessage());
		}
		return compositeValueResultDto;
		
	}
}

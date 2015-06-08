package ar.edu.unlp.extractor.metadata.tesis.dtos;

import java.util.List;

/**
 * DTO para los resultados con valores multiples
 * @author Fernando
 *
 */
public class CompositeValueResultDto {
	
	private  List<String> value;
	private String error;
	private String metadataName;
	
	
	
	public CompositeValueResultDto(List<String> value, String error, String metadataName) {
		super();
		this.value = value;
		this.error = error;
		this.metadataName = metadataName;
	}
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMetadataName() {
		return metadataName;
	}
	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}

	
	

}

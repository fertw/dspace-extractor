package ar.edu.unlp.extractor.metadata.tesis.dtos;

/**
 * DTO para valores simples
 * @author Fernando
 *
 */
public class SimpleValueResultDto {
	
	private  String value;
	private String error;
	private String metadataName;
	
	
	public SimpleValueResultDto(String value, String error, String metadataName) {
		super();
		this.value = value;
		this.error = error;
		this.metadataName = metadataName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
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

package ar.edu.unlp.extractor.metadata.tesis.engine;

import java.util.Properties;

public abstract class ExtractorEngine {
	
    private static Properties prop;
    
    
    protected static void setUp() throws Exception {
        prop = new Properties();
        prop.load(ExtractorEngine.class.getClassLoader().getResourceAsStream("dev-config.properties"));
    }
    
    protected static String getProfileDirectory() {
    	if (prop == null){
    		try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	 String path = prop.getProperty("language.profiles");
		 return path;
	}
    
    protected static String getModelsDirectory() {
    	if (prop == null){
    		try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	 String path = prop.getProperty("models.src");
		 return path;
	}
    
   

}

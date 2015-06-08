package ar.edu.unlp.extractor.metadata.tesis.engine.author;

import java.io.File;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;

import ar.edu.unlp.extractor.metadata.tesis.utils.PdfReaderEngine;

public class AuthorExtractorEngineTest extends TestCase {
    
    private Properties prop;
    
    protected void setUp() throws Exception {
        super.setUp();
        prop = new Properties();
        prop.load(AuthorExtractorEngineTest.class.getClassLoader().getResourceAsStream("config.properties"));
    }
    
    @Test
    public void testExtractAuthorsSpanish() throws Exception {
        
        String path = prop.getProperty("pdf.resource.author");
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".pdf")) {
                
                try {                      
                    
                    String page = PdfReaderEngine.extractTextByArea(file);
                    System.out.println("PDF :\n\n" + file.getName() + "\n\n"); 
                    System.out.println("Page Content:\n\n" + page + "\n\n");                    
                    AuthorEngine authorEngine = new AuthorEngine();
                    List<String> authors = authorEngine.extractAuthors(file);
                    
                    System.out.println("\n\n");
                    System.out.println("AUTORES-----------------------------------------------: ");
                    System.out.println("\n\n");
                    for (String author : authors) {
                        System.out.println("Autor: " + author);
                    }
                } catch (Exception e) {
                    
                }
                
            }
            
        }
    }
    
//    @Test
//    public void testExtractAuthorsEnglish() throws Exception {
//        
//        String path = prop.getProperty("pdf.resource.author");
//        File folder = new File(path);
//        File[] listOfFiles = folder.listFiles();
//        
//        for (int i = 0; i < listOfFiles.length; i++) {
//            File file = listOfFiles[i];
//            if (file.isFile() && file.getName().endsWith(".pdf")) {
//                
//                PdfReader reader = new PdfReader(file.getAbsolutePath());
//                System.out.println("This PDF has " + reader.getNumberOfPages() + " pages.");
//                String page = PdfTextExtractor.getTextFromPage(reader, 1);
//                System.out.println("Page Content:\n\n" + page + "\n\n");
//                System.out.println("Is this document tampered: " + reader.isTampered());
//                System.out.println("Is this document encrypted: " + reader.isEncrypted());
//                
//                AuthorEngine authorEngine = new AuthorEngine("en-sent.bin", "en-ner-person.bin");
//                List<String> authors = authorEngine.extractAuthors(page);
//                
//                System.out.println("\n");
//                System.out.println("\n");
//                System.out.println("AUTORES-----------------------------------------------: ");
//                System.out.println("\n");
//                System.out.println("\n");
//                for (String author : authors) {
//                    System.out.println("Autor: " + author);
//                }
//            }
//            
//        }
//    }
}

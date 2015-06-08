package ar.edu.unlp.extractor.metadata.tesis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

public class Trainer {
    
    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        Charset charset = Charset.forName("UTF-8");
        ObjectStream<String> lineStream =new PlainTextByLineStream(new FileInputStream("/home/yogi.singh/dev/java/nlp/data/en-ner-person.train"), charset);
        ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);       

        TokenNameFinderModel model = NameFinderME.train("en", "person", sampleStream, Collections.<String, Object>emptyMap());
        NameFinderME nfm = new NameFinderME(model);
        String sentence = "";


        BufferedReader br = new BufferedReader(new FileReader("/home/yogi.singh/dev/java/nlp/train.txt"));
        try
         {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null)
            {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            sentence = sb.toString();
         } 
        finally
        {
            br.close();
        }

        InputStream is1 = new FileInputStream("/home/yogi.singh/dev/java/nlp/data/en-token.bin");
        TokenizerModel model1 = new TokenizerModel(is1);

        Tokenizer tokenizer = new TokenizerME(model1);

        String tokens[] = tokenizer.tokenize(sentence);

        for (String a : tokens)
            System.out.println(a);

        Span nameSpans[] = nfm.find(tokens);
        for(Span s: nameSpans)
        {
            System.out.print(s.toString());
            System.out.print(" ");
            for(int index = s.getStart();index < s.getEnd();index++)
            {
                System.out.print(tokens[index] + " ");
            }
            System.out.println(" ");
        }
        
    }
    
}

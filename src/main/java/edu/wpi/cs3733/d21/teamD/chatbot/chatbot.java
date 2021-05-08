package edu.wpi.cs3733.d21.teamD.chatbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

public class chatbot {

  public chatbot() {}

  // Sentence detector
  public String[] SentenceDetect(String paragraph) throws InvalidFormatException, IOException {

    // always start with a model, a model is learned from training data
    InputStream is = new FileInputStream("openNLP/en-sent.bin");
    SentenceModel model = new SentenceModel(is);
    SentenceDetectorME sdetector = new SentenceDetectorME(model);

    String sentences[] = sdetector.sentDetect(paragraph);

    is.close();
    return sentences;
  }

  // tokenizer
  public String[] Tokenize(String paragraph) throws InvalidFormatException, IOException {
    InputStream is = new FileInputStream("openNLP/en-token.bin");
    TokenizerModel model = new TokenizerModel(is);
    Tokenizer tokenizer = new TokenizerME(model);

    String tokens[] = tokenizer.tokenize(paragraph);

    for (String a : tokens) System.out.println(a);
    is.close();
    return tokens;
  }

  // Name Finder
  public static void findName() throws IOException {
    InputStream is = new FileInputStream("openNLP/en-ner-person.bin");

    TokenNameFinderModel model = new TokenNameFinderModel(is);
    is.close();

    NameFinderME nameFinder = new NameFinderME(model);

    String[] sentence = new String[] {"Mike", "Smith", "is", "a", "good", "person"};

    Span nameSpans[] = nameFinder.find(sentence);

    for (Span s : nameSpans) System.out.println(s.toString());
  }
}

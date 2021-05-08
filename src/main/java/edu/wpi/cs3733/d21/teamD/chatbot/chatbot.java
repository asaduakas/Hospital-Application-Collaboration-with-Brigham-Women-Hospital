package edu.wpi.cs3733.d21.teamD.chatbot;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;
import opennlp.tools.doccat.*;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;

public class chatbot {

  public chatbot() {}

  /**
   * Train categorizer model as per the category sample training data we created.
   *
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  public DoccatModel trainCategorizerModel() throws FileNotFoundException, IOException {
    // faq-categorizer.txt is a custom training data with categories as per our chat
    // requirements.
    InputStreamFactory inputStreamFactory =
        new MarkableFileInputStreamFactory(new File("openNLP/faq-categorizer.txt"));
    ObjectStream<String> lineStream =
        new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
    ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

    DoccatFactory factory =
        new DoccatFactory(new FeatureGenerator[] {new BagOfWordsFeatureGenerator()});

    TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
    params.put(TrainingParameters.CUTOFF_PARAM, 0);

    // Train a model with classifications from above file.
    DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, factory);
    return model;
  }

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
  public String[] Tokenize(String sentence) throws InvalidFormatException, IOException {
    InputStream is = new FileInputStream("openNLP/en-token.bin");
    TokenizerModel model = new TokenizerModel(is);
    Tokenizer tokenizer = new TokenizerME(model);

    String tokens[] = tokenizer.tokenize(sentence);

    System.out.println("Tokens: ");
    for (String a : tokens) System.out.println(a);
    is.close();
    return tokens;
  }

  // POS Tagger
  public String[] detectPOSTags(String[] tokens) throws IOException {
    // Better to read file once at start of program & store model in instance
    // variable. but keeping here for simplicity in understanding.
    try (InputStream modelIn = new FileInputStream("openNLP/en-pos-maxent.bin")) {
      // Initialize POS tagger tool
      POSTaggerME myCategorizer = new POSTaggerME(new POSModel(modelIn));

      // Tag sentence.
      String[] posTokens = myCategorizer.tag(tokens);

      System.out.println("POS Tags: ");
      System.out.println(
          "POS Tags : " + Arrays.stream(posTokens).collect(Collectors.joining(" | ")));

      return posTokens;
    }
  }

  // Lemmatizer
  public String[] lemmatizeTokens(String[] tokens, String[] posTags)
      throws InvalidFormatException, IOException {
    // Better to read file once at start of program & store model in instance
    // variable. but keeping here for simplicity in understanding.
    try (InputStream modelIn = new FileInputStream("openNLP/en-lemmatizer.bin")) {

      // Tag sentence.
      LemmatizerME myCategorizer = new LemmatizerME(new LemmatizerModel(modelIn));
      String[] lemmaTokens = myCategorizer.lemmatize(tokens, posTags);

      System.out.println("Lemmas: ");
      System.out.println(
          "Lemmatizer : " + Arrays.stream(lemmaTokens).collect(Collectors.joining(" | ")));

      return lemmaTokens;
    }
  }

  // detect the category of the conversation
  public String detectCategory(DoccatModel model, String[] finalTokens) throws IOException {
    // Initialize document categorizer tool
    DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);

    // Get best possible category.
    double[] probabilitiesOfOutcomes = myCategorizer.categorize(finalTokens);
    String category = myCategorizer.getBestCategory(probabilitiesOfOutcomes);
    System.out.println("Category: " + category);

    return category;
  }

  // Username Name Finder
  // If there is any punctuation before or after the name, it wont recognize
  public LinkedList<String> findUserName(String paragraph) throws IOException {

    LinkedList<String> names = new LinkedList<String>();

    String[] sentence = paragraph.split(" ");
    InputStream is = new FileInputStream("openNLP/en-username.bin");

    TokenNameFinderModel model = new TokenNameFinderModel(is);
    is.close();

    NameFinderME nameFinder = new NameFinderME(model);
    Span nameSpans[] = nameFinder.find(sentence);

    for (Span s : nameSpans) System.out.println(s.getStart() + " : " + s.getEnd());

    for (Span s : nameSpans) {
      for (int i = s.getStart(); i < s.getEnd(); i++) {
        names.add(sentence[i]);
      }
    }

    return names;
  }
}

package edu.wpi.cs3733.d21.teamD.chatbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import opennlp.tools.namefind.*;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;

public class modelTraining {

  public modelTraining() {}

  public void usernameModelTraining() throws IOException {
    InputStreamFactory in = null;
    try {
      in =
          new MarkableFileInputStreamFactory(
              new File(
                  "openNLP/apache-opennlp-1.9.3-bin/apache-opennlp-1.9.3/bin/en-username.txt"));
    } catch (FileNotFoundException e2) {
      e2.printStackTrace();
    }

    ObjectStream sampleStream = null;
    try {
      sampleStream =
          new NameSampleDataStream(new PlainTextByLineStream(in, StandardCharsets.UTF_8));
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    //    TrainingParameters params = new TrainingParameters();
    //    params.put(TrainingParameters.ITERATIONS_PARAM, 140);
    //    params.put(TrainingParameters.CUTOFF_PARAM, 3);

    TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
    params.put(TrainingParameters.CUTOFF_PARAM, 1);

    System.out.println("sample stream " + sampleStream);

    TokenNameFinderModel nameFinderModel = null;
    try {
      nameFinderModel =
          NameFinderME.train(
              "en",
              null,
              sampleStream,
              params,
              TokenNameFinderFactory.create(null, null, Collections.emptyMap(), new BioCodec()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    File output = new File("openNLP/en-username.bin");
    FileOutputStream outputStream = new FileOutputStream(output);
    nameFinderModel.serialize(outputStream);
  }
}

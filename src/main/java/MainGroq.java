
import javiergs.tulip.groq.GroqClient;
import javiergs.tulip.groq.GroqConfig;

import java.io.InputStream;
import java.util.Properties;

public class MainGroq {
  public static void main(String[] args) throws Exception {

    Properties p = new Properties();
    try (InputStream in = MainGroq.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (in == null) throw new RuntimeException("config.properties not found in app resources");
      p.load(in);
    }

    String apiKey = p.getProperty("GROQ_API_KEY");
    String baseUrl = p.getProperty("GROQ_BASE_URL");
    String model = p.getProperty("GROQ_MODEL");

    GroqConfig cfg = new GroqConfig(apiKey, baseUrl, model);
    GroqClient groq = new GroqClient(cfg);

    String story = "As a user, I want to be able to write javadoc comments for my code ";
    String answer = groq.chat("Is the following a correct user story: " + story);
    System.out.println(answer);

    answer = groq.chat("Recommend a list of tasks for the story:  " + story);
    System.out.println(answer);
  }
}


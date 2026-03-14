import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javiergs.tulip.taiga.TaigaClient;
import javiergs.tulip.taiga.TaigaProject;
import javiergs.tulip.taiga.TaigaSprint;
import javiergs.tulip.taiga.TaigaTask;
import javiergs.tulip.taiga.TaigaUserStory;

public class MainTaiga {

  public static void main(String[] args) throws Exception {
    Properties cfg = loadProperties("config.properties");
    String host = require(cfg, "taiga.host");
    String username = require(cfg, "taiga.username");
    String password = require(cfg, "taiga.password");

    TaigaClient taiga = new TaigaClient(host);
    taiga.login(username, password);

    List<TaigaProject> projects = taiga.getMyProjects();

    for (TaigaProject p : projects) {
      printProjectHeader(p);

      List<TaigaSprint> sprints = taiga.getSprints(p.getId());

      printSprints(sprints);
      printStoriesPerSprint(taiga, sprints);
      printStories(taiga.getStories(p.getId()));
      printTasks(taiga.getTasks(p.getId()));

      System.out.println("--------------------------------------------------");
    }
  }

  private static void printProjectHeader(TaigaProject p) {
    System.out.println("PROJECT: " + p.getName());
    System.out.println("DESCRIPTION: " + p.getDescription());
    System.out.println("ID: " + p.getId());
  }

  private static void printSprints(List<TaigaSprint> sprints) {
    System.out.println("Sprints:");
    if (sprints.isEmpty()) {
      System.out.println("  (none)");
      return;
    }

    for (TaigaSprint s : sprints) {
      System.out.println("  - " + s);
    }
  }

  private static void printStoriesPerSprint(TaigaClient taiga, List<TaigaSprint> sprints) throws Exception {
    System.out.println("User Stories per Sprint:");

    if (sprints.isEmpty()) {
      System.out.println("  (no sprints)");
      return;
    }

    for (TaigaSprint sprint : sprints) {
      System.out.println("  Sprint: " + sprint.getName());

      List<TaigaUserStory> stories = taiga.getStoriesBySprint(sprint.getId());

      if (stories.isEmpty()) {
        System.out.println("    (no stories)");
        continue;
      }

      for (TaigaUserStory us : stories) {
        System.out.println("    - " + us);
      }
    }
  }

  private static void printStories(List<TaigaUserStory> stories) {
    System.out.println("User Stories:");
    if (stories.isEmpty()) {
      System.out.println("  (none)");
      return;
    }

    for (TaigaUserStory us : stories) {
      System.out.println("  - " + us);
    }
  }

  private static void printTasks(List<TaigaTask> tasks) {
    System.out.println("Tasks:");
    if (tasks.isEmpty()) {
      System.out.println("  (none)");
      return;
    }

    for (TaigaTask t : tasks) {
      System.out.println("  - " + t);
    }
  }

  private static Properties loadProperties(String resourceName) throws Exception {
    Properties p = new Properties();
    try (InputStream in = MainTaiga.class.getClassLoader().getResourceAsStream(resourceName)) {
      if (in == null) {
        throw new RuntimeException(resourceName + " not found in src/main/resources");
      }
      p.load(in);
    }
    return p;
  }

  private static String require(Properties p, String key) {
    String v = p.getProperty(key);
    if (v == null || v.isBlank()) {
      throw new IllegalArgumentException("Missing property: " + key);
    }
    return v.trim();
  }
}

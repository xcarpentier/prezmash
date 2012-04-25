import com.avaje.ebean.Ebean;
import play.Application;
import models.Candidat;
import play.GlobalSettings;
import play.Logger;
import play.libs.Yaml;

import java.util.List;
import java.util.Map;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        InitialData.insert(app);
        Logger.info("Application started...");
    }

    static class InitialData {

        public static void insert(Application app) {
            if (Ebean.find(Candidat.class).findRowCount() == 0) {

                Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml.load("initial-data.yml");

                // Insert candidats
                Ebean.save(all.get("candidats"));
            }
        }

    }


}



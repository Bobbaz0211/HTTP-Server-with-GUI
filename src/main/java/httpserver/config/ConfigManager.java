package httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import httpserver.utilPackage.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigManager {

    private static ConfigManager myConfigManager;
    private static Config myCurrentConfig;

    private ConfigManager(){
    }

    public static ConfigManager getInstance(){
        if (myConfigManager == null){
            myConfigManager = new ConfigManager();
        }
        return myConfigManager;
    }

    //Load config file with path
    public void loadConfigFile(String filePath)  {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
        }
        catch (FileNotFoundException e) {
            throw new HttpConfigException(e);
        }

        StringBuffer sb = new StringBuffer();

        int i;
        try {
            while ((i = fileReader.read()) != -1) {
                sb.append((char) i);
            }
        }
        catch (IOException e) {
            throw new HttpConfigException(e);
        }

        JsonNode conf = null;
        try {
            conf = Json.parse(sb.toString());
        }
        catch (IOException e) {
            throw new HttpConfigException("Error parsing the configuration file");
        }

        try {
            myCurrentConfig = Json.fromJson(conf, Config.class);
        }
        catch (JsonProcessingException e) {
            throw new HttpConfigException("Error parsing configuration file , internal");
        }


    }

    // Return currently loaded config
    public Config getCurrentConfig(){
        if(myCurrentConfig == null){
            throw new HttpConfigException("No current configuration set");
        }
        return myCurrentConfig;
    }
}

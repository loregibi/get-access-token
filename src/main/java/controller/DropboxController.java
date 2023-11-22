package controller;

import com.dropbox.core.DbxException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.DropboxModel;
import view.DropboxView;

import java.io.*;
import java.util.Iterator;

public class DropboxController {
    private final DropboxModel dropboxModel;
    private final DropboxView dropboxView;

    private final String jsonFileName = "SSME-Client-Configuration.json";

    public DropboxController(DropboxModel dropboxModel, DropboxView dropboxView) {
        this.dropboxModel = dropboxModel;
        this.dropboxView = dropboxView;
    }

    public void authorizeDropbox(String code) {
        try {
            if (dropboxModel != null) {
                String accessToken = dropboxModel.finishAuthorization(code);

                getJsonFile();
                saveAccessTokenToJson(accessToken);
            }
        } catch (DbxException e) {
            dropboxView.setStatus("Authorization Failed");
        }
    }

    private void saveAccessTokenToJson(String accessToken) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            File jsonFile = getJsonFile();

            JsonNode rootNode = objectMapper.readTree(jsonFile);
            JsonNode dropboxArrayNode = rootNode.path("Dropbox");

            if (dropboxArrayNode != null && dropboxArrayNode.isArray()) {
                boolean accessTokenPresent = false;
                Iterator<JsonNode> elements = dropboxArrayNode.elements();

                while (elements.hasNext()) {
                    JsonNode dropboxNode = elements.next();
                    JsonNode tokenNode = dropboxNode.path("DropboxToken");

                    if (tokenNode != null && tokenNode.isTextual()) {
                        String dropboxToken = tokenNode.asText();

                        if (accessToken.equals(dropboxToken)) {
                            accessTokenPresent = true;
                            break;
                        }
                    }
                }

                if (!accessTokenPresent) {
                    // Insert new token into a new JSON object
                    JsonNode newTokenNode = objectMapper.createObjectNode()
                            .put("DropboxToken", accessToken);

                    // Add new object to the Dropbox Array
                    ((com.fasterxml.jackson.databind.node.ArrayNode) dropboxArrayNode)
                            .add(newTokenNode);

                    // Write changes to the JSON file without altering the structure
                    ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
                    writer.writeValue(jsonFile, rootNode);

                    dropboxView.setStatus("Authorized. Access token generated!");
                } else {
                    dropboxView.setStatus("Access Token already exists!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getJsonFile() {
        String jarDir = System.getProperty("user.dir"); // Program execution directory
        File jsonFile = new File(jarDir, jsonFileName);

        if (!jsonFile.exists()) {
            // If the file doesn't exist, copy it from the project resource
            try (InputStream inputStream = DropboxController.class.getResourceAsStream("/" + jsonFileName);
                 FileOutputStream outputStream = new FileOutputStream(jsonFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonFile;
    }
}

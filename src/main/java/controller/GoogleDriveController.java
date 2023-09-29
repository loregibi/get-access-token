package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.GoogleDriveModel;
import view.GoogleDriveView;

import java.io.*;
import java.util.Iterator;

public class GoogleDriveController {
    private GoogleDriveModel googleDriveModel;
    private GoogleDriveView googleDriveView;

    private final String jsonFileName = "SSME-Client-Configuration.json";

    public GoogleDriveController(GoogleDriveModel googleDriveModel, GoogleDriveView googleDriveView) {
        this.googleDriveModel = googleDriveModel;
        this.googleDriveView = googleDriveView;
    }

    public void authorizeGoogleDrive(String code) {
        try {
            if(googleDriveModel != null) {
                String accessToken = googleDriveModel.finishAuthorization(code);

                getJsonFile();
                saveAccessTokenToJson(accessToken);
            }
        } catch (IOException e) {
            googleDriveView.setStatus("Authorization Failed");
        }
    }

    private void saveAccessTokenToJson(String accessToken) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            File jsonFile = getJsonFile();

            JsonNode rootNode = objectMapper.readTree(jsonFile);
            JsonNode googleDriveArrayNode = rootNode.get("GoogleDrive");

            if (googleDriveArrayNode != null && googleDriveArrayNode.isArray()) {
                boolean accessTokenPresent = false;
                Iterator<JsonNode> elements = googleDriveArrayNode.elements();

                while (elements.hasNext()) {
                    JsonNode googleDriveNode = elements.next();
                    JsonNode tokenNode = googleDriveNode.path("GoogleDriveToken");


                    if (tokenNode != null && tokenNode.isTextual()) {
                        String googleDriveToken = tokenNode.asText();

                        if (accessToken.equals(googleDriveToken)) {
                            accessTokenPresent = true;
                            break;
                        }
                    }
                }
                if (!accessTokenPresent) {
                    // Insert new token into a new JSON object
                    JsonNode newTokenNode = objectMapper.createObjectNode()
                            .put("GoogleDriveToken", accessToken);

                    // Add new object to the GoogleDrive Array
                    ((com.fasterxml.jackson.databind.node.ArrayNode) googleDriveArrayNode)
                            .add(newTokenNode);

                    // Write changes to the JSON file without altering the structure
                    ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
                    writer.writeValue(jsonFile, rootNode);

                    googleDriveView.setStatus("Authorized. Access token generated!");
                } else {
                    googleDriveView.setStatus("Access Token already exists!");
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

package model;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class GoogleDriveModel {
    private final static ConfigLoader cf = new ConfigLoader();
    private static final String CLIENT_ID = cf.getClientID();
    private static final String CLIENT_SECRET = cf.getClientSecret();
    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/drive");
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    private static GoogleAuthorizationCodeFlow flow;

    public GoogleDriveModel() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

         flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
    }

    public static String startAuthorization() {
        String authorizeUrl = flow.newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI)
                .build();
        return authorizeUrl;
    }

    public String finishAuthorization(String code) throws IOException {
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(REDIRECT_URI)
                .execute();
        return tokenResponse.getAccessToken();
    }
}

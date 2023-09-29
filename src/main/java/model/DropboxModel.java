package model;

import com.dropbox.core.*;
import java.util.Locale;

public class DropboxModel {
    private DbxWebAuth webAuth;
    private DbxRequestConfig config;

    public DropboxModel() {
        config = DbxRequestConfig.newBuilder("TokenAccess")
                .withUserLocaleFrom(Locale.getDefault())
                .build();

        DbxAppInfo appInfo = new DbxAppInfo("bvxcq0hejz79trw", "16sh3j7on98td2z");
        webAuth = new DbxWebAuth(config, appInfo);
    }

    public String startAuthorization() {
        String authorizeUrl = webAuth.authorize(DbxWebAuth
                .newRequestBuilder()
                .build());
        return authorizeUrl;
    }

    public String finishAuthorization(String code) throws DbxException {
        DbxAuthFinish authFinish = webAuth.finishFromCode(code);
        return authFinish.getAccessToken();
    }
}

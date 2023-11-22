package model;

import com.dropbox.core.*;
import java.util.Locale;

public class DropboxModel {
    private DbxWebAuth webAuth;
    private DbxRequestConfig config;
    private ConfigLoader cf = new ConfigLoader();

    public DropboxModel() {
        config = DbxRequestConfig.newBuilder("TokenAccess")
                .withUserLocaleFrom(Locale.getDefault())
                .build();

        DbxAppInfo appInfo = new DbxAppInfo(cf.getAppKey(), cf.getAppSecret());
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

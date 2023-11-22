package model;

import com.dropbox.core.*;
import java.util.Locale;

public class DropboxModel {
    private final DbxWebAuth webAuth;

    public DropboxModel() {
        final DbxRequestConfig config = DbxRequestConfig.newBuilder("TokenAccess")
                .withUserLocaleFrom(Locale.getDefault())
                .build();

        final ConfigLoader cf = new ConfigLoader();

        DbxAppInfo appInfo = new DbxAppInfo(cf.getAppKey(), cf.getAppSecret());
        webAuth = new DbxWebAuth(config, appInfo);
    }

    public String startAuthorization() {
        return webAuth.authorize(DbxWebAuth
                .newRequestBuilder()
                .build());
    }

    public String finishAuthorization(String code) throws DbxException {
        DbxAuthFinish authFinish = webAuth.finishFromCode(code);
        return authFinish.getAccessToken();
    }
}

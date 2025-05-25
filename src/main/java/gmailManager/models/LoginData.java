package gmailManager.models;

import models.Oggetti;
import org.json.JSONObject;

public class LoginData {

    private String clientId;
    private String projectId;
    private String authUri;
    private String tokenUri;
    private String authProviderX509CertUrl;
    private String clientSecret;

    public LoginData(){
        this("","","","","","");
    }

    public LoginData(String clientId, String projectId, String authUri, String tokenUri, String authProviderX509CertUrl, String clientSecret) {
        this.clientId = clientId;
        this.projectId = projectId;
        this.authUri = authUri;
        this.tokenUri = tokenUri;
        this.authProviderX509CertUrl = authProviderX509CertUrl;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getAuthUri() {
        return authUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public String getAuthProviderX509CertUrl() {
        return authProviderX509CertUrl;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public LoginData parseObjectFromJSON(JSONObject object){
        JSONObject web = object.getJSONObject("web");
        String clientId1 = web.getString("client_id");
        String projectId1 = web.getString("project_id");
        String authUri1 = web.getString("auth_uri");
        String tokenUri1 = web.getString("token_uri");
        String authProviderX509CertUrl1 = web.getString("auth_provider_x509_cert_url");
        String clientSecret1 = web.getString("client_secret");
        return new LoginData(clientId1,projectId1,authUri1,tokenUri1,authProviderX509CertUrl1,clientSecret1);
    }


}

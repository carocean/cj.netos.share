package cj.netos.share.service;

import cj.netos.share.AccessTokenInfo;
import cj.netos.share.IAccessTokenService;
import cj.studio.ecm.IServiceSite;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceSite;

@CjService(name = "accessTokenService")
public class AccessTokenService implements IAccessTokenService {
    @CjServiceSite
    IServiceSite site;

    @Override
    public AccessTokenInfo getInfo() {
        String appKey = site.getProperty("appKey");
        String appSecret = site.getProperty("appSecret");
        String nonce = System.currentTimeMillis() + "";
        String accessToken = String.format("%s%s%s", appKey, appSecret, nonce);
        AccessTokenInfo info = new AccessTokenInfo();
        info.setAccessToken(accessToken);
        info.setNonce(nonce);
        return info;
    }
}

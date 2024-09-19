package appsys.free.constru_app.auth;

import appsys.free.constru_app.auth.entity.Rol;
import appsys.free.constru_app.auth.entity.UserApp;

import appsys.free.constru_app.auth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InfoAddToken implements TokenEnhancer {
    @Autowired
    IUserService iUserService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        UserApp user = iUserService.findByUserName(authentication.getName());
        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("noDocumento", user.getNoDocument());
        info.put("nombres", user.getNames());
        info.put("apellidos", user.getSurnames());
        //info.put("roles", user.getRoles());
        info.put("roles", getRoles(user.getRoles()));


        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }

    public String getRoles(List<Rol> roles) {
        String rolesString = "";
        if (!roles.isEmpty()) {
            for (Rol rol : roles) {
                rolesString += rol.getName() + ",";
            }
        }
    return rolesString;
    }
}

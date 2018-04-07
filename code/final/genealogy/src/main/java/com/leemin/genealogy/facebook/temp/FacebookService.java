package com.leemin.genealogy.facebook.temp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class FacebookService {

    @Value("${spring.social.facebook.appId}")
    String facebookAppId;
    @Value("${spring.social.facebook.appSecret}")
    String facebookSecret;

    String accessToken ="";

    public String createFacebookAuthorizationURL(){
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        OAuth2Operations          oauthOperations   = connectionFactory.getOAuthOperations();
        OAuth2Parameters          params            = new OAuth2Parameters();
        params.setRedirectUri("https://localhost:8080/facebook/");
        params.setScope("public_profile,email,user_birthday");
        return oauthOperations.buildAuthorizeUrl(params);
    }

    public void createFacebookAccessToken(String code) {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
        AccessGrant               accessGrant       = connectionFactory.getOAuthOperations().exchangeForAccess(code, "https://localhost:8080/connect/facebook", null);
        accessToken = accessGrant.getAccessToken();
        System.out.println("accessToken:" + accessToken);
    }
    public String getName() {
        Facebook facebook = new FacebookTemplate(accessToken);
//https://localhost:8080/facebook?code=AQDLCBMIiKRyuJjMGI5jiAzNwrCqJoDD7HlNbw1xZOXPneoLIuctcCO0LB6LWq5K74Pf9VJXE6DisHRV5aIBqK0IvB18e6ZZVYfePydwIzJSdWxnPhuSc0XGz_YwGd72VRlN9kPyljurW4WXyoj6kVfLCYTHLLKVPcFFUJ2fUyR7nBmpXc7cZ3u7Eq3HteG35n5-tl6q_4_RLr7FBmvO9wKX_ieikhjFp3wvfoWlkF97R_HgH-ZKwhJkOXnFS7NTPnfOY_klZKplGfviUnSXIeHrWfhhYyUOOPrGrWpe4m6Q-3i1wK0-jTuw2QDe6Ee7-2EEvP45O1i3i6JVEAc8LLw2#_=_
        String [] fields = {
                "id",
                "cover",
                "name",
                "first_name",
                "last_name",
                "age_range",
                "link",
                "gender",
                "locale",
                "picture",
                "timezone",
                "updated_time",
                "verified",
                "email"
        };
        User userProfile = facebook.fetchObject("me", User.class, fields);
        System.out.println(userProfile.getId());
        System.out.println(userProfile.getAgeRange().getMin());
        System.out.println(userProfile.getName());
        System.out.println(userProfile.getFirstName());
        System.out.println(userProfile.getLastName());
        System.out.println(userProfile.getGender());
        System.out.println(userProfile.getLink());
        System.out.println(userProfile.getLocale().getLanguage());
        System.out.println(userProfile.getLocale().getCountry());
        System.out.println(userProfile.getEmail());
        LinkedHashMap picture = (LinkedHashMap)userProfile.getExtraData()
                                                          .get("picture");
        LinkedHashMap dataPicture =(LinkedHashMap) picture.get("data");

        System.out.println(dataPicture.get("width") + "|" + dataPicture.get("height") + "|" +dataPicture.get("url") );
        return userProfile.getName();
    }
}
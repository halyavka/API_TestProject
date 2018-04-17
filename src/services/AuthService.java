package services;

import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.ClassNameUtil;
import utils.PropertyLoader;
import utils.TestEntity;
import utils.Util;


public class AuthService {

    private TestEntity entity;
    private static final Logger log = Logger.getLogger(ClassNameUtil.getCurrentClassName());
    private static final String AUTH_URL = PropertyLoader.loadProperty("auth.url");
    private static final String SIGNIN_URL = AUTH_URL + "signin";

    public AuthService(TestEntity entity) {
        this.entity = entity;
    }

    private JsonObject signIn() {
        entity.stringBuilder.append("SignIn player with login ").append(entity.player.getLogin()).append(" <br>");
        log.info("SignIn player with login " + entity.player.getLogin());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("brandId", entity.player.getBrandId());
        jsonObject.addProperty("login", entity.player.getEmail());
        jsonObject.addProperty("password", entity.player.getPassword());

        return (JsonObject) entity.client
                .post(SIGNIN_URL, jsonObject.toString(), 200)
                .setHeader("Content-Type", "application/json")
                .execute();
    }

    public void signInPlayer() {
        Util.sleep(3000);
        JsonObject jsonObject = signIn();
        Assert.assertTrue(jsonObject.has("token"), "There is not token in the response!");
        entity.player.setToken(jsonObject.get("token").getAsString());
    }

}

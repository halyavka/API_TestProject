package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.ClassNameUtil;
import utils.PropertyLoader;
import utils.TestEntity;
import utils.Util;

import java.util.Objects;

import static runners.Runner.BRAND_ID;


public class ProfileService {

    private TestEntity entity;
    private static final Logger log = Logger.getLogger(ClassNameUtil.getCurrentClassName());
    private static final String PROFILE_URL = PropertyLoader.loadProperty("profile.url");
    private static final String SIGNUP_URL = PROFILE_URL + "public/signup";
    private static final String PROFILES_URL = PROFILE_URL + "profiles/";

    public ProfileService(TestEntity entity) {
        this.entity = entity;
    }

    public JsonObject getSignUpOptions(String brandId) {
        entity.stringBuilder.append("Get SignUp options <br>");
        log.info("Get signUp options");
        return (JsonObject) entity.client
                .options(SIGNUP_URL + "?brandId=" + brandId, 200)
                .setHeader("Content-Type", "application/json")
                .execute();
    }

    private JsonObject signUp() {
        entity.stringBuilder.append("SignUp new player <br>");
        log.info("SignUp new player");
        String json = new Gson().toJson(entity.player);
        return (JsonObject) entity.client
                .post(SIGNUP_URL + "?brandId=" + BRAND_ID, json, 200)
                .setHeader("Content-Type", "application/json")
                .execute();
    }

    private JsonObject getProfiles() {
        entity.stringBuilder.append("Get profile by user uuid <br>");
        log.info("Get profile by user uuid");
        String uuid = entity.player.getUuid();
        return (JsonObject) entity.client
                .get(PROFILES_URL + uuid + "?token=" + entity.player.getToken(), 200, 404)
                .setHeader("Content-Type", "application/json")
                .execute();
    }

    public void signUpNewPlayer() {
        JsonObject jsonObject = signUp();
        Assert.assertTrue(jsonObject.has("playerUUID"), "There is not playerUUID key in the response!");
        entity.player.setUuid(jsonObject.get("playerUUID").getAsString());
    }

    public void getProfileFromElasticSearch(int attempts) {
        Object object = getProfiles();
        if (object == null) {
            //todo: need to add additional verification: is service is alive and available.
            //todo: Also we can check logs of service
            Util.sleep(3 * 1000); // wait 3 seconds and try again
            attempts --;
            if (attempts == 0) {
                Assert.fail("Could not find profile " + entity.player.getUuid() + "! Please check elastic service!");
            }
            getProfileFromElasticSearch(attempts);
        }
        //todo: in this step we can check response (JSON object).
        Assert.assertEquals(((JsonObject) Objects.requireNonNull(object)).get("email").getAsString(), entity.player.getEmail(),
                "Login from response is not equal original login!");
    }


}

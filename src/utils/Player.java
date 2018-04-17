package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static runners.Runner.BRAND_ID;


public class Player {

    private JsonObject postObj;

    private String uuid;
    private String token;
    private String address;
    private String affiliateId;
    private String birthDate;
    private String brandId;
    private String btag;
    private List<String> campaigns = new ArrayList<>();
    private String city;
    private String country;
    private String currency;
    private String email;
    private String firstName;
    private String gender;
    private String identifier;
    private String languageCode;
    private String lastName;
    private String login;
    private String password;
    private String phone;
    private String phoneCode;
    private String postCode;
    private String termsAndConditionsId;
    private HashMap<String, Boolean> subscriptions = new HashMap<>();

    public Player(JsonObject signUpOptions) {
        long id = System.currentTimeMillis();
        getPostOptions(signUpOptions);
        termsAndConditionsId = ((JsonObject) postObj.get("termsAndConditionsId")).get("latestUUID").getAsString();
        subscriptions.put("marketingMail", true);
        subscriptions.put("marketingNews", true);
        subscriptions.put("marketingSMS", true);
        postCode = "03056";
        phone = "09326335";
        password = "Password1";
        login = "test" + id;
        lastName = "Automation";
        setLanguageCode();
        identifier = "test" + id;
        gender = "MALE";
        firstName = "TestUser";
        email = "test" + id + "@test.com";
        setCurrency();
        setCountryAndPhoneCode();
        city = "Kiev";
        campaigns.add("newTest");
        btag = "";
        brandId = BRAND_ID;
        birthDate = "2000-02-09";
        affiliateId = "";
        address = "New Street 12";
    }

    private void setLanguageCode() {
        JsonArray langArray = (JsonArray) ((JsonObject) postObj.get("languageCode")).get("list");
        this.languageCode = langArray.get(Util.getRandom(langArray.size())).getAsString();
    }

    private void setCurrency() {
        JsonArray currencyArray = (JsonArray) ((JsonObject) postObj.get("currency")).get("list");
        this.currency = currencyArray.get(Util.getRandom(currencyArray.size())).getAsString();
    }

    private void setCountryAndPhoneCode() {
        JsonArray countryArray = (JsonArray) ((JsonObject) postObj.get("country")).get("list");
        JsonObject countryObj = (JsonObject) countryArray.get(Util.getRandom(countryArray.size()));
        this.country = countryObj.get("countryCode").getAsString();
        this.phoneCode = countryObj.get("phoneCode").getAsString();
    }

    private void getPostOptions(JsonObject signUpOptions) {
        postObj = (JsonObject) signUpOptions.get("post");
    }

    public String getEmail() {
        return email;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

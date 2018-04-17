package testSuites;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import runners.Runner;
import utils.Player;


public class TestSuite extends Runner {

    @BeforeClass
    public void before() {
        entity.player = new Player(profileService.getSignUpOptions(BRAND_ID));
    }

    @Test
    public void test() {
        Reporter.log("Scenario: Test scenario");
        profileService.signUpNewPlayer();
        authService.signInPlayer();
        profileService.getProfileFromElasticSearch(10);
    }

}

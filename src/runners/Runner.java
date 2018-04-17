package runners;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import services.AuditService;
import services.AuthService;
import services.ProfileService;
import utils.*;

public class Runner {

    private static final Logger log = Logger.getLogger(ClassNameUtil.getCurrentClassName());
    public static final String BRAND_ID = PropertyLoader.loadProperty("brand.id");

    public TestEntity entity;
    protected ProfileService profileService;
    protected AuthService authService;
    protected AuditService auditService;


    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        log.info("Start test suite");
        initClient();
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext context) {
        auditService = new AuditService(entity);
        authService = new AuthService(entity);
        profileService = new ProfileService(entity);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethodMain(ITestContext context) {
        log.info("Start test " + context.getCurrentXmlTest().getName());
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestContext context, ITestResult testResult) {
        String testName = context.getCurrentXmlTest().getName() + "_" + testResult.getName();
        configureReport(testResult);
        String status = (testResult.isSuccess()) ? "PASSED" : "FAILED";
        log.info("Test " + testName + " finished, STATUS = " + status);
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite(ITestContext context) {
        log.info("Test suite finished");
    }

    public void initClient() {
        entity = new TestEntity(new TestHttpClient());
    }

    private void configureReport(ITestResult testResult) {
        Reporter.setCurrentTestResult(testResult);
        Reporter.log("<br>");
        Reporter.log(entity.stringBuilder.toString());
        Reporter.log("<br>");
        entity.stringBuilder = new StringBuilder();
    }

}

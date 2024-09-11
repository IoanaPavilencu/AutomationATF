package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProfilePage;
import requestObject.RequestUser;
import responseObject.ResponseToken;
import responseObject.ResponseUser;

import java.time.Duration;

public class CreateUserFETest {

    public String baseURI = "https://demoqa.com";
    public RequestUser requestBody;
    public WebDriver driver;
    public String userId;
    public String token;


    @Test
    public void testMethod() {
        System.out.println("===== STEP 1 : Create account =====");
        createAccount();

        System.out.println("===== STEP 2 : Generate Token =====");
        generateToken();

        System.out.println("===== STEP 3 : Get user details =====");
        validateAccountBE();

        System.out.println("===== STEP 4 : Login user =====");
        loginApplication();

        System.out.println("===== STEP 5 : Delete user =====");
        deleteAccountFE();

        System.out.println("===== STEP 6 : Get user details =====");
        validateAccountBE();

    }

    public void createAccount(){

        // Definesc configurarea clientului
        // Definim un request(pe baza clientului)
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri(baseURI);

       requestBody = new RequestUser("src/test/resources/createUser.json");

        // Adaugam request body
        System.out.println(requestBody);
        request.body(requestBody);

        // Executam request-ul de tip POST la un endpoint specific
        Response response = request.post("/Account/v1/User");

        // Validam response status code
        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(),201);

        Assert.assertTrue(response.getStatusLine().contains("Created"));

        System.out.println(response.getStatusCode());
        ResponseUser responseBody = response.getBody().as(ResponseUser.class);
        Assert.assertTrue(responseBody.getUsername().equals(requestBody.getUserName()));
        System.out.println(responseBody);
        userId = responseBody.getUserId();
    }

    public void generateToken(){

        // Definesc configurarea clientului
        // Definim un request(pe baza clientului)
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri(baseURI);

        // Adaugam request body
        request.body(requestBody);

        // Executam request-ul de tip POST la un endpoint specific
        Response response = request.post("/Account/v1/GenerateToken");

        // Validam response status code
        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(),200);

        Assert.assertTrue(response.getStatusLine().contains("OK"));

        ResponseToken responseBody = response.getBody().as(ResponseToken.class);
        System.out.println(responseBody.getToken());

        System.out.println(responseBody);

        token = responseBody.getToken();

        //System.out.println(response.getHeaders());
    }
    public void validateAccountBE(){
        RequestSpecification request = RestAssured.given();
        request.contentType(ContentType.JSON);
        request.baseUri(baseURI);

        request.header("Authorization", "Bearer" + token);

        Response response = request.get("/Account/v1/User/" + userId);

        response.body().prettyPrint();


    }

    public void loginApplication(){
        driver = new ChromeDriver();
        driver.get("https://demoqa.com/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        LoginPage loginPage =new LoginPage(driver);
        loginPage.loginIntoApplication(requestBody);

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.validateLoginProcess(requestBody);

    }

    public void deleteAccountFE(){

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.deleteAccount();

    }


}

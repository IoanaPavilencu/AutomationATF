package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import requestObject.RequestUser;
import responseObject.ResponseToken;
import responseObject.ResponseUser;
import service.AccountService;

import java.time.Duration;

public class CreateUserBETest {

    public AccountService accountService;
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

        System.out.println("===== STEP 4 : Delete user =====");
        deleteAccountBE();

        System.out.println("===== STEP 5 : Login user =====");
        loginApplication();

        System.out.println("===== STEP 6 : Get user details =====");
        validateAccountBE();

    }

    public void createAccount(){

        // Definesc configurarea clientului
        // Definim un request(pe baza clientului)

       requestBody = new RequestUser("src/test/resources/createUser.json");
        accountService =new AccountService();

        ResponseUser responseBody = accountService.createAccount(requestBody);
        userId = responseBody.getUserId();
    }

    public void generateToken(){

        // Validam response status code

        ResponseToken responseBody = accountService.generateToken(requestBody);
        token = responseBody.getToken();

    }
    public void validateAccountBE(){

        accountService.validateAccountBE(token, userId);
    }

    public void loginApplication(){
        driver = new ChromeDriver();
        driver.get("https://demoqa.com/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        LoginPage loginPage =new LoginPage(driver);
        loginPage.loginIntoApplication(requestBody);
        loginPage.validateLoginError();
    }



    public void deleteAccountBE(){

        accountService.deleteAccountBE(token, userId);
    }
}

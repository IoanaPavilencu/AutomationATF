import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import requestObject.RequestUser;
import responseObject.ResponseToken;
import responseObject.ResponseUser;

public class CreateUserTest {

    public String baseURI = "https://demoqa.com";
    public RequestUser requestBody;
    @Test
    public void testMethod() {
        System.out.println("===== STEP 1 : Create account =====");
        createAccount();

        System.out.println("===== STEP 2 : Generate Token =====");
        generateToken();
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

        System.out.println(response.getHeaders());
    }
}

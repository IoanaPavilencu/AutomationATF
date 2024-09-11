package client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIClient {
    public String baseURI = "https://demoqa.com";

    // Clasa in care configuram clientul
    // Facem o metoda in care setam configurarile de client
    // Facem o metoda in care setam actiunea pe care o poate face clientul

    public Response performRequest(String requestType, RequestSpecification requestSpecification, String endpoint){
        switch (requestType){
            case "POST":
                return prepareClient(requestSpecification).post(endpoint);
            case "GET":
                return prepareClient(requestSpecification).get(endpoint);
            case "DELETE":
                return prepareClient(requestSpecification).delete(endpoint);
        }
        return null;
    }

    public RequestSpecification prepareClient(RequestSpecification requestSpecification){
        requestSpecification.contentType(ContentType.JSON);
        requestSpecification.baseUri(baseURI);
        return requestSpecification;
    }
}

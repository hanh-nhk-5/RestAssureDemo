import files.PayLoad;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FirstTest {
    public static void main(String[] args){
        RestAssured.baseURI= "https://rahulshettyacademy.com";
        JsonPath responseJson=  given().log().all().queryParam("key", "qaclick123")
                                    .header("Content-Type", "application/json")
                                    .body(PayLoad.addPlace())
                                .when().post("maps/api/place/add/json")
                                .then().log().all().assertThat()
                                                        .statusCode(200)
                                                        .body("scope", equalTo("APP"))
                                                        .header("server", equalToIgnoringCase("Apache/2.4.52 (Ubuntu)"))
                                .extract().body().jsonPath();

        String placeId= responseJson.getString("place_id");
        String newAddress= "70 winter walk, USA";
        given().log().all().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\""+ newAddress +"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
        .when().put("maps/api/place/update/json")
        .then().log().all().assertThat().statusCode(200).body("msg", equalToIgnoringCase("Address successfully updated"));

        responseJson=   given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
                        .when().get("maps/api/place/get/json")
                        .then().assertThat().statusCode(200).extract().body().jsonPath();

        Assert.assertEquals(responseJson.getString("address"), newAddress);
    }
}

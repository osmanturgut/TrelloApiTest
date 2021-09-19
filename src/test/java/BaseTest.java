
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;


import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected String boardId;
    public static HashMap map = new HashMap<>();

    @BeforeSuite
    public void setup() {

        map.put("key", "f7985a0fd5b5605c8484df2ccc1d9a76");
        map.put("token", "0bb22f90c1c59d3a1b1562efe2eb120b9e09ecefcece264fe694c86555ef6f83");


        RestAssured.baseURI = "https://api.trello.com/1";
    }

    @BeforeClass
    public void createBoard() {
        Response response =
                given()
                        .header("Accept-Encoding", "gzip,deflate")
                        .contentType("application/json")
                        .queryParam("name", "adeBoard")
                        .body(map)
                        .log().all().
                        when()
                        .post("/boards");

        response.then()
                .statusCode(200)
                .extract().body().jsonPath().get("name").equals("adeBoard");

        boardId = (String) response.then()
                .extract().jsonPath().getMap("$").get("id");
        System.out.println(boardId);
    }

    @AfterClass
    public void tearDown() {
        given()
                .body(map)
                .pathParam("id", boardId)
                .contentType(ContentType.JSON).log().all().
                when()
                .delete(Constants.deleteBoard).
                then()
                .statusCode(200)
                .contentType(ContentType.JSON);

    }

}

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class TrelloTest extends BaseTest {

    protected String idList;
    protected String idCard1;
    protected String idCard2;


    @Test(priority = 1)
    public void createList() {

        Response response =
                given()
                        .body(map)
                        .header("Accept-Encoding", "gzip,deflate")
                        .queryParam("name", "adeList")
                        .queryParam("idBoard", boardId)
                        .contentType(ContentType.JSON)
                        .log().all().
                        when()
                        .post(Constants.getLists);
        response.then()
                .statusCode(200)
                .body("name", equalTo("adeList"));

        idList = (String) response.then()
                .extract().jsonPath().getMap("$").get("id");
        System.out.println(idList);

    }


    @Test(priority = 2)
    public void addCard() {
        Response response1 =
                given()
                        .body(map)
                        .header("Accept-Encoding", "gzip,deflate")
                        .queryParam("name", "adeCard_1")
                        .queryParam("idList", idList)
                        .contentType(ContentType.JSON)
                        .log().all().
                        when()
                        .post(Constants.createCard);

        response1.then()
                .statusCode(200);

        idCard1 = (String) response1.then()
                .extract().jsonPath().getMap("$").get("id");
        System.out.println("idCard1: " + idCard1);
        Response response2 =
                given()
                        .body(map)
                        .header("Accept-Encoding", "gzip,deflate")
                        .queryParam("name", "adeCard_2")
                        .queryParam("idList", idList)
                        .contentType(ContentType.JSON)
                        .log().all().
                        when()
                        .post(Constants.createCard);

        response2.then()
                .statusCode(200);

        idCard2 = (String) response2.then()
                .extract().jsonPath().getMap("$").get("id");
        System.out.println("idCard2: " + idCard2);


    }


    @Test(priority = 3)
    public void updateCard() {

        given()
                .pathParam("cardId", idCard1)
                .header("Accept-Encoding", "gzip,deflate")
                .queryParam("key", "f7985a0fd5b5605c8484df2ccc1d9a76")
                .queryParam("token", "0bb22f90c1c59d3a1b1562efe2eb120b9e09ecefcece264fe694c86555ef6f83")
                .body(Constants.changeColor)
                .contentType(ContentType.JSON)
                .log().all().
                when()
                .put(Constants.updateCard).
                then()
                .statusCode(200);
    }

    @Test(priority = 4)
    public void deleteCard() {

        given()
                .header("Accept-Encoding", "gzip,deflate")
                .pathParam("cardId", idCard1)
                .body(map)
                .contentType(ContentType.JSON)
                .log().all().
                when()
                .delete(Constants.updateCard).
                then()
                .statusCode(200);

        given()
                .pathParam("cardId", idCard2)
                .body(map)
                .contentType(ContentType.JSON)
                .log().all().
                when()
                .delete(Constants.updateCard).
                then()
                .statusCode(200);
    }
}
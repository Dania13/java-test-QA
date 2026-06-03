package PDSAPI.tests;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;

public class ApiTests {

    @Test
    public void testGetUser() {
        // Базовый URL, общий для всех запросов
        given().baseUri("https://reqres.in")
                .when()
                .get("/docs")
                .then()
                .statusCode(200) // Проверяем статус код
//                .body("data.id", equalTo(2)) // Проверяем значение поля в JSON
                .log().all(); // Логируем детали ответа
    }
}
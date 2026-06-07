package PDSAPI.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@Epic("Тестовый сайт")
@Feature("Проверка роботоспособности")
public class ApiTests {

    @Test
    @Description("Проверка открытия сайта")
    public void testGetUser() {
        // Базовый URL, общий для всех запросов
        given().baseUri("https://reqres.in")
                .filter(new AllureRestAssured())
                .when()
                .get("/docs")
                .then()
                .statusCode(200); // Проверяем статус код
    }
}

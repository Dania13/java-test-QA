package PDSAPI.tests.methods;

import PDSAPI.actions.Auth;
import PDSAPI.models.DictRequest;
import PDSAPI.specs.ConstantValues;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;


/**
 * Тесты для проверки метода получения справочных значений
 */
@Epic("Проверка API методов продукта")
@Feature("Метод Получения справочных значений")
public class DictTests {

    private String sessionToken;

    /**
     * Предустановка с авторизацией
     */
    @BeforeEach
    @Step("Предустановка")
    public void setUp() {
        sessionToken = Auth.getCachedSessionToken(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
    }

    /**
     * Позитивный тест получения справочника
     */
    @Test
    @Disabled("в процессе отладки")
    @Description("Получение справочника регионов")
    public void successGetDict(){
        DictRequest dict = DictRequest.builder()
                .accID(sessionToken)
                .product("ПДС")
                .dictionaryCode("region")
                .build();
        given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(dict)
                .filter(new AllureRestAssured())
        .when()
                .post(ConstantValues.DICT_ENDPOINT)
        .then()
                .statusCode(200)
//                .body("types.dictionaries.code", equalTo("region"))
        ;
    }
}

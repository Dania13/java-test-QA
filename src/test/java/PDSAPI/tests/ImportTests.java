package PDSAPI.tests;

import PDSAPI.actions.Auth;
import PDSAPI.models.*;
import PDSAPI.models.Object;
import PDSAPI.specs.ConstantValues;
import helpers.InnGenerator;
import helpers.SNILSGenerator;
import io.restassured.http.ContentType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ImportTests {
    private String sessionToken;

    @BeforeEach
    public void setUp() {
        sessionToken = Auth.loginUser(
                ConstantValues.BASE_URL,
                ConstantValues.AUTH_ENDPOINT,
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
    }

    @Test
    public void successImportWithPOJO(){

        String INN = InnGenerator.getINNFL();
        String SNILS = SNILSGenerator.getSNILS(true);

        Product product = new Product("Программа долгосрочных сбережений граждан (ПДС)");

        List<Parameter> parametsItems = new ArrayList<>();

        parametsItems.add(new Parameter("dogovor.predvRaschet","Предварительный расчет", false,"Логический"));

        Parameters parameters = new Parameters(parametsItems);

        Risk risks = new Risk("true", "Пенсионное накопление", 4000);

        RiskInfo riskInfo = new RiskInfo(Collections.singletonList(risks));

        List<Object> Objects = new ArrayList<>();

        Objects.add(new Object("Объект страхования", null, riskInfo));

        InsuranceObjects insuranceObjects = new InsuranceObjects(Objects);

        Insurant insurant = getInsurant(SNILS, INN);

        PolicyImport policyImport = PolicyImport.builder()
                .currCode("RUR")
                .insCompanyName("ООО СК «Согласие-Вита»")
                .comment("Получили идеальный расчет")
                .insurant(insurant)
                .insuranceObjects(insuranceObjects)
                .parameters(parameters)
                .product(product).build();

        ImportRequest importRequest = new ImportRequest(policyImport);

        ImportResponse response = given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", sessionToken)
                .contentType(ContentType.JSON)
                .body(importRequest)
//                .log().all()
                .when()
                .post(ConstantValues.IMPORT_ENDPOINT).
                then()
                .statusCode(200)
//                .log().all()
                .extract()
                .as(ImportResponse.class)
        ;

//        String CalcID = response.getPolicy().getCalcID();
//        System.out.println("CalcID="+CalcID);
        assertNotNull(response.getPolicy().getCalcID());
        assert response.getPolicy().getInsPremTotal() > 0;
        assertNull(response.getWarnings());
    }

    private static @NotNull Insurant getInsurant(String SNILS, String INN) {
        Address residenceAddress = new Address(340063, "Пермский край, Пермский р-н, с Гамово, ул. 50 лет Октября, д. 11", "Россия", "Пермский");
        Address factAddress = new Address(347863, "614520, Россия, Пермский край, Пермский р-н, п.Кукуштан , ул. Чапаева, д. 1", "Россия", "Пермский");
        Document document = new Document("2020-01-01T12:00:00.000Z", "001-001", "001011", "ОВД1", "0101", "ПАСПОРТ_РФ");
        Physical physical = new Physical("1978-04-07T12:00:00.000Z", "Гор. Лермонтов", "Россия", document, "qa@virtusystems.ru", factAddress, "Анна", "Иванова", "Анновна", "false", "+7 (919) 124-43-86", residenceAddress, "F", SNILS, INN);
        return new Insurant(physical, "ФЛ");
    }

}

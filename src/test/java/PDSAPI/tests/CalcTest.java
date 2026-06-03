package PDSAPI.tests;

import PDSAPI.models.*;
import PDSAPI.models.Object;
import PDSAPI.specs.ConstantValues;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CalcTest {

    private AuthResponse responseAuth;

    @BeforeEach
    public void auth(){
        AuthRequest user = AuthRequest.builder()
                .login(ConstantValues.LOGIN_AUTH)
                .password(ConstantValues.PASSWORD_AUTH).build();
        responseAuth = given()
                .baseUri(ConstantValues.BASE_URL)
                .contentType(ContentType.JSON)
                .body(user).
                when()
                .post(ConstantValues.AUTH_ENDPOINT).
                then()
                .statusCode(200)
                .extract()
                .as(AuthResponse.class);
    }

    @Test
    public void successCalc(){

        String requestBody = """
                {
                   "productType": "Рисковое страхование",
                   "policyCalc": {
                     "currCode": "RUR",
                     "insCompanyName": "ООО СК «Согласие-Вита»",
                     "insuranceObjects": {
                       "objects": [
                         {
                           "name": "Объект страхования",
                           "parameters": null,
                           "riskInfo": {
                             "risks": [
                               {
                                 "insured": "true",
                                 "name": "Пенсионное накопление",
                                 "insPrem": 4000
                               }
                             ]
                           }
                         }
                       ]
                     },
                     "onlinePayment": false,
                     "parameters": {
                       "parameters": [
                         {
                           "code": "dogovor.vkladchikPol",
                           "name": "Возраст",
                           "stringValue": "жен.",
                           "type": "Строка"
                         },
                         {
                           "code": "dogovor.vkladchikVozrast",
                           "name": "Возраст вкладчика",
                           "intValue": "45",
                           "type": "Целое"
                         },
                         {
                           "code": "dogovor.dohodvmes",
                           "name": "Среднемесячный доход",
                           "decimalValue": 60000,
                           "type": "Вещественный"
                         },
                         {
                           "code": "dogovor.povtornyeVznosy",
                           "name": "Повторные взносы",
                           "decimalValue": 4500,
                           "type": "Вещественный"
                         },
                         {
                           "code": "dogovor.periodichnostVznosov",
                           "name": "Периодичность повторных взносов",
                           "stringValue": "ежеквартально",
                           "type": "Строка"
                         },
                         {
                           "code": "dogovor.srokNakoplenij",
                           "intValue": 25,
                           "name": "Срок накоплений",
                           "type": "Целое"
                         },
                         {
                           "code": "dogovor.srokEzhemesVyplat",
                           "name": "Срок ежемесячных выплат",
                           "intValue": 10,
                           "type": "Целое"
                         },
                         {
                           "code": "dogovor.perevestiPensionNakopleniya",
                           "stringValue": "да",
                           "type": "Строка",
                           "name": "Сумма кредита"
                         },
                         {
                           "code": "dogovor.razmerPensionNakoplenij",
                           "name": "Сумма пенсионных накоплений по ОПС",
                           "decimalValue": 6500,
                           "type": "Вещественный"
                         },
                         {
                           "code": "dogovor.investirovatNalogovyjVychet",
                           "stringValue": "да",
                           "type": "Строка",
                           "name": "Инвестировать налоговый вычет в программу("
                         },
                         {
                           "code": "dogovor.godDohodnostInvestSredstv",
                           "name": "Годовая доходность инвестирования средств",
                           "decimalValue": 25,
                           "type": "Вещественный"
                         },
                         {
                           "code": "dogovor.predvRaschet",
                           "name": "Предварительный расчет",
                           "boolValue": true,
                           "type": "Логический"
                         }
                       ]
                     },
                     "product": {
                       "name": "Программа долгосрочных сбережений граждан (ПДС)"
                     }
                   }
                 }
                """;
        given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", responseAuth.getSessionToken())
                .contentType(ContentType.JSON)
                .body(requestBody).
        when()
                .post(ConstantValues.CALC_ENDPOINT).
        then()
                .statusCode(200)
                .body("accID", equalTo(responseAuth.getSessionToken()))
                .body("calcPolicyResult.calcResults[0].policy.calcID", equalTo("123"))
                .log().all();
    }

    @Test
    public void successCalcWithPOJO(){

        Product product = new Product("Программа долгосрочных сбережений граждан (ПДС)");

        List<Parameter> parametsItems = new ArrayList<>();

        parametsItems.add(new Parameter("dogovor.vkladchikPol", "Возраст", "жен.", "Строка"));
        parametsItems.add(new Parameter("dogovor.vkladchikVozrast","Возраст вкладчика", 45,"Целое"));
        parametsItems.add(new Parameter("dogovor.dohodvmes", "Среднемесячный доход", 60000, "Вещественный"));
        parametsItems.add(new Parameter("dogovor.povtornyeVznosy", "Повторные взносы", 4500, "Вещественный"));
        parametsItems.add(new Parameter("dogovor.periodichnostVznosov", "Периодичность повторных взносов", "ежеквартально", "Строка"));
        parametsItems.add(new Parameter("dogovor.srokNakoplenij", "Срок накоплений", 25, "Целое"));
        parametsItems.add(new Parameter("dogovor.srokEzhemesVyplat", "Срок ежемесячных выплат", 10, "Целое"));
        parametsItems.add(new Parameter("dogovor.perevestiPensionNakopleniya", "Сумма кредита", "да", "Строка"));
        parametsItems.add(new Parameter("dogovor.razmerPensionNakoplenij", "Сумма пенсионных накоплений по ОПС", 6500, "Вещественный"));
        parametsItems.add(new Parameter("dogovor.investirovatNalogovyjVychet", "Инвестировать налоговый вычет в программу(", "да", "Строка"));
        parametsItems.add(new Parameter("dogovor.godDohodnostInvestSredstv", "Годовая доходность инвестирования средств", 25, "Вещественный"));
        parametsItems.add(new Parameter("dogovor.predvRaschet", "Предварительный расчет", true, "Логический"));

        Parameters parameters = new Parameters(parametsItems);

        Risk risks = new Risk("true", "Пенсионное накопление", 4000);

        RiskInfo riskInfo = new RiskInfo(Collections.singletonList(risks));

        List<Object> Objects = new ArrayList<>();

        Objects.add(new Object("Объект страхования", null, riskInfo));

        InsuranceObjects insuranceObjects = new InsuranceObjects(Objects);

        PolicyCalc policyCalc = PolicyCalc.builder()
                .currCode("RUR")
                .insCompanyName("ООО СК «Согласие-Вита»")
                .insuranceObjects(insuranceObjects)
                .onlinePayment(false)
                .parameters(parameters)
                .product(product).build();

        CalcRequest calc = new CalcRequest("Рисковое страхование", policyCalc);


        given()
                .baseUri(ConstantValues.BASE_URL)
                .header("sessionToken", responseAuth.getSessionToken())
                .contentType(ContentType.JSON)
                .body(calc)
//                .log().all()
                .when()
                .post(ConstantValues.CALC_ENDPOINT).
                then()
                .statusCode(200)
                .body("accID", equalTo(responseAuth.getSessionToken()))
//                .body("calcPolicyResult.calcResults[0].policy.calcID", notNullValue().toString())
                .log().all();
    }

}

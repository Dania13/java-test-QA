package PDSAPI.actions;

import PDSAPI.models.*;
import PDSAPI.models.Object;
import helpers.InnGenerator;
import helpers.SNILSGenerator;
import org.jetbrains.annotations.NotNull;
import com.github.javafaker.Faker;
import java.util.Locale;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreatePolicy {
    static Faker faker = new Faker(Locale.forLanguageTag("ru"));
    String INN = InnGenerator.getINNFL();
    String SNILS = SNILSGenerator.getSNILS(true);


    List<Parameter> parametsItems = new ArrayList<>();

    Product product;
    Parameters parameters;
    Risk risks;
    RiskInfo riskInfo;
    InsuranceObjects insuranceObjects;
    Insurant createInsurant;

    {
        product = new Product();
        parametsItems.add(new Parameter("dogovor.predvRaschet","Предварительный расчет", false,"Логический"));
        parameters = new Parameters(parametsItems);
        risks = new Risk("true", "Пенсионное накопление", 4000);
        riskInfo = new RiskInfo(Collections.singletonList(risks));

        List<Object> Objects = new ArrayList<>();
        Objects.add(new Object("Объект страхования", null, riskInfo));
        insuranceObjects = new InsuranceObjects(Objects);
        createInsurant = new CreateInsurant().getInsurant();
    }


    public PolicyImport getPolicy() {
        return PolicyImport.builder()
                .currCode("RUR")
                .insCompanyName("ООО СК «Согласие-Вита»")
                .comment("Получили идеальный расчёт")
                .insurant(this.createInsurant)
                .insuranceObjects(this.insuranceObjects)
                .parameters(this.parameters)
                .product(this.product).build();
    }
}

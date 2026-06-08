package helpers;

import PDSAPI.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Утилитарный класс для создания объекта полиса (PolicyImport) для тестирования.
 * <p>
 * Формирует полис по программе долгосрочных сбережений граждан (ПДС) с предварительным расчетом,
 * одним риском "Пенсионное накопление" и автоматически сгенерированным страхователем.
 * </p>
 */
public class CreatePolicy {

    /** Список параметров полиса */
    List<Parameter> parametsItems = new ArrayList<>();

    /** Продукт страхования (ПДС) */
    Product product;

    /** Параметры полиса */
    Parameters parameters;

    /** Риск страхования */
    Risk risks;

    /** Информация о рисках */
    RiskInfo riskInfo;

    /** Объекты страхования */
    InsuranceObjects insuranceObjects;

    /** Созданный страхователь */
    Insurant createInsurant;

    /**
     * Инициализация всех компонентов полиса.
     * <p>Выполняется в блоке инициализации экземпляра.</p>
     */
    {
        product = new Product("Программа долгосрочных сбережений граждан (ПДС)");
        parametsItems.add(new Parameter("dogovor.predvRaschet","Предварительный расчет", false,"Логический"));
        parameters = new Parameters(parametsItems);
        risks = new Risk("true", "Пенсионное накопление", 4000);
        riskInfo = new RiskInfo(Collections.singletonList(risks));

        List<InsuranceObject> Objects = new ArrayList<>();
        Objects.add(new InsuranceObject("Объект страхования", null, riskInfo));
        insuranceObjects = new InsuranceObjects(Objects);
        createInsurant = new CreateInsurant().getInsurant();
    }

    /**
     * Возвращает полностью собранный объект полиса для импорта.
     *
     * @return объект {@link PolicyImport}, содержащий полис с указанными параметрами,
     *         страхователем, объектами страхования и продуктом
     */
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

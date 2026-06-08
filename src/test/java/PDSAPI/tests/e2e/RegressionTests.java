package PDSAPI.tests.e2e;

import PDSAPI.actions.*;
import PDSAPI.models.*;
import PDSAPI.specs.ConstantValues;
import helpers.CreatePolicy;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Класс регрессионных тестов для проверки API методов страхового продукта.
 * <p>Содержит end-to-end тесты, проверяющие полный жизненный цикл страхового полиса:
 * от авторизации и импорта до оформления и аннулирования.</p>
 *
 * <p>Особенности тестов:
 * <ul>
 *   <li>Использование кэширования токена сессии для оптимизации</li>
 *   <li>Автоматическая генерация тестовых данных через {@link CreatePolicy}</li>
 *   <li>Прикрепление всех необходимых документов перед оформлением</li>
 *   <li>Allure отчётность с подробным описанием шагов</li>
 * </ul>
 * </p>
 *
 * <p>Пример запуска:</p>
 * <pre>
 * mvn test -Dtest=RegressionTests
 * mvn test -Dtest=RegressionTests#issue
 * mvn test -Dgroups=regression
 * </pre>
 */
@Epic("Проверка API методов продукта")
@Feature("Регрессионные тесты")
public class RegressionTests {

    /** Типы документов, необходимые для оформления полиса */
    private static final String[] REQUIRED_DOCUMENTS = {
            "Документ, удостоверяющий личность",
            "Анкета для проведения идентификации клиента",
            "Согласие на обработку ПД",
            "Согласие на доп. услугу"
    };

    private String sessionToken;
    private String calcID;
    private String policyID;

    /**
     * Тест оформления страхового полиса.
     * <p>Проверяет полный цикл создания полиса:
     * <ol>
     *   <li>Авторизация в системе</li>
     *   <li>Импорт полиса с расчётом</li>
     *   <li>Прикрепление всех необходимых документов</li>
     *   <li>Оформление полиса</li>
     *   <li>Проверка получения номера полиса</li>
     * </ol>
     * </p>
     *
     * <p><b>Ожидаемый результат:</b> Полис успешно создан и оформлен,
     * получен валидный номер полиса.</p>
     */
    @Test
    @Tag("regression")
    @Description("Оформление полиса")
    public void issue(){
        // Авторизация
        sessionToken = Auth.getCachedSessionToken(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
        // Импорт полиса
        ImportResponse response = Import.importPolicy(sessionToken, new CreatePolicy().getPolicy());
        // Получение calcID из импорта
        calcID = Import.getCalcId(response);
        // Получение policyID из полиса
        policyID = Import.getPolicyId(response);
        // Прикрепление документов
        Attach.AttachDocs(sessionToken, calcID, REQUIRED_DOCUMENTS);
        // Оформление полиса
        Issue.issuePolicy(sessionToken, policyID);
    }

    /**
     * Тест оформления полиса с последующим аннулированием.
     * <p>Проверяет полный цикл создания и отмены полиса:
     * <ol>
     *   <li>Авторизация в системе</li>
     *   <li>Импорт полиса с расчётом</li>
     *   <li>Прикрепление всех необходимых документов</li>
     *   <li>Оформление полиса</li>
     *   <li>Аннулирование полиса</li>
     * </ol>
     * </p>
     *
     * <p><b>Ожидаемый результат:</b> Полис успешно создан, оформлен,
     * а затем аннулирован без ошибок.</p>
     */
    @Test
    @Tag("regression")
    @Description("Оформление полиса с последующим аннулированием")
    public void issueWithAnnulate(){
        // Авторизация
        sessionToken = Auth.getCachedSessionToken(
                ConstantValues.LOGIN_AUTH,
                ConstantValues.PASSWORD_AUTH
        );
        // Импорт полиса
        ImportResponse response = Import.importPolicy(sessionToken, new CreatePolicy().getPolicy());
        // Получение calcID из импорта
        calcID = Import.getCalcId(response);
        // Получение policyID из полиса
        policyID = Import.getPolicyId(response);
        // Прикрепление документов
        Attach.AttachDocs(sessionToken, calcID, REQUIRED_DOCUMENTS);
        // Оформление полиса
        Issue.issuePolicy(sessionToken, policyID);
        // Аннулирование полиса
        Annul.AnnulPolicy(sessionToken, calcID);
    }

}

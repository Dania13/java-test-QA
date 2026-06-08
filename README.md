## 📋 Оглавление
1. [Общее описание](#общее-описание)
2. [Структура проекта](#структура-проекта)
3. [Тесты](#тесты)
4. [Технологии и зависимости](#технологии-и-зависимости)
5. [Установка и настройка](#установка-и-настройка)
6. [Модели данных](#модели-данных)
7. [API действия](#api-действия)
8. [Генераторы тестовых данных](#генераторы-тестовых-данных)
9. [Примеры использования](#примеры-использования)
10. [Запуск тестов](#запуск-тестов)
11. [Отчётность](#отчётность)
12. [Рекомендации по разработке](#рекомендации-по-разработке)

---

## Общее описание

Проект предназначен для автоматизации тестирования API страховой системы, реализующей **Программу долгосрочных сбережений граждан (ПДС)**. Содержит набор моделей данных, вспомогательных генераторов и API-клиентов для выполнения операций:

- Авторизация пользователей
- Расчёт страховых полисов
- Импорт и оформление полисов
- Аннулирование полисов
- Прикрепление документов
- Печать документов

---

## Структура проекта

```
src/main/java/PDSAPI/
├── models/          # Модели данных
│   ├── Address.java
│   ├── Insurant.java
│   ├── Policy.java
│   ├── Physical.java
│   └── ... (более 30 моделей)
└── helpers/         # Вспомогательные классы
    ├── CreateInsurant.java
    ├── CreatePolicy.java
    ├── DateFormatter.java
    ├── InnGenerator.java
    └── SNILSGenerator.java
    
    src/test/java/PDSAPI/
├── actions/          # API действия (клиенты)
│   ├── Auth.java    # Авторизация
│   ├── Import.java  # Импорт полиса
│   ├── Issue.java   # Оформление полиса
│   ├── Annul.java   # Аннулирование
│   ├── Attach.java  # Прикрепление документов
│   └── Print.java   # Печать документов
├── specs/           # Конфигурации
│   └── ConstantValues.java
└── tests/           # Тесты
    ├── e2e/         # e2e тесты
        └──RegressionTersts.java # Регрессионные тесты
    ├──methods/      # Модульные тесты
    ├── AnnulTests.java     # Тесты на метод аннулирования
    ├── AttachTests.java    # Тесты на метод прикладывания документов
    ├── AuthTests.java      # Тесты на метод авторизации
    ├── CalcTests.java      # Тесты на метод расчёта
    ├── DictTests.java      # Тесты на метод получения справочных значений
    ├── ImportTests.java    # Тесты на метод сохранения полиса
    ├── IssueTests.java     # Тесты на метод оформления
    └── PrintTests.java     # Тесты на метод печати полиса
```

---

## Тесты
Тесты находятся в папке tests.
Тесты деляться на два вида: e2e и модульные.
Модульные тесты имеют позитивные и негативные сценарии.
e2e тесты имеют только позитивные сценарии оформления полиса и оформления полиса с дальнейшим аннулированием.

Для тестов e2e в папке action написаны сами методы отправки с проверками и логированием, так что в самих тестах только используются написанные методы.
Для тестов модульных в предустановках в самих тестах используются методы отпрвки для тестирвания разных вариантов.

## Технологии и зависимости

| Технология | Версия  | Назначение |
|------------|---------|------------|
| Java | 11+     | Основной язык |
| Rest Assured | 6.0.0   | HTTP-клиент для API тестирования |
| JUnit 5 | 5.10.2  | Фреймворк для тестирования |
| Lombok | 1.18.46 | Генерация кода (геттеры/сеттеры) |
| Jackson | 2.22.0  | JSON сериализация/десериализация |
| Allure | 2.27.0  | Формирование отчетов |
| JavaFaker | 1.0.2   | Генерация тестовых данных |
| SLF4J | 2.0.13   | Логирование |

---

## Установка и настройка

### Требования
- JDK 11 или выше
- Maven 3.6+
- Доступ к API страховой системы

### Шаги установки

1. **Клонирование репозитория**
```bash
git clone https://github.com/your-company/pds-api-tests.git
cd pds-api-tests
```

2. **Настройка конфигурации**
```java
// PDSAPI/specs/ConstantValues.java
public class ConstantValues {
    public static final String BASE_URL = "your-api-server";
    public static final String AUTH_ENDPOINT = "/api/auth/login";
    public static final String IMPORT_ENDPOINT = "/api/policy/import";
    public static final String ISSUE_ENDPOINT = "/api/policy/issue";
    public static final String ANNUL_ENDPOINT = "/api/policy/annul";
    public static final String ATTACH_ENDPOINT = "/api/document/attach";
    public static final String PRINT_ENDPOINT = "/api/policy/print";
}
```

3. **Сборка проекта**
```bash
mvn clean compile
```

4. **Запуск тестов**
```bash
mvn test
```

---

## Модели данных

### Основные модели

| Класс | Описание | Ключевые поля |
|-------|----------|---------------|
| `Physical` | Физическое лицо | firstName, lastName, birthDate, inn, snils |
| `Insurant` | Страхователь | physical, type ("ФЛ"/"ЮЛ") |
| `Policy` | Страховой полис | number, date, state, insPremTotal |
| `Address` | Адрес | postIndex, addressText, country, region |
| `Document` | Удостоверяющий документ | series, number, dateOfIssue, type |
| `Risk` | Страховой риск | name, insured, insPrem |
| `Payment` | Платеж | number, date, sum |

### Пример создания модели

```java
// Создание адреса
Address address = new Address(123456, "г. Москва, ул. Тверская, д. 1", "Россия", "Москва");

// Создание физического лица
Physical physical = Physical.builder()
    .firstName("Иван")
    .lastName("Петров")
    .middleName("Иванович")
    .birthDate("1990-01-01T12:00:00.000Z")
    .inn("123456789012")
    .snils("123-456-789 01")
    .build();

// Создание страхователя
Insurant insurant = new Insurant(physical, "ФЛ");
```

---

## API действия

### 1. Авторизация

```java
// Базовая авторизация
String sessionToken = Auth.loginUser("username", "password");

// Авторизация с кэшированием (оптимизация)
String token = Auth.getCachedSessionToken("username", "password");

// Проверка неверных учётных данных
String errorMessage = Auth.loginUserInvalid("wrong_user", "wrong_pass");
```

### 2. Импорт полиса

```java
// Создание полиса для импорта
PolicyImport policy = new CreatePolicy().getPolicy();

// Импорт полиса
ImportResponse response = Import.importPolicy(sessionToken, policy);

// Получение идентификаторов
String calcId = Import.getCalcId(response);
String policyId = Import.getPolicyId(response);
String policyNumber = Import.getNumber(response);
```

### 3. Оформление полиса

```java
// Оформление полиса после импорта
Issue.issuePolicy(sessionToken, policyId);

// Оформление с получением ответа
IssueResponse issueResponse = Issue.issuePolicyAndGetResponse(sessionToken, policyId);
```

### 4. Аннулирование полиса

```java
// Аннулирование по идентификатору расчёта
Annul.annulPolicy(sessionToken, calcId);

// Аннулирование с указанием причины
Annul.annulPolicy(sessionToken, calcId, "Техническая ошибка");
```

### 5. Прикрепление документов

```java
// Прикрепление документа, удостоверяющего личность
Attach.attachDocs(sessionToken, calcId, "Документ, удостоверяющий личность");

// Прикрепление с получением ID документа
int docId = Attach.attachDocsAndGetDocId(sessionToken, calcId, "SNILS");
```

### 6. Печать документов

```java
// Печать полиса
String pdfUrl = Print.printPolicy(sessionToken, calcId);
```

---

## Генераторы тестовых данных

### CreateInsurant
```java
CreateInsurant createInsurant = new CreateInsurant();
Insurant insurant = createInsurant.getInsurant();
// Создает страхователя со случайными данными:
// - ФИО (русские имена)
// - ИНН (валидный, 12 цифр)
// - СНИЛС (валидный, с контрольной суммой)
// - Дата рождения (18-120 лет)
// - Адреса (регистрации и фактический)
// - Паспортные данные
```

### CreatePolicy
```java
CreatePolicy createPolicy = new CreatePolicy();
PolicyImport policy = createPolicy.getPolicy();
// Создает полис ПДС с параметрами:
// - Программа долгосрочных сбережений
// - Предварительный расчет
// - Риск "Пенсионное накопление" (4000 руб.)
```

### InnGenerator
```java
// Генерация валидного ИНН (12 цифр)
String inn = InnGenerator.getINNFL();
```

### SNILSGenerator
```java
// Генерация СНИЛС в форматированном виде (XXX-XXX-XXX XX)
String snils = SNILSGenerator.getSNILS(true);

// Генерация в неформатированном виде (11 цифр)
String snilsRaw = SNILSGenerator.getSNILS(false);
```

---

## Примеры использования

### Полный цикл работы с полисом

```java
public class PolicyLifecycleTest {
    
    @Test
    void testFullPolicyLifecycle() {
        // 1. Авторизация
        String sessionToken = Auth.loginUser("test_user", "test_pass");
        
        // 2. Создание тестовых данных
        PolicyImport policy = new CreatePolicy().getPolicy();
        
        // 3. Импорт полиса
        ImportResponse importResponse = Import.importPolicy(sessionToken, policy);
        String calcId = Import.getCalcId(importResponse);
        String policyId = Import.getPolicyId(importResponse);
        
        // 4. Прикрепление документов
        Attach.attachDocs(sessionToken, calcId, "PASSPORT");
        Attach.attachDocs(sessionToken, calcId, "SNILS");
        
        // 5. Оформление полиса
        Issue.issuePolicy(sessionToken, policyId);
        
        // 6. Печать документов
        String pdfUrl = Print.printPolicy(sessionToken, calcId);
        
        // 7. Аннулирование (опционально)
        // Annul.annulPolicy(sessionToken, calcId);
        
        System.out.println("Полис успешно создан и оформлен!");
        System.out.println("Номер полиса: " + Import.getNumber(importResponse));
        System.out.println("Ссылка на PDF: " + pdfUrl);
    }
}
```

### Параметризованный тест

```java
@ParameterizedTest
@CsvSource({
    "PASSPORT, Паспорт РФ",
    "SNILS, СНИЛС",
    "INN, ИНН"
})
void testDocumentAttachment(String docType, String docName) {
    String token = Auth.getCachedSessionToken("user", "pass");
    PolicyImport policy = new CreatePolicy().getPolicy();
    ImportResponse response = Import.importPolicy(token, policy);
    
    assertDoesNotThrow(() -> {
        Attach.attachDocs(token, Import.getCalcId(response), docType);
    });
    
    System.out.println("Документ " + docName + " успешно прикреплен");
}
```

---

## Запуск тестов

### Запуск всех тестов
```bash
mvn clean test
```

### Запуск конкретного тестового класса
```bash
mvn clean test -Dtest=IssueTests
```

### Запуск регрессионных тестов (e2e)
```bash
mvn clean test -Dgroups="regression"
```

---

## Отчётность

### Allure Report

1. **Генерация отчёта**
```bash
mvn allure:report
```

2. **Просмотр отчёта**
```bash
mvn allure:serve
```

3. **Очистка отчётов**
```bash
mvn allure:clean
```

### Логирование

```java
// Настройка логирования в logback.xml
<configuration>
    <logger name="PDSAPI" level="DEBUG"/>
    <logger name="PDSAPI.actions" level="INFO"/>
    <logger name="PDSAPI.helpers" level="DEBUG"/>
</configuration>
```

---

## Рекомендации по разработке

### Добавление новой модели

1. Создать класс в пакете `PDSAPI.models`
2. Использовать Lombok аннотации (`@Getter`, `@Setter`, `@Builder`)
3. Добавить Javadoc документацию
4. Добавить конструкторы для сериализации/десериализации

```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewModel {
    private String field1;
    private int field2;
    
    /**
     * Описание метода
     * @param param описание параметра
     * @return описание返回值
     */
    public String someMethod(String param) {
        return param;
    }
}
```

### Добавление нового API действия

1. Создать класс в пакете `PDSAPI.actions`
2. Добавить приватный конструктор (utility class)
3. Использовать `@Step` для Allure отчётов
4. Добавить валидацию входных параметров
5. Добавить логирование

```java
public final class NewAction {
    private static final Logger log = LoggerFactory.getLogger(NewAction.class);
    
    private NewAction() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    @Step("Выполнение действия с параметром {param}")
    public static ResponseType doSomething(String sessionToken, String param) {
        log.info("Выполнение действия с параметром: {}", param);
        // реализация
    }
}
```

### Code Style

- Использовать Java Code Conventions
- Имена методов в **camelCase**
- Константы в **UPPER_SNAKE_CASE**
- Добавлять Javadoc для всех публичных методов
- Не использовать магические числа/строки

### Best Practices

1. **Всегда проверяйте ответы API**
```java
assertNotNull(response.getAccID());
assertTrue(response.isSuccess());
```

2. **Используйте кэширование токена**
```java
// Плохо
String token = Auth.loginUser("user", "pass"); // каждый раз новая авторизация

// Хорошо
String token = Auth.getCachedSessionToken("user", "pass"); // с кэшированием
```

3. **Логируйте важные события**
```java
log.info("Пользователь {} авторизован", login);
log.debug("Получен ответ: {}", response);
```

4. **Закрывайте ресурсы**
```java
// Используйте try-with-resources для потоков
try (FileInputStream fis = new FileInputStream("file.txt")) {
    // работа с файлом
}
```

---

## Troubleshooting

### Проблема: Connection refused
**Решение:** Проверьте доступность API сервера и настройки `BASE_URL`

### Проблема: sessionToken expired
**Решение:** Используйте `Auth.getCachedSessionToken()` для автоматического обновления

### Проблема: Lombok не работает в IDE
**Решение:** Установите плагин Lombok для вашей IDE

### Проблема: Ошибки валидации ИНН/СНИЛС
**Решение:** Используйте генераторы `InnGenerator` и `SNILSGenerator` для создания валидных номеров

---

## Контакты и поддержка

| Роль | Контакт                    |
|-----|----------------------------|
| QA | d.tihonova@virtusystems.ru |

---

## Версионирование

| Версия | Дата       | Изменения |
|--------|------------|-----------|
| 1.0.0 | 01-06-2026 | Initial release |
| 1.1.0 | 08-06-2026 | Добавлено кэширование токена |

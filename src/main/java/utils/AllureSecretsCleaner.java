package utils;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class AllureSecretsCleaner {

    private static final Pattern[] SECRET_PATTERNS = {
            // Паттерн для JSON формата: {"name":"password","value":"secret"}
            Pattern.compile("\"name\":\"password\",\"value\":\"([^\"]*)\"", Pattern.CASE_INSENSITIVE),
            // Паттерн для HTML экранированных кавычек: &quot;password&quot;: &quot;secret&quot;
            Pattern.compile("&quot;password&quot;\\s*:\\s*&quot;([^&]*?)&quot;", Pattern.DOTALL | Pattern.CASE_INSENSITIVE),

            // Паттерн для формата: "name":"[2] secret, bool" (захватывает secret)
            Pattern.compile("\"name\":\".*?\\[\\d+\\]\\s*([^,]*?)\\s*,", Pattern.CASE_INSENSITIVE),

            // Паттерн для JSON: "password":"secret"
            Pattern.compile("\"password\"\\s*:\\s*\"([^\"]*)\"", Pattern.CASE_INSENSITIVE),
    };

    public static void main(String[] args) throws IOException {
        // Используем "target/allure-results" как значение по умолчанию для локального запуска
        String resultsPath = args.length > 0 ? args[0] : "target/allure-results";
        Path resultsDir = Paths.get(resultsPath);

        System.out.println("Обработка директории: " + resultsDir.toAbsolutePath());

        if (!Files.exists(resultsDir)) {
            System.err.println("Директория allure-results не найдена");
            System.exit(1);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(resultsDir, "*")) {
            for (Path file : stream) {
                cleanFile(file);
            }
        }

        System.out.println("Очистка завершена");
    }

    private static void cleanFile(Path file) throws IOException {
        String content = Files.readString(file);
        String original = content;

        System.out.println("\nОбработка файла: " + file.getFileName());

        for (int i = 0; i < SECRET_PATTERNS.length; i++) {
            Pattern pattern = SECRET_PATTERNS[i];
            Matcher matcher = pattern.matcher(content);

            if (matcher.find()) {
                System.out.println("  Паттерн " + (i+1) + " сработал:");
                System.out.println("    Найдено: " + matcher.group(0));
                System.out.println("    Группа 1: " + matcher.group(1));
            }

            content = matcher.replaceAll(match -> {
                String fullMatch = match.group(0);
                String secret = match.group(1);
                String replacement = fullMatch.replace(secret, "***");
                System.out.println("    Заменяем '" + secret + "' на '***'");
                return replacement;
            });
        }

        if (!content.equals(original)) {
            Files.writeString(file, content);
            System.out.println("  ✅ Файл изменён");
        } else {
            System.out.println("  ⏭️ Файл не требует изменений");
        }
    }
}
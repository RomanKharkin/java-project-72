package hexlet.code.controllers;

import hexlet.code.App;
import hexlet.code.domain.Url;
import io.ebean.DB;
import io.ebean.Database;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RootControllerTest {
    private static Javalin app;
    private static String baseUrl;

    @BeforeAll
    public static void beforeAll() {
        // Получаем инстанс приложения
        app = App.getApp();
        // Запускаем приложение на рандомном порту
        app.start(0);
        // Получаем порт, на которм запустилось приложение
        int port = app.port();
        // Формируем базовый URL
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    public static void afterAll() {
        // Останавливаем приложение
        app.stop();
    }

    @Test
    void getApp() {
        Assertions.assertNotNull(App.getApp());
    }

    // Между тестами база данных очищается
    // Благодаря этому тесты не влияют друг на друга
    @BeforeEach
    void beforeEach() {
        Database db = DB.getDefault();
//        db.truncate("url");
        Url existingUrl = new Url("https://roman.com");
        existingUrl.save();
    }
}

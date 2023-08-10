package hexlet.code.controllers;

import hexlet.code.App;
import hexlet.code.domain.Url;
import hexlet.code.domain.query.QUrl;
import io.ebean.DB;
import io.ebean.Database;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UrlControllerTest {

    @Test
    void getApp() {
        Assertions.assertNotNull(App.getApp());
    }

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

    // Между тестами база данных очищается
    // Благодаря этому тесты не влияют друг на друга
    @BeforeEach
    void beforeEach() {
        Database db = DB.getDefault();
        db.truncate("url");
        Url existingUrl = new Url("https://roman.com");
        existingUrl.save();
    }

    @Test
    void testUsers() {
        HttpResponse<String> response = Unirest
                .get(baseUrl + "/urls")
                .asString();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testShow() {
        HttpResponse<String> responsePost = Unirest
                .post(baseUrl + "/")
                .field("url", "https://bazzara.it")
                .asString();
        assertThat(responsePost.getStatus()).isEqualTo(302);
        Url actualUrl = new QUrl()
                .name.equalTo("https://bazzara.it")
                .findOne();
        assertThat(actualUrl).isNotNull();
        assertThat(actualUrl.getName()).isEqualTo("https://bazzara.it");

        String id = String.valueOf(actualUrl.getId());

        HttpResponse<String> response = Unirest
                .get(baseUrl + "/urls/" + id)
                .asString();
        assertThat(response.getStatus()).isEqualTo(200);
    }
}

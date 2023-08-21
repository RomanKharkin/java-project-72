package hexlet.code.controllers;

import hexlet.code.App;
import hexlet.code.domain.Url;
import hexlet.code.domain.UrlCheck;
import hexlet.code.domain.query.QUrl;
import hexlet.code.domain.query.QUrlCheck;
import io.ebean.DB;
import io.ebean.Database;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class UrlControllerTest {

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

    @Test
    void testUrls() {
        HttpResponse<String> response = Unirest.get(baseUrl + "/urls").asString();
        String content = response.getBody();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).contains("https://roman.com");
    }

    @Test
    void testShowUrl() {
        HttpResponse<String> responsePost = Unirest.post(baseUrl + "/").field("url", "https://bazzara.com").asString();
        assertThat(responsePost.getStatus()).isEqualTo(302);
        Url actualUrl = new QUrl().name.equalTo("https://bazzara.com").findOne();
        assertThat(actualUrl).isNotNull();
        assertThat(actualUrl.getName()).isEqualTo("https://bazzara.com");

        String id = String.valueOf(actualUrl.getId());

        HttpResponse<String> response = Unirest.get(baseUrl + "/urls/" + id).asString();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void checkUrl() throws Exception {
        MockWebServer server = new MockWebServer();

        File file = new File(
                getClass().getClassLoader().getResource("good.html").getFile()
        );

        String content = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8).stream()
                .collect(Collectors.joining());

        server.enqueue(new MockResponse().setBody(content));

        server.start();

        HttpUrl goodUrl = server.url("");        // Запускаем сервер.

        Url url = new Url(goodUrl.toString());
        url.save();

        List<UrlCheck> urlChecks = new QUrlCheck().url.equalTo(url).findList();
        assertThat(urlChecks.isEmpty()).isTrue();
        HttpResponse<String> responseCreateCheck = Unirest.post(baseUrl + "/urls/" + url.getId() + "/checks")
                .asString();
        assertThat(responseCreateCheck.getStatus()).isEqualTo(HttpStatus.FOUND);
        urlChecks = new QUrlCheck().url.equalTo(url).findList();
        assertThat(urlChecks.isEmpty()).isFalse();
        UrlCheck urlCheck = new QUrlCheck().url.equalTo(url).findOne();
        assertThat(urlCheck.getDescription()).isEqualTo("description");
        assertThat(urlCheck.getH1()).isEqualTo("Header1");
        assertThat(urlCheck.getTitle()).isEqualTo("title");
        // Выключаем сервер. Экземпляры нельзя использовать повторно.
        server.shutdown();
    }
}

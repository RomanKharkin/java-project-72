package hexlet.code.controllers;

import hexlet.code.domain.Url;
import hexlet.code.domain.UrlCheck;
import hexlet.code.domain.query.QUrl;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;


public final class UrlController {
    public static final String NEW_URL_FORM_PARAM = "url";
    public static Handler listUrls = ctx -> {
        List<Url> urls = new QUrl().orderBy().id.asc().findList();
        ctx.attribute("urls", urls);
        ctx.render("urls.html");
    };

    public static Handler showUrl = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Url url = new QUrl().id.equalTo(id).findOne();
        ctx.attribute("url", url);
        ctx.render("show.html");
    };

    public static Handler createUrl = ctx -> {
        String newUrl = ctx.formParam(NEW_URL_FORM_PARAM);
        URL javaNetUrl;
        try {
            javaNetUrl = new URL(newUrl);
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("error", "Некорректный URL");
            ctx.attribute(NEW_URL_FORM_PARAM, newUrl);
            ctx.status(HttpStatus.UNPROCESSABLE_CONTENT);
            ctx.render("index.html");
            return;
        }

        String urlToSave = new URL(javaNetUrl.getProtocol(), javaNetUrl.getHost(), javaNetUrl.getPort(), "")
                .toString();

        if (new QUrl().name.equalTo(urlToSave).exists()) {
            ctx.sessionAttribute("error", "Страница уже существует");
            ctx.attribute(NEW_URL_FORM_PARAM, newUrl);
            ctx.render("index.html");
            return;
        }

        Url url = new Url(urlToSave);
        url.save();
        ctx.sessionAttribute("success", "Ссылка успешно добавлена");
        ctx.redirect("/urls");
    };


    public static Handler checkUrl = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Url url = new QUrl().id.equalTo(id).findOne();
        HttpResponse<String> responseUrl;

        try {
            responseUrl = Unirest.get(url.getName()).asString();
            int statusCode = responseUrl.getStatus();
            Document doc = Jsoup.parse(responseUrl.getBody());
            String title = doc.title();
            String h1 = Optional.ofNullable(doc.select("h1").first()).orElse(new Element("h1")).text();
            String description = Optional.ofNullable(doc.select("meta[name='description']").first())
                    .orElse(new Element("meta")).attr("content");

            UrlCheck newUrlCheck = new UrlCheck(statusCode, title, h1, description, url);
            newUrlCheck.save();

        } catch (Exception e) {
            ctx.sessionAttribute("error", "Некорректная страница");
        }

        ctx.redirect("/urls/" + url.getId());
    };
}

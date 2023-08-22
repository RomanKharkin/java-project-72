package hexlet.code.controllers;

import hexlet.code.domain.Url;
import hexlet.code.domain.UrlCheck;
import hexlet.code.domain.query.QUrl;
import io.javalin.http.Handler;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Optional;


public final class UrlController {
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

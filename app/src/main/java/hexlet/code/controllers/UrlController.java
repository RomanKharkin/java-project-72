package hexlet.code.controllers;

import hexlet.code.domain.Url;
import hexlet.code.domain.query.QUrl;
import io.javalin.http.Handler;

import java.util.List;


public final class UrlController {
    public static Handler listUrls = ctx -> {
        List<Url> urls = new QUrl()
                .orderBy()
                .id.asc()
                .findList();
        ctx.attribute("urls", urls);
        ctx.render("urls.html");

    };

    public static Handler showUrl = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        System.out.println(id);
        Url url = new QUrl()
                .id.equalTo(id)
                .findOne();
        System.out.println(url);
        ctx.attribute("url", url);
        ctx.render("show.html");
    };
}

package hexlet.code.controllers;

import hexlet.code.domain.Url;
import hexlet.code.domain.query.QUrl;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;

import java.net.MalformedURLException;
import java.net.URL;

public final class RootController {
    public static final String NEW_URL_FORM_PARAM = "url";

    public static Handler newUrl = ctx -> {
        ctx.attribute(NEW_URL_FORM_PARAM, "");
        ctx.render("index.html");
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
}

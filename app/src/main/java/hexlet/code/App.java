package hexlet.code;

import io.javalin.Javalin;

public class App {

    public static Javalin getApp() {
        Javalin app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        addRoutes(app);
        System.out.println();

        return app;
    }

    public static void main(String[] args) {
        Javalin app = getApp();
        app.start();
    }

    private static void addRoutes(Javalin app) {
        app.get("/", ctx -> ctx.result("Hello World"));
    }

}

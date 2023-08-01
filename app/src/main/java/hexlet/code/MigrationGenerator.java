package hexlet.code;

import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;

import java.io.IOException;

public final class MigrationGenerator {

    public static void main(String[] args) throws IOException {
        System.out.println("MIGRATION");
        // Создаём миграцию
        DbMigration dbMigration = DbMigration.create();

        dbMigration.addPlatform(Platform.POSTGRES, "postgres");
        // Указываем платформу, в нашем случае H2
        dbMigration.addPlatform(Platform.H2, "h2");

        // Генерируем миграцию
        dbMigration.generateMigration();
    }
}

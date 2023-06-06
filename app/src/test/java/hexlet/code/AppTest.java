package hexlet.code;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void getApp() {
        Assertions.assertNotNull(App.getApp());
    }
}

package main;

import java.io.InputStream;

public interface StaticResources {

    public static InputStream get(String path) {
        return StaticResources.class.getResourceAsStream("../static/" + path);
    }
}

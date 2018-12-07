package main;

import java.io.InputStream;

/** Utility class for dealing with the project's static resources. */
public interface StaticResources {

    /**
     * Utility method for getting static resources from the resources/static project directory.
     *
     * @param path relative path of the file inside the static directory.
     * @return the resource as an InputStream.
     */
    static InputStream get(final String path) {
        return StaticResources.class.getResourceAsStream("../static/" + path);
    }
}

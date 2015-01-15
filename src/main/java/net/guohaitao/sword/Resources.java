package net.guohaitao.sword;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.*;

/**
 * Created by i@guohaitao.net on 15-1-7.
 * Description: 资源工具类
 */
public final class Resources {


    /**
     * Pseudo URL prefix for loading from the class path: "classpath:"
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * URL protocol for a file in the file system: "file"
     */
    private static final String URL_PROTOCOL_FILE = "file";

    private Resources() {
    }

    /**
     * 给定资源地址转换为File对象
     *
     * @param resourceLocation
     *         the resource location to resolve: either a
     *         "classpath:" pseudo URL, a "file:" URL, or a plain file path
     * @return a corresponding File object
     * @throws FileNotFoundException
     *         if the resource cannot be resolved to
     *         a file in the file system
     */
    public static File getFile(String resourceLocation) {
        Preconditions.checkNotNull(resourceLocation, "Resource location must not be null");
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = Classes.getDefaultClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                Throwables.propagate(new FileNotFoundException("Cannot be resolved to absolute file path because it does not reside in the file system"));
            }
            return getFile(url);
        }
        try {
            // try URL
            return getFile(new URL(resourceLocation));
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            return new File(resourceLocation);
        }
    }


    /**
     * 给定URL地址转换为File对象
     *
     * @param resourceUrl
     *         the resource URL to resolve
     * @return a corresponding File object
     * @throws FileNotFoundException
     *         if the URL cannot be resolved to
     *         a file in the file system
     */
    public static File getFile(URL resourceUrl) {
        Preconditions.checkNotNull(resourceUrl, "Resource URL must not be null");
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            Throwables.propagate(new FileNotFoundException("Cannot be resolved to absolute file path because it does not reside in the file system: " + resourceUrl));
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        } catch (URISyntaxException ex) {
            // Fallback for URLs that are not valid URIs (should hardly ever happen).
            return new File(resourceUrl.getFile());
        }
    }


    /**
     * Create a URI instance for the given URL,
     * replacing spaces with "%20" URI encoding first.
     * <p>Furthermore, this method works on JDK 1.4 as well,
     * in contrast to the {@code URL.toURI()} method.
     *
     * @param url
     *         the URL to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException
     *         if the URL wasn't a valid URI
     * @see java.net.URL#toURI()
     */
    private static URI toURI(URL url) throws URISyntaxException {
        return new URI(Strings.replace(url.toString(), " ", "%20"));
    }

}

package ca.jdelreyes.moviereservationsystem.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class CookieUtil {
    private CookieUtil() {
    }

    public static String getCookie(String cookieName, HttpServletRequest request) {
        String cookieHeader = request.getHeader(HttpHeaders.COOKIE);

        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");

            for (String cookie : cookies) {
                String[] cookiePair = cookie.split("=", 2);

                if (cookiePair.length == 2) {
                    String name = cookiePair[0].trim();
                    String value = cookiePair[1].trim();

                    if (cookieName.equals(name)) {
                        return name + "=" + value;
                    }
                }
            }
        }

        return null;
    }
}

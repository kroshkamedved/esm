package com.epam.esm.util;

public class LinkUtil {
    public static String createLinkHeadre(String uri, String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }
}

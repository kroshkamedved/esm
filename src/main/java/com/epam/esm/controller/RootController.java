package com.epam.esm.controller;

import com.epam.esm.util.LinkUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

@RestController
@RequestMapping("/")
public class RootController {
    @GetMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getAppLinks(HttpServletResponse response, HttpServletRequest request ){
        String rootUrl = request.getRequestURL().toString();

        URI tagsUri = new UriTemplate("{rootUrl}{resource}").expand(rootUrl,"tags");
        URI certificatesUri = new UriTemplate("{rootUrl}{resource}").expand(rootUrl,"certificates");

        response.addHeader(HttpHeaders.LINK, LinkUtil.createLinkHeader(tagsUri.toASCIIString(),"collection"));
        response.addHeader(HttpHeaders.LINK, LinkUtil.createLinkHeader(certificatesUri.toASCIIString(),"collection"));
    }
}

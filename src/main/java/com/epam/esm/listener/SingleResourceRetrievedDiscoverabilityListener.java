package com.epam.esm.listener;

import com.epam.esm.util.LinkUtil;
import com.epam.esm.event.SingleResourceRetrieved;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class SingleResourceRetrievedDiscoverabilityListener implements ApplicationListener<SingleResourceRetrieved> {
    @Autowired
    private final HttpServletRequest request;

    @Override
    public void onApplicationEvent(SingleResourceRetrieved event) {
        addLinkHeaderOnSingleResourceRetrieved(event.getResponse(), request);
    }

    private void addLinkHeaderOnSingleResourceRetrieved(HttpServletResponse response, HttpServletRequest request) {
        String requestUri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri().toASCIIString();
        int positionOfLastSlash = requestUri.lastIndexOf("/");

        String uriForResourceCreation = requestUri.substring(0, positionOfLastSlash);
        String linkHeadreValue = LinkUtil.createLinkHeadre(uriForResourceCreation, "collection");

        response.addHeader(HttpHeaders.LINK, linkHeadreValue);
    }
}

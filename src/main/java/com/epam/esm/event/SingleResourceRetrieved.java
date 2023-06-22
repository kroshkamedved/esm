package com.epam.esm.event;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class SingleResourceRetrieved extends ApplicationEvent {
    @Getter
    private HttpServletResponse response;

    public SingleResourceRetrieved(HttpServletResponse response, Object source) {
        super(source);
        this.response = response;
    }
}

package com.epam.esm;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TagRepository tagRepository;

    @Test
    @DisplayName("POST returns HTTP status Bad Request when fields are missing")
    void createTag_ShouldReturnBadRequest_whenPassingNullFields() throws Exception { //TODO Good! Now create more tests! :)
        Tag tag = Tag.builder().name(null).build();

        mockMvc.perform(
                        post("/tags")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isBadRequest());
    }

}

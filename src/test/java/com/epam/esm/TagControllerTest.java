package com.epam.esm;

import com.epam.esm.repository.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {com.epam.esm.config.security.WebSecurityBeansConfig.class, com.epam.esm.config.security.SecurityConfig.class})
public class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TagRepository tagRepository;

  /*  @Test
    @DisplayName("POST returns HTTP status Bad Request when fields are missing")
    void createTag_ShouldReturnBadRequest_whenPassingNullFields() throws Exception {
        Tag tag = Tag.builder().name(null).build();

        mockMvc.perform(
                        post("/tags")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tag)))
                .andExpect(status().isUnauthorized());
    }*/
}

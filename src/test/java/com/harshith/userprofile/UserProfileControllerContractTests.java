package com.harshith.userprofile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserProfileControllerContractTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void profileEndpointReturnsNotImplementedUntilBusinessLogicIsAdded() throws Exception {
        mockMvc.perform(get("/api/v1/profiles/user-123"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.title").value("Business logic not implemented"));
    }
}

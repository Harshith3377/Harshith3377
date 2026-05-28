package com.harshith.userprofile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserProfileControllerContractTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsGetsUpdatesAndDeletesProfile() throws Exception {
        String userId = "controller-user-1";

        mockMvc.perform(post("/api/v1/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "controller-user-1",
                                  "attributes": {
                                    "theme": "dark"
                                  }
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.attributes.theme").value("dark"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());

        mockMvc.perform(get("/api/v1/profiles/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attributes.theme").value("dark"));

        mockMvc.perform(put("/api/v1/profiles/{userId}/attributes/{key}", userId, "language")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": "en"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attributes.theme").value("dark"))
                .andExpect(jsonPath("$.attributes.language").value("en"));

        mockMvc.perform(delete("/api/v1/profiles/{userId}", userId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/profiles/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("User profile not found"));
    }

    @Test
    void duplicateUserIdsReturnConflict() throws Exception {
        String body = """
                {
                  "userId": "controller-user-duplicate",
                  "attributes": {}
                }
                """;

        mockMvc.perform(post("/api/v1/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Duplicate user profile"));
    }

    @Test
    void invalidRequestsReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "",
                                  "attributes": {}
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid request"));
    }
}

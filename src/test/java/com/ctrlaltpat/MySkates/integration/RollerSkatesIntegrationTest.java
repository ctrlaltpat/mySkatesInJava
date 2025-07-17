package com.ctrlaltpat.MySkates.integration;

import com.ctrlaltpat.MySkates.model.RollerSkatesPair;
import com.ctrlaltpat.MySkates.repository.RollerSkatesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RollerSkatesIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private RollerSkatesRepository repository;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                repository.deleteAll();
        }

        @Test
        void createRollerSkates_ShouldPersistToDatabase() throws Exception {
                RollerSkatesPair newPair = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira",
                                "RollerBones Teams (white)");

                mockMvc.perform(post("/api/v1/skates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newPair)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.name").value("OG 172"))
                                .andExpect(jsonPath("$.brand").value("Riedell"))
                                .andExpect(jsonPath("$.plates").value("Chaya Ophira"))
                                .andExpect(jsonPath("$.wheels").value("RollerBones Teams (white)"));

                assertThat(repository.count()).isEqualTo(1);
        }

        @Test
        void getAllRollerSkates_ShouldReturnAllFromDatabase() throws Exception {
                RollerSkatesPair pair1 = repository.save(
                                new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)"));
                RollerSkatesPair pair2 = repository
                                .save(new RollerSkatesPair("3200", "Riedell", "Arius", "Varsity Plus (black)"));

                mockMvc.perform(get("/api/v1/skates"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].id").value(pair1.getId()))
                                .andExpect(jsonPath("$[0].name").value("OG 172"))
                                .andExpect(jsonPath("$[1].id").value(pair2.getId()))
                                .andExpect(jsonPath("$[1].name").value("3200"));
        }

        @Test
        void getRollerSkatesById_ShouldReturnFromDatabase() throws Exception {
                RollerSkatesPair savedPair = repository.save(
                                new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)"));

                mockMvc.perform(get("/api/v1/skates/" + savedPair.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(savedPair.getId()))
                                .andExpect(jsonPath("$.name").value("OG 172"))
                                .andExpect(jsonPath("$.brand").value("Riedell"))
                                .andExpect(jsonPath("$.plates").value("Chaya Ophira"))
                                .andExpect(jsonPath("$.wheels").value("RollerBones Teams (white)"));
        }

        @Test
        void getRollerSkatesById_WhenNotFound_ShouldReturn404() throws Exception {
                mockMvc.perform(get("/api/v1/skates/999"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void updateRollerSkates_ShouldUpdateInDatabase() throws Exception {
                RollerSkatesPair savedPair = repository.save(
                                new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)"));
                RollerSkatesPair updateData = new RollerSkatesPair("Updated Name", "Updated Brand", "Updated Plates",
                                "Updated Wheels");

                mockMvc.perform(put("/api/v1/skates/" + savedPair.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(savedPair.getId()))
                                .andExpect(jsonPath("$.name").value("Updated Name"))
                                .andExpect(jsonPath("$.brand").value("Updated Brand"))
                                .andExpect(jsonPath("$.plates").value("Updated Plates"))
                                .andExpect(jsonPath("$.wheels").value("Updated Wheels"));

                RollerSkatesPair updatedPair = repository.findById(savedPair.getId()).orElseThrow();
                assertThat(updatedPair.getName()).isEqualTo("Updated Name");
                assertThat(updatedPair.getBrand()).isEqualTo("Updated Brand");
                assertThat(updatedPair.getPlates()).isEqualTo("Updated Plates");
                assertThat(updatedPair.getWheels()).isEqualTo("Updated Wheels");
        }

        @Test
        void updateRollerSkates_WhenNotFound_ShouldReturn404() throws Exception {
                RollerSkatesPair updateData = new RollerSkatesPair("Updated", "Updated", "Updated", "Updated");

                mockMvc.perform(put("/api/v1/skates/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deleteRollerSkates_ShouldRemoveFromDatabase() throws Exception {
                RollerSkatesPair savedPair = repository.save(
                                new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)"));
                Long savedId = savedPair.getId();

                assertThat(repository.existsById(savedId)).isTrue();

                mockMvc.perform(delete("/api/v1/skates/" + savedId))
                                .andExpect(status().isNoContent());

                assertThat(repository.existsById(savedId)).isFalse();
                assertThat(repository.count()).isEqualTo(0);
        }

        @Test
        void deleteRollerSkates_WhenNotFound_ShouldReturn404() throws Exception {
                mockMvc.perform(delete("/api/v1/skates/999"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void fullCrudWorkflow_ShouldWorkEndToEnd() throws Exception {
                RollerSkatesPair newPair = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira",
                                "RollerBones Teams (white)");

                String createResponse = mockMvc.perform(post("/api/v1/skates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newPair)))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

                RollerSkatesPair createdPair = objectMapper.readValue(createResponse, RollerSkatesPair.class);
                Long createdId = createdPair.getId();

                mockMvc.perform(get("/api/v1/skates/" + createdId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("OG 172"));

                RollerSkatesPair updateData = new RollerSkatesPair("Updated Name", "Updated Brand", "Updated Plates",
                                "Updated Wheels");
                mockMvc.perform(put("/api/v1/skates/" + createdId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateData)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Updated Name"));

                mockMvc.perform(get("/api/v1/skates/" + createdId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Updated Name"));

                mockMvc.perform(delete("/api/v1/skates/" + createdId))
                                .andExpect(status().isNoContent());

                mockMvc.perform(get("/api/v1/skates/" + createdId))
                                .andExpect(status().isNotFound());
        }

        @Test
        void createMultipleSkates_ShouldAllPersist() throws Exception {
                RollerSkatesPair pair1 = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira",
                                "RollerBones Teams (white)");
                RollerSkatesPair pair2 = new RollerSkatesPair("3200", "Riedell", "Arius", "Varsity Plus (black)");
                RollerSkatesPair pair3 = new RollerSkatesPair("220", "Riedell", "Reactor Neo", "Bones Reds");

                mockMvc.perform(post("/api/v1/skates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pair1)))
                                .andExpect(status().isOk());

                mockMvc.perform(post("/api/v1/skates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pair2)))
                                .andExpect(status().isOk());

                mockMvc.perform(post("/api/v1/skates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pair3)))
                                .andExpect(status().isOk());

                mockMvc.perform(get("/api/v1/skates"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(3));

                assertThat(repository.count()).isEqualTo(3);
        }
}
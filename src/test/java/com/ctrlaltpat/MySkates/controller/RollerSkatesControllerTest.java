package com.ctrlaltpat.MySkates.controller;

import com.ctrlaltpat.MySkates.model.RollerSkatesPair;
import com.ctrlaltpat.MySkates.service.RollerSkatesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ctrlaltpat.MySkates.exception.ResourceNotFoundException;

@WebMvcTest(RollerSkatesController.class)
class RollerSkatesControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private RollerSkatesService rollerSkatesService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void getAllRollerSkates_ShouldReturnListOfSkates() throws Exception {
                RollerSkatesPair pair1 = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira",
                                "RollerBones Teams (white)");
                pair1.setId(1L);
                RollerSkatesPair pair2 = new RollerSkatesPair("3200", "Riedell", "Arius", "Varsity Plus (black)");
                pair2.setId(2L);
                List<RollerSkatesPair> pairs = Arrays.asList(pair1, pair2);

                when(rollerSkatesService.getAllRollerSkates()).thenReturn(pairs);

                mockMvc.perform(get("/api/v1/skates"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[0].name").value("OG 172"))
                                .andExpect(jsonPath("$[0].brand").value("Riedell"))
                                .andExpect(jsonPath("$[1].id").value(2))
                                .andExpect(jsonPath("$[1].name").value("3200"));
        }

        @Test
        void getRollerSkatesById_WhenSkateExists_ShouldReturnSkate() throws Exception {
                RollerSkatesPair pair = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira",
                                "RollerBones Teams (white)");
                pair.setId(1L);

                when(rollerSkatesService.getRollerSkatesById(1L)).thenReturn(Optional.of(pair));

                mockMvc.perform(get("/api/v1/skates/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("OG 172"))
                                .andExpect(jsonPath("$.brand").value("Riedell"))
                                .andExpect(jsonPath("$.plates").value("Chaya Ophira"))
                                .andExpect(jsonPath("$.wheels").value("RollerBones Teams (white)"));
        }

        @Test
        void getRollerSkatesById_WhenSkateDoesNotExist_ShouldReturnNotFound() throws Exception {
                when(rollerSkatesService.getRollerSkatesById(172L)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/v1/skates/172"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void createRollerSkates_ShouldReturnCreatedSkate() throws Exception {
                RollerSkatesPair inputPair = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira",
                                "RollerBones Teams (white)");
                RollerSkatesPair savedPair = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira",
                                "RollerBones Teams (white)");
                savedPair.setId(1L);

                when(rollerSkatesService.createRollerSkates(any(RollerSkatesPair.class))).thenReturn(savedPair);

                mockMvc.perform(post("/api/v1/skates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputPair)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("OG 172"))
                                .andExpect(jsonPath("$.brand").value("Riedell"))
                                .andExpect(jsonPath("$.plates").value("Chaya Ophira"))
                                .andExpect(jsonPath("$.wheels").value("RollerBones Teams (white)"));
        }

        @Test
        void updateRollerSkates_ShouldReturnUpdatedSkate() throws Exception {
                RollerSkatesPair inputPair = new RollerSkatesPair("Updated Name", "Updated Brand", "Updated Plates",
                                "Updated Wheels");
                RollerSkatesPair updatedPair = new RollerSkatesPair("Updated Name", "Updated Brand", "Updated Plates",
                                "Updated Wheels");
                updatedPair.setId(1L);

                when(rollerSkatesService.updateRollerSkates(eq(1L), any(RollerSkatesPair.class)))
                                .thenReturn(updatedPair);

                mockMvc.perform(put("/api/v1/skates/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputPair)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("Updated Name"))
                                .andExpect(jsonPath("$.brand").value("Updated Brand"))
                                .andExpect(jsonPath("$.plates").value("Updated Plates"))
                                .andExpect(jsonPath("$.wheels").value("Updated Wheels"));
        }

        @Test
        void deleteRollerSkates_ShouldReturnNoContent() throws Exception {
                doNothing().when(rollerSkatesService).deleteRollerSkates(1L);

                mockMvc.perform(delete("/api/v1/skates/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void createRollerSkates_WithInvalidJson_ShouldReturnBadRequest() throws Exception {
                String invalidJson = "{\"name\":\"Test\",\"brand\":}";

                mockMvc.perform(post("/api/v1/skates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void createRollerSkates_WithEmptyBody_ShouldReturnBadRequest() throws Exception {
                mockMvc.perform(post("/api/v1/skates")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(""))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void updateRollerSkates_WhenServiceThrowsException_ShouldReturnNotFound() throws Exception {
                RollerSkatesPair inputPair = new RollerSkatesPair("Test", "Test", "Test", "Test");

                when(rollerSkatesService.updateRollerSkates(eq(999L), any(RollerSkatesPair.class)))
                                .thenThrow(new ResourceNotFoundException("Roller Skates not found"));

                mockMvc.perform(put("/api/v1/skates/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputPair)))
                                .andExpect(status().isNotFound());
        }

        @Test
        void deleteRollerSkates_WhenServiceThrowsException_ShouldReturnNotFound() throws Exception {
                doThrow(new ResourceNotFoundException("Roller Skates not found"))
                                .when(rollerSkatesService).deleteRollerSkates(999L);

                mockMvc.perform(delete("/api/v1/skates/999"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void getRollerSkatesById_WithInvalidId_ShouldReturnBadRequest() throws Exception {
                mockMvc.perform(get("/api/v1/skates/invalid"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void getAllRollerSkates_WhenNoSkates_ShouldReturnEmptyArray() throws Exception {
                when(rollerSkatesService.getAllRollerSkates()).thenReturn(Arrays.asList());

                mockMvc.perform(get("/api/v1/skates"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        void createRollerSkates_WithoutContentType_ShouldReturnUnsupportedMediaType() throws Exception {
                RollerSkatesPair inputPair = new RollerSkatesPair("Test", "Test", "Test", "Test");

                mockMvc.perform(post("/api/v1/skates")
                                .content(objectMapper.writeValueAsString(inputPair)))
                                .andExpect(status().isUnsupportedMediaType());
        }
}
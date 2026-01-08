/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web.controller;

import nl.healthri.pubmet.core.domain.Index;
import nl.healthri.pubmet.core.domain.IndexType;
import nl.healthri.pubmet.core.services.IndexService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IndexControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private IndexService indexService;
    @InjectMocks
    private IndexController indexController;

    public Index createSampleIndex() throws URISyntaxException, MalformedURLException {
        return new Index(
                UUID.randomUUID(),
                new URI("http://healthri.nl/").toURL(),
                "Health-RI",
                IndexType.PUSH
        );
    }

    @Test
    public void GivenValidIndex_WhenCreatingIndex_ReturnCreatedStatus() throws Exception {
        // Arrange
        var index = createSampleIndex();
        var jsonBody = objectMapper.writeValueAsString(index);

        Mockito.when(indexService.create(ArgumentMatchers.any(Index.class)))
                .thenReturn(index);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated());
    }

    @Test
    public void GivenInvalidIndex_WhenCreatingIndex_ReturnBadRequestStatus() throws Exception {
        // Arrange
        var jsonBody = objectMapper.writeValueAsString(null);

        Mockito.when(indexService.create(ArgumentMatchers.any(Index.class)))
                .thenReturn(null);

        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.post("/index")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void GivenExistingIndex_WhenFindingIndexById_ReturnOkWithIndex(){

    }

    @Test
    public void GivenNonExistentIndex_WhenFindingIndexById_ReturnNotFoundResponseStatus(){

    }
}
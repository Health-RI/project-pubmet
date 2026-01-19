/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web.controller;

import nl.healthri.pubmet.core.TestConstants;
import nl.healthri.pubmet.core.api.MetadataManager;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.StringReader;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MetadataManagerControllerTest {
    @MockitoBean
    MetadataManager provider;

    @Test
    public void GivenExistingId_WhenGetMetadataById_ReturnModel(@Autowired MockMvc mvc) throws Exception {
        // Arrange
        var expectedModel = TestConstants.TEST_MODEL;
        var id = UUID.randomUUID();

        BDDMockito.given(provider.getMetadata(id))
                .willReturn(Optional.of(expectedModel));

        // Assert
        mvc.perform(get("/metadata/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/turtle"))
                .andExpect(result -> {
                    var content = result.getResponse().getContentAsString();

                    var actual = Rio.parse(
                            new StringReader(content),
                            "",
                            RDFFormat.TURTLE
                    );

                    Assertions.assertEquals(expectedModel, actual);
                });
    }

    @Test
    public void GivenModel_WhenUploadMetadata_ReturnStatusCreated(@Autowired MockMvc mvc) throws Exception {
        // Arrange
        var contentType = "text/turtle";
        var modelContent = TestConstants.TEST_TURTLE;
        var origin = "https://www.health-ri.nl/";

        // Act & Assert
        var request = post("/metadata")
                .content(modelContent)
                .contentType(contentType)
                .header(HttpHeaders.ORIGIN, origin);

        mvc.perform(request)
                .andExpect(status().isCreated());
    }

}

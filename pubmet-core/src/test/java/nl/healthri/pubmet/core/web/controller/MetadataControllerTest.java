/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web.controller;

import nl.healthri.pubmet.core.TestConstants;
import nl.healthri.pubmet.core.api.MetadataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MimeTypeUtils;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MetadataControllerTest {
    @MockitoBean
    MetadataProvider provider;

    @Test
     void testMetadataRetrieval(@Autowired MockMvc mvc) throws Exception {
        // Arrange
        var expected = TestConstants.TEST_TURTLE;

        // Act
        UUID id = UUID.randomUUID();
        BDDMockito.given(provider.getMetadataById(id)).
                willReturn(Optional.ofNullable(TestConstants.TEST_MODEL));

        // Assert
        mvc.perform(get("/metadata/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/turtle"))
                .andExpect(result -> {
                    var actual = result.getResponse().getContentAsString().trim();
                    Assertions.assertEquals(actual, expected);
                });
    }

    @Test
    void GivenNewMetadata_WhenUploadMetadata_ReturnStatusCreated(@Autowired MockMvc mvc) throws Exception {
        // Arrange
        var contentType = MimeTypeUtils.APPLICATION_JSON.toString();
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

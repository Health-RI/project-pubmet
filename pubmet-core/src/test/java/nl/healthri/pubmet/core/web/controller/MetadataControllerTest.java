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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class MetadataControllerTest {
    @MockitoBean
    MetadataProvider provider;

    @Test
    public void testMetadataRetrieval(@Autowired MockMvc mvc) throws Exception {
        // Arrange
        var expected = TestConstants.TEST_TURTLE;

        // Act
        UUID id = UUID.randomUUID();
        BDDMockito.given(provider.getMetadata(id)).
                willReturn(Optional.ofNullable(TestConstants.TEST_MODEL));

        // Assert
        mvc.perform(get("/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/turtle"))
                .andExpect(result -> {
                    var actual = result.getResponse().getContentAsString().trim();
                    Assertions.assertEquals(actual, expected);
                });
    }
}

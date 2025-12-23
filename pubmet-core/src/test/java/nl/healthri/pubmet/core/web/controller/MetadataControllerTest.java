/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web.controller;

import nl.healthri.pubmet.core.TestConstants;
import nl.healthri.pubmet.core.api.MetadataProvider;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

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
        BDDMockito.given(provider.getMetadata("hello")).willReturn(Optional.of(TestConstants.TEST_MODEL));

        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/turtle"))
                .andExpect(content().string(TestConstants.TEST_TURTLE));
    }
}

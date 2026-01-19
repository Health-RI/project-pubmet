/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.services;

import nl.healthri.pubmet.core.domain.Index;
import nl.healthri.pubmet.core.domain.IndexType;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

import static nl.healthri.pubmet.core.TestConstants.TEST_INVALID_MODEL;
import static nl.healthri.pubmet.core.TestConstants.TEST_MODEL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class MetadataManagerServicesTest {
    @MockitoBean
    private IndexService indexService;
    private MetadataManagerServices metadataManagerService;

    @BeforeEach
    void setUp() {
        metadataManagerService = new MetadataManagerServices(indexService);
    }

    public Index createSampleIndex() throws URISyntaxException, MalformedURLException {
        return new Index(
                UUID.randomUUID(),
                new URI("http://healthri.nl/").toURL(),
                "Health-RI",
                IndexType.PUSH
        );
    }

    @Test
    void GivenModel_WhenUploadMetadata_ThenSaveModel() throws IOException, URISyntaxException {
        // Arrange
        var index = createSampleIndex();
        var model = TEST_MODEL;
        var origin = "https://www.health-ri.nl/";
        var expectedMapSize = 1;

        Mockito.when(metadataManagerService.indexService
                        .findByOrigin(anyString()))
                .thenReturn(Optional.of(index));

        // Act
        metadataManagerService.uploadMetadata(model, origin);

        // Assert
        assertEquals(expectedMapSize, metadataManagerService.inMemoryModels.size());
    }

    @Test
    void GivenInvalidModel_WhenUploadMetadata_ThenThrowIOException(){
        // Arrange
        var origin = "https://www.health-ri.nl/";
        var model = TEST_INVALID_MODEL;

        // Act & Assert
        assertThrows(IOException.class, () ->
                metadataManagerService.uploadMetadata(model, origin)
        );
    }

    @Test
    void GivenNonExistentModel_WhenGettingMetadata_ReturnNotFound(){
        // Arrange
        var id = UUID.randomUUID();

        // Act
        var result = metadataManagerService.getMetadata(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void GivenExistingModel_WhenGettingMetadata_ReturnModel() throws IOException, URISyntaxException {
        // Arrange
        var index = createSampleIndex();
        var model = TEST_MODEL;
        var origin = "https://www.health-ri.nl/";
        var expectedMapSize = 1;

        // Act
        Mockito.when(metadataManagerService.indexService
                .findByOrigin(anyString()))
                .thenReturn(Optional.of(index));

        metadataManagerService.uploadMetadata(model, origin);

        // Assert
        assertEquals(expectedMapSize, metadataManagerService.inMemoryModels.size());
    }

}
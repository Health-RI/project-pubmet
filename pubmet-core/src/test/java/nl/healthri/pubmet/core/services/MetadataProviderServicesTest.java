/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.services;

import nl.healthri.pubmet.core.TestConstants;
import nl.healthri.pubmet.core.domain.Index;
import nl.healthri.pubmet.core.domain.IndexType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MetadataProviderServicesTest {

    @MockitoBean
    private IndexService indexService;

    private MetadataProviderServices metadataProviderService;

    @BeforeEach
    void setUp() {
        metadataProviderService = new MetadataProviderServices(indexService);
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
    void GivenValidModel_WhenUploadMetadata_ThenSaveModel() throws IOException, URISyntaxException {
        // Arrange
        var index = createSampleIndex();
        var modelContent = TestConstants.TEST_TURTLE;
        var contentType = "text/turtle";
        var origin = "https://www.health-ri.nl/";
        var expectedMapSize = 1;

        Mockito.when(metadataProviderService.indexService
                        .findByOrigin(anyString()))
                .thenReturn(Optional.of(index));

        // Act
        metadataProviderService.uploadMetadata(modelContent, contentType, origin);

        // Assert
        assertEquals(expectedMapSize, metadataProviderService.inMemoryModels.size());
    }

    @Test
    void GivenInvalidModel_WhenUploadMetadata_ThenThrowIOException(){
        // Arrange
        var data = "some random data";
        var invalidType = "application/not-real";
        var origin = "https://www.health-ri.nl/";

        // Act & Assert
        assertThrows(IOException.class, () ->
                metadataProviderService.uploadMetadata(data, invalidType, origin)
        );
    }

    @Test
    void GivenNonExistentModel_WhenGettingMetadata_ReturnEmptyList(){
        // Arrange
        var id = UUID.randomUUID();

        // Act
        var result = metadataProviderService.getMetadata(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void GivenExistingModel_WhenGettingMetadata_ReturnModel() throws IOException, URISyntaxException {
        // Arrange
        var index = createSampleIndex();
        var contentType = "text/turtle";
        var origin = "https://www.health-ri.nl/";
        var expectedMapSize = 1;


        // Act
        Mockito.when(metadataProviderService.indexService
                .findByOrigin(anyString()))
                .thenReturn(Optional.of(index));

        metadataProviderService.uploadMetadata(TestConstants.TEST_TURTLE, contentType, origin);

        // Assert
        assertEquals(expectedMapSize, metadataProviderService.inMemoryModels.size());
    }
}
/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.services;

import nl.healthri.pubmet.core.TestConstants;
import nl.healthri.pubmet.core.domain.Index;
import nl.healthri.pubmet.core.domain.IndexType;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MetadataManagerServicesTest {
    @MockitoBean
    private IndexService indexService;
    private MetadataManagerServices metadataManagerService;

    @BeforeEach
    void setUp() {
        metadataManagerService = new MetadataManagerServices(indexService);
    }

    @Test
    void GivenValidModel_WhenUploadMetadata_ThenSaveModel(){
        // Arrange
        var modelContent = TestConstants.TEST_TURTLE;
        var contentType = "text/turtle";
        var origin = "https://www.health-ri.nl/";

        // Act & Assert
        assertDoesNotThrow(() ->
                metadataManagerService.uploadMetadata(modelContent, contentType, origin)
        );
    }

    @Test
    void GivenInvalidModel_WhenUploadMetadata_ThenThrowIOException(){
        // Arrange
        var data = "some random data";
        var invalidType = "application/not-real";
        var origin = "https://www.health-ri.nl/";

        // Act & Assert
        assertThrows(IOException.class, () ->
                metadataManagerService.uploadMetadata(data, invalidType, origin)
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
        var url = URI.create("https://www.health-ri.nl/").toURL();
        var index = new Index(UUID.randomUUID(), url, "Health-RI", IndexType.PUSH);

        var contentType = "text/turtle";
        var expectedMapSize = 1;
        var origin = "https://www.health-ri.nl/";

        // Act
        Mockito.when(metadataManagerService.indexService
                .findByOrigin(anyString()))
                .thenReturn(Optional.of(index));

        metadataManagerService.uploadMetadata(TestConstants.TEST_TURTLE, contentType, origin);

        // Assert
        assertEquals(expectedMapSize, metadataManagerService.inMemoryModels.size());
    }

}
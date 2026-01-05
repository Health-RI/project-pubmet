/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.services;

import nl.healthri.pubmet.core.TestConstants;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MetadataManagerServicesTest {

    private MetadataManagerServices service;

    @BeforeEach
    void setUp() {
        var inMemoryModels = new HashMap<UUID, Model>();
        service = new MetadataManagerServices(inMemoryModels);
    }

    @Test
    void GivenValidModel_WhenUploadMetadata_ThenSaveModel(){
        // Arrange
        var modelContent = TestConstants.TEST_TURTLE;
        var contentType = "text/turtle";

        // Act & Assert
        assertDoesNotThrow(() ->
                service.uploadMetadata(modelContent, contentType)
        );
    }

    @Test
    void GivenInvalidModel_WhenUploadMetadata_ThenThrowIOException(){
        // Arrange
        var data = "some random data";
        var invalidType = "application/not-real";

        // Act & Assert
        assertThrows(IOException.class, () ->
                service.uploadMetadata(data, invalidType)
        );
    }

    @Test
    void GivenNonExistentModel_WhenGettingMetadata_ReturnNotFound(){
        // Arrange
        var id = UUID.randomUUID();

        // Act
        var result = service.getMetadata(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void GivenExistingModel_WhenGettingMetadata_ReturnModel() throws IOException {
        // Arrange
        var contentType = "text/turtle";
        var expectedMapSize = 1;

        // Act
        service.uploadMetadata(TestConstants.TEST_TURTLE, contentType);

        // Assert
        assertEquals(expectedMapSize, service.inMemoryModels.size());
    }

}
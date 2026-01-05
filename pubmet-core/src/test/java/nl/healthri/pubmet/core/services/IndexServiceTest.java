/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.services;

import nl.healthri.pubmet.core.domain.Index;
import nl.healthri.pubmet.core.domain.IndexType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class IndexServiceTest {

    private IndexService service;

    @BeforeEach
    void setUp() {
        service = new IndexService();
    }

    public Index createSampleIndex() throws URISyntaxException, MalformedURLException {
        return new Index(
                UUID.randomUUID(),
                "Test Title",
                "test@example.com",
                "Test Name",
                "Description",
                "Health-RI",
                new URI("https://example.com").toURL(),
                IndexType.PUSH
        );
    }

    @Test
    public void GivenValidIndex_WhenCreatingIndex_ReturnStatusCreated() throws URISyntaxException, MalformedURLException {
        // Arrange
        var index = createSampleIndex();

        // Act
        Index result = service.create(index);

        // Assert
        assertNotNull(result);
        assertEquals(index.id, result.id);
        assertEquals(1, service.inMemoryIndexes.size());
        assertEquals(index, service.inMemoryIndexes.get(index.id));
    }

    @Test
    public void GivenInvalidIndex_WhenCreatingIndex_Return() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> service.create(null));
    }

    @Test
    public void GivenExistingIndex_WhenFindingIndexById_ReturnIndex() throws MalformedURLException, URISyntaxException {
        // Arrange
        var index = createSampleIndex();

        // Act
        service.inMemoryIndexes.put(index.id, index);
        var found = service.findById(index.id);

        // Assert
        assertNotNull(found);
        assertEquals(index.id, found.id);
        assertEquals(index.title, found.title);
        assertEquals(index.description, found.description);
        assertEquals(index.name, found.name);
        assertEquals(index.url, found.url);
        assertEquals(index.email, found.email);
        assertEquals(index.organization, found.organization);
        assertEquals(index.url, found.url);
        assertEquals(index.indexType, found.indexType);

    }

    @Test
    public void GivenNonExistentIndex_WhenFindingIndexById_ReturnNotFoundException() {
        // Arrange
        var nonExistentId = UUID.randomUUID();

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> service.findById(nonExistentId));
    }
}
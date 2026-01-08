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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IndexServiceTest {

    private IndexService indexService;

    @BeforeEach
    void setUp() {
        indexService = new IndexService();
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
    public void GivenValidIndex_WhenCreatingIndex_ReturnStatusCreated() throws URISyntaxException, MalformedURLException {
        // Arrange
        var index = createSampleIndex();

        // Act
        Index result = indexService.create(index);

        // Assert
        assertNotNull(result);
        assertEquals(index.id, result.id);
        assertEquals(1, indexService.inMemoryIndexes.size());
        assertEquals(index, indexService.inMemoryIndexes.get(index.id));
    }

    @Test
    public void GivenInvalidIndex_WhenCreatingIndex_Return() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> indexService.create(null));
    }

    @Test
    public void GivenExistingIndex_WhenFindingIndexById_ReturnIndex() throws MalformedURLException, URISyntaxException {
        // Arrange
        var index = createSampleIndex();

        // Act
        indexService.inMemoryIndexes.put(index.id, index);
        var found = indexService.findById(index.id);

        // Assert
        assertNotNull(found);
        assertEquals(index.id, found.id);
        assertEquals(index.url, found.url);
        assertEquals(index.organization, found.organization);
    }

    @Test
    public void GivenNonExistentIndex_WhenFindingIndexById_ReturnNotFoundException() {
        // Arrange
        var nonExistentId = UUID.randomUUID();

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> indexService.findById(nonExistentId));
    }
}
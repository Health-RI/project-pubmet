/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.services;

import nl.healthri.pubmet.core.domain.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class IndexService {
    private static final Logger logger = LoggerFactory.getLogger(IndexService.class);

    public final Map<UUID, Index> inMemoryIndexes = new HashMap<>();

    public Index create(Index index) {
        inMemoryIndexes.put(index.id, index);
        logger.info("Successfully uploaded indexes. Total models: {}", inMemoryIndexes.size());

        return index;
    }

    public Index findById(UUID id) {
        Index found = inMemoryIndexes.get(id);
        if (found == null) {
            throw new NoSuchElementException("Index not found with ID: " + id);
        }

        return found;
    }
}
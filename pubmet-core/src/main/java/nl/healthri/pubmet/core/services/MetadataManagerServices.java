/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.services;

import nl.healthri.pubmet.core.api.MetadataManager;
import nl.healthri.pubmet.core.api.MetadataProvider;
import nl.healthri.pubmet.core.domain.IndexType;
import org.apache.coyote.BadRequestException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class MetadataManagerServices implements MetadataManager {
    private static final Logger logger = LoggerFactory.getLogger(MetadataManagerServices.class);

    public final Map<UUID, Model> inMemoryModels = new HashMap<>();
    public final IndexService indexService;

    public MetadataProviderServices(IndexService indexService) {
        this.indexService = indexService;
    }

    @Override
    public Optional<Model> getMetadata(UUID id) {
        return Optional.ofNullable(inMemoryModels.get(id));
    }

    @Override
    public void retrieveMetadata(){
        logger.info("retrieving metadata");

        // Gets all indexes with Pull type
        var indexes = indexService.getAllByType(IndexType.PULL);
        if(indexes.isEmpty()){
            throw new NoSuchElementException("FDP pull index list is empty, please add indexes before retrieving data.");
        }

        indexes.forEach(index -> {
            // add harvest logic
        });
    }

    @Override
    public Model uploadMetadata(String body, String contentType) throws IOException {
        logger.info("Uploading metadata");

        var reader = new StringReader(body);
        var format = Rio.getParserFormatForMIMEType(contentType)
                .orElseThrow(() -> new IOException("Unsupported content type: " + contentType));
        var model = Rio.parse(reader, "", format);
        var uuid = UUID.randomUUID();

        // Finds index with Push type matching origin URL
        var index = indexService.findByOrigin(origin);
        if(index.isEmpty()){
            throw new BadRequestException("provided origin does not exist in FDP");
        }

        // Saves metadata to in memory map
        inMemoryModels.put(uuid, model);
        logger.info("Successfully uploaded metadata. Total models: {}", inMemoryModels.size());

        return model;
    }

}
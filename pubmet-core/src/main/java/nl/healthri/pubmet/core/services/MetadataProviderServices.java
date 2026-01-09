/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.services;

import nl.healthri.pubmet.core.api.MetadataProvider;
import nl.healthri.pubmet.core.domain.Index;
import org.apache.coyote.BadRequestException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.Rio;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class MetadataProviderServices implements MetadataProvider {
    private static final Logger logger = LoggerFactory.getLogger(MetadataProviderServices.class);
    public final Map<UUID, Model> inMemoryModels = new HashMap<>();
    public final IndexService indexService;

    public MetadataProviderServices(IndexService indexService) {
        this.indexService = indexService;
    }

    @Override
    public Optional<Model> getMetadataById(@NonNull UUID id) {
        logger.info("getting metadata by id {}", id);
        return Optional.ofNullable(inMemoryModels.get(id));
    }

    @Override
    public List<Model> pullMetadata(Index index){
        logger.info("retrieving metadata");
        return inMemoryModels.values().stream().toList();
    }


    @Override
    public void uploadMetadata(@NonNull String body, @NonNull String contentType, @NonNull String origin) throws IOException, URISyntaxException {
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
    }
}
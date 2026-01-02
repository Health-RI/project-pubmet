/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.services;

import nl.healthri.pubmet.core.api.MetadataProvider;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.Rio;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@Service
public class MetadataProviderServices implements MetadataProvider {
    private static final Logger logger = LoggerFactory.getLogger(MetadataProviderServices.class);

    public final Map<UUID, Model> inMemoryModels = new HashMap<>();

    @Override
    public Optional<Model> getMetadata(@NonNull UUID id) {
        return Optional.ofNullable(inMemoryModels.get(id));
    }

    @Override
    public void uploadMetadata(@NonNull String body, @NonNull String contentType) throws IOException {
        logger.info("Uploading metadata");

        var reader = new StringReader(body);
        var format = Rio.getParserFormatForMIMEType(contentType)
                .orElseThrow(() -> new IOException("Unsupported content type: " + contentType));
        var model = Rio.parse(reader, "", format);
        var uuid = UUID.randomUUID();

        inMemoryModels.put(uuid, model);
        logger.info("Successfully uploaded metadata. Total models: {}", inMemoryModels.size());
    }
}
/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.api;

import nl.healthri.pubmet.core.domain.Index;
import org.eclipse.rdf4j.model.Model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MetadataProvider {
    Optional<Model> getMetadataById(UUID id);
    List<Model> pullMetadata(Index index);
    void uploadMetadata(String body, String contentType, String origin) throws IOException, URISyntaxException;
}

/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.api;

import org.apache.coyote.BadRequestException;
import org.eclipse.rdf4j.model.Model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

public interface MetadataProvider {
    Optional<Model> getMetadata(UUID id);
    void retrieveMetadata() throws BadRequestException;
    void uploadMetadata(String body, String contentType, String origin) throws IOException, URISyntaxException;
}

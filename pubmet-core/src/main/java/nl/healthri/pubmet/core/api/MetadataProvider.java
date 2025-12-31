/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.api;

import org.eclipse.rdf4j.model.Model;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface MetadataProvider {
    Optional<Model> getMetadata(UUID id);
    void uploadMetadata(String body, String contentType) throws IOException;
}

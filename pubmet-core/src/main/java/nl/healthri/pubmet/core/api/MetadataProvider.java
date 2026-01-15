/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.api;

import org.eclipse.rdf4j.model.Model;

import java.util.Optional;

public interface MetadataProvider {
    Optional<Model> getMetadata(String id);
}

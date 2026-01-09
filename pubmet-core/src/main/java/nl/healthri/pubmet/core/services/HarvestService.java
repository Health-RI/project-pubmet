/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.services;

import nl.healthri.pubmet.core.api.MetadataProvider;
import nl.healthri.pubmet.core.domain.IndexType;
import org.eclipse.rdf4j.model.Model;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HarvestService {

    private final IndexService indexService;
    private final MetadataProvider metadataProviderService;

    public HarvestService(IndexService indexService, MetadataProvider metadataProviderService) {
        this.indexService = indexService;
        this.metadataProviderService = metadataProviderService;
    }

    public List<Model> harvest() {
        var indexesToPull = indexService.getAllByType(IndexType.PULL);
        return indexesToPull.stream()
                .flatMap(index -> metadataProviderService.pullMetadata(index).stream())
                .toList();
    }
}
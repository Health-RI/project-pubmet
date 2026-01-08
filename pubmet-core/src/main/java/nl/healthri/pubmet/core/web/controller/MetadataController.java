/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web.controller;

import nl.healthri.pubmet.core.api.MetadataProvider;
import org.eclipse.rdf4j.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping(path = "metadata")
public class MetadataController {
    private static final Logger logger = LoggerFactory.getLogger(nl.healthri.pubmet.core.web.controller.MetadataController.class);

    private final MetadataProvider provider;

    public MetadataController(MetadataProvider provider) {
        this.provider = provider;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> getMetadata(@PathVariable UUID id) {
        logger.info("Got a request for metadata with id {}", id);

        var model = provider.getMetadata(id);
        return ResponseEntity.of(model);
    }

    @PostMapping()
    public ResponseEntity<Void> uploadMetadata(
            @RequestBody String body,
            @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
            @RequestHeader(HttpHeaders.ORIGIN) String origin)
    {
        logger.info("Received request to upload metadata containing");

        try {
            provider.uploadMetadata(body, contentType, origin);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Failed to upload metadata", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web.controller;

import jdk.jfr.ContentType;
import nl.healthri.pubmet.core.api.MetadataProvider;
import org.apache.coyote.BadRequestException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF4J;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Reader;
import java.io.StringReader;

@RestController
@RequestMapping
public class MetadataController {
    private static final Logger logger = LoggerFactory.getLogger(nl.healthri.pubmet.core.web.controller.MetadataController.class);

    private final MetadataProvider provider;

    public MetadataController(MetadataProvider provider) {
        this.provider = provider;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Model> getMetadata(@PathVariable String id) {
        logger.info("Got a request for metadata with id {}", id);

        var model = provider.getMetadata(id);
        return ResponseEntity.of(model);
    }

    @PostMapping()
    public ResponseEntity<Model> uploadMetadata(
            @RequestBody String body,
            @RequestHeader("Content-Type") String contentType){
        logger.info("Received request to upload metadata containing");

        try {
            var model = provider.uploadMetadata(body, contentType);
            return ResponseEntity.status(HttpStatus.CREATED).body((model));
        } catch (Exception e) {
            logger.error("Failed to upload metadata", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

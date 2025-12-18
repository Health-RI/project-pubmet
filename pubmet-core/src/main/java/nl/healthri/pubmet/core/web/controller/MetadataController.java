/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web.controller;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MetadataController {
    private static final Logger logger = LoggerFactory.getLogger(MetadataController.class);

    @GetMapping("/{id}")
    public ResponseEntity<Model> getMetadata(@PathVariable String id) {
        logger.info("Got a request for metadata with id {}", id);

        var model = new LinkedHashModel();
        var subject = Values.iri("http://example.com");
        var object = Values.literal("hello world");

        model.add(subject, RDFS.LABEL, object);

        return ResponseEntity.ok(model);
    }
}

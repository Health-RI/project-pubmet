/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web.controller;


import nl.healthri.pubmet.core.services.HarvestService;
import org.eclipse.rdf4j.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "harvest")
public class HarvestController {
    private static final Logger logger = LoggerFactory.getLogger(nl.healthri.pubmet.core.web.controller.HarvestController.class);
    private final HarvestService harvestService;

    public HarvestController(HarvestService harvestService) {
        this.harvestService = harvestService;
    }

    @PostMapping("")
    public ResponseEntity<List<Model>> harvestMetadata()
    {
        logger.info("Received request to upload metadata containing");

        try {
            var models = harvestService.harvest();
            return ResponseEntity.ok(models);
        } catch (Exception e) {
            logger.error("Failed to upload metadata", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

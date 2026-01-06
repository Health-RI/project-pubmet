/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web.controller;

import nl.healthri.pubmet.core.domain.Index;
import nl.healthri.pubmet.core.services.IndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping(path = "index")
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(nl.healthri.pubmet.core.web.controller.IndexController.class);

    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @PostMapping
    public ResponseEntity<Index> create(@RequestBody Index index) {
        indexService.create(index);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Index> GetById(@PathVariable UUID id) {
        try {
            var index = indexService.findById(id);
            return ResponseEntity.ok(index);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

}
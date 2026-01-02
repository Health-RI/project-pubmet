/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.web.controller;

import nl.healthri.pubmet.core.domain.Index;
import nl.healthri.pubmet.core.services.IndexService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "index")
public class IndexController {

    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @PostMapping
    public Index create(@RequestBody Index index) {
        return indexService.create(index);
    }

    @GetMapping("/{id}")
    public Index GetById(@PathVariable UUID id) {
        return indexService.findById(id);
    }
}
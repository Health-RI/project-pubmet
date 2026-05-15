/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core.domain;

import java.net.URL;
import java.util.UUID;

public class Index {
    public UUID id;
    public URL url;
    public String organization;
    public IndexType type;

    public Index(UUID id, URL url, String organization, IndexType type) {
        this.id = id;
        this.url = url;
        this.organization = organization;
        this.type = type;
    }
}
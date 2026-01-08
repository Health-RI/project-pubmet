/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.cloud.harvester;

import nl.healthri.pubmet.cloud.api.PullHarvester;

import java.net.URL;

public class PullHarvesterImpl extends AbstractHarvesterBase implements PullHarvester {
    @Override
    public void harvest(URL endpoint) {
        // TODO resolve endpoint content
    }
}

/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.cloud.harvester;

import nl.healthri.pubmet.cloud.api.PushHarvester;
import org.eclipse.rdf4j.model.Model;

public class PushHarvesterImpl extends AbstractHarvesterBase implements PushHarvester {
    @Override
    public void harvest(Model model) {
        harvesterInternal(model);
    }
}

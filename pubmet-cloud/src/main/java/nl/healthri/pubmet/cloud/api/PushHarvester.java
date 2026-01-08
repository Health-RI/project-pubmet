/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.cloud.api;

import org.eclipse.rdf4j.model.Model;

// TODO consider moving to a separate module
public interface PushHarvester {
    // TODO refine the method signature
    void harvest(Model model);
}

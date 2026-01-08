/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.cloud.api;

import java.net.URL;

// TODO consider moving to a separate module
public interface PullHarvester {
    // TODO refine the method signature
    void harvest(URL endpoint);
}

/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core;

import nl.healthri.pubmet.core.api.MetadataManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CoreAutoConfigurationTest {
    @MockitoBean
    MetadataManager provider;

    @Test
    public void testAutoConfigurationLoaded(@Autowired ApplicationContext applicationContext) {
        assertThat(applicationContext.containsBean("message-converter-ttl")).isTrue();
    }
}

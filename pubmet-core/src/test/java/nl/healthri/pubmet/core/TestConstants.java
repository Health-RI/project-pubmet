/*
 * SPDX-FileCopyrightText: 2025 Health-RI
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package nl.healthri.pubmet.core;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

public class TestConstants {
    public static Model TEST_MODEL;
    public static String TEST_TURTLE;
    public static Model TEST_INVALID_MODEL;
    public static String TEST_INVALID_TURTLE;

    static {
        TEST_MODEL = new LinkedHashModel();
        TEST_MODEL.add(Values.iri("http://example.com"), RDFS.LABEL, Values.literal("hello world"));
        TEST_INVALID_MODEL = new LinkedHashModel();
        TEST_INVALID_MODEL.add(Values.iri("http://example.com"), RDFS.LABEL, Values.literal("hello world"));

        TEST_TURTLE = "<http://example.com> <http://www.w3.org/2000/01/rdf-schema#label> \"hello world\" .";
        TEST_INVALID_TURTLE = "<this is not valid> <this is not valid> \"this is not valid\" .";
    }
}

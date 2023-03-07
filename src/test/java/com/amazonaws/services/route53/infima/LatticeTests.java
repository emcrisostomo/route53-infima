/**
 * Copyright 2013-2013 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * <p>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.amazonaws.services.route53.infima;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class LatticeTests
{

    @Test
    public void testSingleCellLattice()
    {
        SingleCellLattice<String> lattice = new SingleCellLattice<>();
        lattice.addEndpoint("A");
        lattice.addEndpoints(Arrays.asList("B", "C", "D"));

        /* Check that all of the end points in, with ordering */
        assertEquals("[B, C, D, A]", lattice.getAllEndpoints().toString());
    }

    @Test
    public void testOneDimensionalLattice()
    {
        String[] endpointsA = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        String[] endpointsB = new String[]{"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"};

        OneDimensionalLattice<String> lattice = new OneDimensionalLattice<>("AZ");
        lattice.addEndpoints("us-east-1a", Arrays.asList(endpointsA));
        lattice.addEndpoints("us-east-1b", Arrays.asList(endpointsB));

        assertEquals(20, lattice.getAllEndpoints().size());
        assertEquals(10, lattice.simulateFailure("AZ", "us-east-1a").getAllEndpoints().size());
        assertEquals(10, lattice.simulateFailure("AZ", "us-east-1b").getAllEndpoints().size());
    }

    @Test
    public void testTwoDimensionalLattice()
    {
        /* Use a 2-D lattice with 20 endpoints for a very simple test */
        String[] endpointsA1 = new String[]{"A", "B", "C", "D", "E"};
        String[] endpointsA2 = new String[]{"F", "G", "H", "I", "J"};
        String[] endpointsB1 = new String[]{"K", "L", "M", "N", "O"};
        String[] endpointsB2 = new String[]{"P", "Q", "R", "S", "T"};

        TwoDimensionalLattice<String> lattice = new TwoDimensionalLattice<>("AZ", "Version");
        lattice.addEndpoints("us-east-1a", "1", Arrays.asList(endpointsA1));
        lattice.addEndpoints("us-east-1a", "2", Arrays.asList(endpointsA2));
        lattice.addEndpoints("us-east-1b", "1", Arrays.asList(endpointsB1));
        lattice.addEndpoints("us-east-1b", "2", Arrays.asList(endpointsB2));

        assertEquals(20, lattice.getAllEndpoints().size());
        assertEquals(10, lattice.simulateFailure("AZ", "us-east-1a").getAllEndpoints().size());
        assertEquals(10, lattice.simulateFailure("AZ", "us-east-1b").getAllEndpoints().size());
        assertEquals(10, lattice.simulateFailure("Version", "1").getAllEndpoints().size());
        assertEquals(10, lattice.simulateFailure("Version", "2").getAllEndpoints().size());
        assertEquals(5, lattice.simulateFailure("AZ", "us-east-1a").simulateFailure("Version", "1").getAllEndpoints().size());
        assertEquals(5, lattice.simulateFailure("AZ", "us-east-1a").simulateFailure("Version", "2").getAllEndpoints().size());
        assertEquals(5, lattice.simulateFailure("AZ", "us-east-1b").simulateFailure("Version", "1").getAllEndpoints().size());
        assertEquals(5, lattice.simulateFailure("AZ", "us-east-1b").simulateFailure("Version", "2").getAllEndpoints().size());
    }
}

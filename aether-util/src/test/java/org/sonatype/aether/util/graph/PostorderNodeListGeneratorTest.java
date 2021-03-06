package org.sonatype.aether.util.graph;

/*******************************************************************************
 * Copyright (c) 2010-2011 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.test.util.DependencyGraphParser;

public class PostorderNodeListGeneratorTest
{

    private DependencyNode parse( String resource )
        throws Exception
    {
        return new DependencyGraphParser( "visitor/ordered-list/" ).parse( resource );
    }

    private void assertSequence( List<DependencyNode> actual, String... expected )
    {
        assertEquals( actual.toString(), expected.length, actual.size() );
        for ( int i = 0; i < expected.length; i++ )
        {
            DependencyNode node = actual.get( i );
            assertEquals( actual.toString(), expected[i], node.getDependency().getArtifact().getArtifactId() );
        }
    }

    @Test
    public void testOrdering()
        throws Exception
    {
        DependencyNode root = parse( "simple.txt" );

        PostorderNodeListGenerator visitor = new PostorderNodeListGenerator();
        root.accept( visitor );

        assertSequence( visitor.getNodes(), "c", "b", "e", "d", "a" );
    }

    @Test
    public void testDuplicateSuppression()
        throws Exception
    {
        DependencyNode root = parse( "cycles.txt" );

        PostorderNodeListGenerator visitor = new PostorderNodeListGenerator();
        root.accept( visitor );

        assertSequence( visitor.getNodes(), "c", "b", "e", "d", "a" );
    }

}

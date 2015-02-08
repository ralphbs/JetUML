package ca.mcgill.cs.stg.jetuml.framework;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;

import org.junit.Test;

import ca.mcgill.cs.stg.jetuml.graph.Edge;
import ca.mcgill.cs.stg.jetuml.graph.Graph;
import ca.mcgill.cs.stg.jetuml.graph.Node;

public class TestPersistenceService
{
	@Test
	public void testClassDiagram() throws Exception
	{
		Graph graph = PersistenceService.read(new FileInputStream("testdata/testPersistenceService.class.jet"));
		Collection<Node> nodes = graph.getNodes();
		assertEquals(8, nodes.size());
		Collection<Edge> edges = graph.getEdges();
		assertEquals(6, edges.size());
		
		File tmp = new File("testdata/tmp");
		tmp.delete();
		PersistenceService.saveFile(graph, new FileOutputStream(tmp));
		graph = PersistenceService.read(new FileInputStream(tmp));
		nodes = graph.getNodes();
		assertEquals(8, nodes.size());
		edges = graph.getEdges();
		assertEquals(6, edges.size());
		tmp.delete();
	}
}

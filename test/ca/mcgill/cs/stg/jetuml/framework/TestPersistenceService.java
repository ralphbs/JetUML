package ca.mcgill.cs.stg.jetuml.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import ca.mcgill.cs.stg.jetuml.graph.ClassNode;
import ca.mcgill.cs.stg.jetuml.graph.ClassRelationshipEdge;
import ca.mcgill.cs.stg.jetuml.graph.Edge;
import ca.mcgill.cs.stg.jetuml.graph.Graph;
import ca.mcgill.cs.stg.jetuml.graph.InterfaceNode;
import ca.mcgill.cs.stg.jetuml.graph.Node;
import ca.mcgill.cs.stg.jetuml.graph.NoteEdge;
import ca.mcgill.cs.stg.jetuml.graph.NoteNode;
import ca.mcgill.cs.stg.jetuml.graph.PackageNode;
import ca.mcgill.cs.stg.jetuml.graph.PointNode;

public class TestPersistenceService
{
	private static final String TEST_FILE_NAME = "testdata/tmp";
	
	@Test
	public void testClassDiagram() throws Exception
	{
		Graph graph = PersistenceService.read(new FileInputStream("testdata/testPersistenceService.class.jet"));
		verifyClassDiagram(graph);
		
		File tmp = new File(TEST_FILE_NAME);
		tmp.delete();
		PersistenceService.saveFile(graph, new FileOutputStream(tmp));
		graph = PersistenceService.read(new FileInputStream(tmp));
		verifyClassDiagram(graph);
		tmp.delete();
	}
	
	private void verifyClassDiagram(Graph pGraph)
	{
		Collection<Node> nodes = pGraph.getNodes();
		
		assertEquals(8, nodes.size());
		Iterator<Node> nIterator = nodes.iterator();
		
		ClassNode node1 = (ClassNode) nIterator.next();
		InterfaceNode node2 = (InterfaceNode) nIterator.next();
		ClassNode node3 = (ClassNode) nIterator.next();
		ClassNode node4 = (ClassNode) nIterator.next();
		NoteNode node5 = (NoteNode) nIterator.next();
		PackageNode node6 = (PackageNode) nIterator.next();
		ClassNode node7 = (ClassNode) nIterator.next();
		PointNode node8 = (PointNode) nIterator.next();
		
		assertEquals("", node1.getAttributes().getText());
		assertTrue(node1.getChildren().isEmpty());
		assertEquals("", node1.getMethods().getText());
		assertEquals("Class1", node1.getName().getText());
		assertNull(node1.getParent());
		assertEquals(new Rectangle2D.Double(470, 180, 100, 60), node1.getBounds());
		
		assertTrue(node2.getChildren().isEmpty());
		assertEquals("", node2.getMethods().getText());
		assertEquals("«interface»", node2.getName().getText());
		assertNull(node2.getParent());
		assertEquals(new Rectangle2D.Double(470, 70, 100, 60), node2.getBounds());
		
		assertEquals("foo", node3.getAttributes().getText());
		assertTrue(node3.getChildren().isEmpty());
		assertEquals("bar", node3.getMethods().getText());
		assertEquals("Class2", node3.getName().getText());
		assertNull(node3.getParent());
		assertEquals(new Rectangle2D.Double(470, 310, 100, 60), node3.getBounds());
		
		assertEquals("", node4.getAttributes().getText());
		assertTrue(node4.getChildren().isEmpty());
		assertEquals("", node4.getMethods().getText());
		assertEquals("Class3", node4.getName().getText());
		assertNull(node4.getParent());
		assertEquals(new Rectangle2D.Double(660, 180, 100, 60), node4.getBounds());
		
		assertTrue(node5.getChildren().isEmpty());
		assertEquals("A note", node5.getText().getText());
		assertNull(node5.getParent());
		assertEquals(new Rectangle2D.Double(770, 310, 60, 40), node5.getBounds());
		
		List<Node> children = node6.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(node7));
		assertEquals("", node6.getContents().getText());
		assertEquals("Package", node6.getName());
		assertNull(node6.getParent());
		assertEquals(new Rectangle2D.Double(220, 110, 160, 120), node6.getBounds());

		assertEquals("", node7.getAttributes().getText());
		assertTrue(node7.getChildren().isEmpty());
		assertEquals("", node7.getMethods().getText());
		assertEquals("Class", node7.getName().getText());
		assertEquals(node6,node7.getParent());
		assertEquals(new Rectangle2D.Double(260, 160, 100, 60), node7.getBounds());
		
		assertEquals(new Rectangle2D.Double(708, 229, 0, 0), node8.getBounds());
		assertTrue(node8.getChildren().isEmpty());
		assertNull(node8.getParent());
		
		Collection<Edge> edges = pGraph.getEdges();
		assertEquals(6, edges.size());
		Iterator<Edge> eIterator = edges.iterator();
		ClassRelationshipEdge edge1 = (ClassRelationshipEdge) eIterator.next();
		ClassRelationshipEdge edge2 = (ClassRelationshipEdge) eIterator.next();
		ClassRelationshipEdge edge3 = (ClassRelationshipEdge) eIterator.next();
		ClassRelationshipEdge edge4 = (ClassRelationshipEdge) eIterator.next();
		NoteEdge edge5 = (NoteEdge) eIterator.next();
		ClassRelationshipEdge edge6 = (ClassRelationshipEdge) eIterator.next();
		
	}
	
	@Test
	public void testSequenceDiagram() throws Exception
	{
		Graph graph = PersistenceService.read(new FileInputStream("testdata/testPersistenceService.sequence.jet"));
		verifySequenceDiagram(graph);
		
		File tmp = new File(TEST_FILE_NAME);
		tmp.delete();
		PersistenceService.saveFile(graph, new FileOutputStream(tmp));
		graph = PersistenceService.read(new FileInputStream(tmp));
		verifySequenceDiagram(graph);
		tmp.delete();
	}
	
	private void verifySequenceDiagram(Graph pGraph)
	{
		Collection<Node> nodes = pGraph.getNodes();
		assertEquals(9, nodes.size());
	}
}

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

import ca.mcgill.cs.stg.jetuml.graph.CallNode;
import ca.mcgill.cs.stg.jetuml.graph.ClassNode;
import ca.mcgill.cs.stg.jetuml.graph.ClassRelationshipEdge;
import ca.mcgill.cs.stg.jetuml.graph.Edge;
import ca.mcgill.cs.stg.jetuml.graph.Graph;
import ca.mcgill.cs.stg.jetuml.graph.ImplicitParameterNode;
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
	
	@Test
	public void testClassDiagramContainment() throws Exception
	{
		Graph graph = PersistenceService.read(new FileInputStream("testdata/testPersistenceService2.class.jet"));
		verifyClassDiagram2(graph);
		
		File tmp = new File(TEST_FILE_NAME);
		tmp.delete();
		PersistenceService.saveFile(graph, new FileOutputStream(tmp));
		graph = PersistenceService.read(new FileInputStream(tmp));
		verifyClassDiagram2(graph);
		tmp.delete();
	}
	
	private void verifyClassDiagram2(Graph pGraph)
	{
		Collection<Node> nodes = pGraph.getNodes();
		assertEquals(8, nodes.size());
		Iterator<Node> nIterator = nodes.iterator();
		PackageNode p1 = (PackageNode) nIterator.next();
		ClassNode c1 = (ClassNode) nIterator.next();
		PackageNode p2 = (PackageNode) nIterator.next();
		NoteNode n1 = (NoteNode) nIterator.next();
		PackageNode p3 = (PackageNode) nIterator.next();
		PackageNode p4 = (PackageNode) nIterator.next();
		InterfaceNode i1 = (InterfaceNode) nIterator.next();
		ClassNode c2 = (ClassNode) nIterator.next();
		
		List<Node> children = p1.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(c1));
		assertEquals(p1, c1.getParent());
		assertEquals(0, c1.getChildren().size());
		
		children = p2.getChildren();
		assertEquals(0, children.size());
		assertNull(n1.getParent());
		
		children = p3.getChildren();
		assertEquals(2, children.size());
		assertTrue(children.contains(p4));
		assertTrue(children.contains(c2));
		assertEquals(p3, p4.getParent());
		assertEquals(p3, c2.getParent());
		
		children = p4.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(i1));
		assertEquals(p4, i1.getParent());
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
		assertEquals("VHV", edge1.getBentStyle().toString());
		assertEquals(new Rectangle2D.Double(515, 130, 22, 50), edge1.getBounds());
		assertEquals(node2, edge1.getEnd());
		assertEquals("TRIANGLE", edge1.getEndArrowHead().toString());
		assertEquals("", edge1.getEndLabel());
		assertEquals("DOTTED", edge1.getLineStyle().toString());
		assertEquals("e2", edge1.getMiddleLabel());
		assertEquals(node1, edge1.getStart());
		assertEquals("NONE", edge1.getStartArrowHead().toString());
		assertEquals("", edge1.getStartLabel());
		
		ClassRelationshipEdge edge2 = (ClassRelationshipEdge) eIterator.next();
		assertEquals("VHV", edge2.getBentStyle().toString());
		assertEquals(new Rectangle2D.Double(515, 240, 22, 70), edge2.getBounds());
		assertEquals(node1, edge2.getEnd());
		assertEquals("TRIANGLE", edge2.getEndArrowHead().toString());
		assertEquals("", edge2.getEndLabel());
		assertEquals("SOLID", edge2.getLineStyle().toString());
		assertEquals("e3", edge2.getMiddleLabel());
		assertEquals(node3, edge2.getStart());
		assertEquals("NONE", edge2.getStartArrowHead().toString());
		assertEquals("", edge2.getStartLabel());
		
		ClassRelationshipEdge edge3 = (ClassRelationshipEdge) eIterator.next();
		assertEquals("HVH", edge3.getBentStyle().toString());
		assertEquals(new Rectangle2D.Double(570, 191, 90, 24), edge3.getBounds());
		assertEquals(node4, edge3.getEnd());
		assertEquals("NONE", edge3.getEndArrowHead().toString());
		assertEquals("*", edge3.getEndLabel());
		assertEquals("SOLID", edge3.getLineStyle().toString());
		assertEquals("e4", edge3.getMiddleLabel());
		assertEquals(node1, edge3.getStart());
		assertEquals("DIAMOND", edge3.getStartArrowHead().toString());
		assertEquals("1", edge3.getStartLabel());
		
		ClassRelationshipEdge edge4 = (ClassRelationshipEdge) eIterator.next();
		assertEquals("HVH", edge4.getBentStyle().toString());
		assertEquals(new Rectangle2D.Double(570, 205, 90, 135), edge4.getBounds());
		assertEquals(node3, edge4.getEnd());
		assertEquals("NONE", edge4.getEndArrowHead().toString());
		assertEquals("", edge4.getEndLabel());
		assertEquals("SOLID", edge4.getLineStyle().toString());
		assertEquals("e5", edge4.getMiddleLabel());
		assertEquals(node4, edge4.getStart());
		assertEquals("BLACK_DIAMOND", edge4.getStartArrowHead().toString());
		assertEquals("", edge4.getStartLabel());
		
		NoteEdge edge5 = (NoteEdge) eIterator.next();
		assertEquals(new Rectangle2D.Double(708, 229, 74, 81), edge5.getBounds());
		assertEquals(node5, edge5.getStart());
		assertEquals(node8, edge5.getEnd());
		
		ClassRelationshipEdge edge6 = (ClassRelationshipEdge) eIterator.next();
		assertEquals("Straight", edge6.getBentStyle().toString());
		assertEquals(new Rectangle2D.Double(360, 181, 110, 29), edge6.getBounds());
		assertEquals(node7, edge6.getEnd());
		assertEquals("V", edge6.getEndArrowHead().toString());
		assertEquals("", edge6.getEndLabel());
		assertEquals("DOTTED", edge6.getLineStyle().toString());
		assertEquals("e1", edge6.getMiddleLabel());
		assertEquals(node1, edge6.getStart());
		assertEquals("NONE", edge6.getStartArrowHead().toString());
		assertEquals("", edge6.getStartLabel());
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
		Iterator<Node> nIterator = nodes.iterator();
		ImplicitParameterNode node1 = (ImplicitParameterNode) nIterator.next();
		CallNode node2 = (CallNode) nIterator.next();
		CallNode node3 = (CallNode) nIterator.next();
		ImplicitParameterNode node4 = (ImplicitParameterNode) nIterator.next();
		CallNode node5 = (CallNode) nIterator.next();
		ImplicitParameterNode node6 = (ImplicitParameterNode) nIterator.next();
		CallNode node7 = (CallNode) nIterator.next();
		NoteNode node8 = (NoteNode) nIterator.next();
		PointNode node9 = (PointNode) nIterator.next();
		
		assertEquals(new Rectangle2D.Double(210, 0, 100, 215), node1.getBounds());
		assertEquals(0, node1.getChildren().size());
		assertEquals("object1:Type1", node1.getName().toString());
		assertNull(node1.getParent());
		
		assertEquals(new Rectangle2D.Double(252, 73, 16, 30), node2.getBounds());
		List<Node> children = node2.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(node3));
		assertEquals(node1, node2.getImplicitParameter());
		assertNull(node2.getParent());
		
		// In sequence diagram call nodes have a children the call
		// nodes they call
		assertEquals(new Rectangle2D.Double(260, 102, 16, 30), node3.getBounds());
		children = node3.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(node5));
		assertEquals(node1, node3.getImplicitParameter());
		assertEquals(node2, node3.getParent());
		
		assertEquals(new Rectangle2D.Double(500, 0, 80, 215), node4.getBounds());
		assertEquals(0, node4.getChildren().size());
		assertEquals(":Type2", node4.getName().toString());
		assertNull(node4.getParent());
		
		assertEquals(new Rectangle2D.Double(532, 121, 16, 30), node5.getBounds());
		children = node5.getChildren();
		assertEquals(1, children.size());
		assertTrue(children.contains(node7));
		assertEquals(node4, node5.getImplicitParameter());
		assertEquals(node3, node5.getParent());
		
		assertEquals(new Rectangle2D.Double(640, 0, 80, 215), node6.getBounds());
		assertEquals(0, node6.getChildren().size());
		assertEquals("object3:", node6.getName().toString());
		assertNull(node6.getParent());
		
		assertEquals(new Rectangle2D.Double(672, 145, 16, 30), node7.getBounds());
		children = node7.getChildren();
		assertEquals(0, children.size());
		assertEquals(node6, node7.getImplicitParameter());
		assertEquals(node5, node7.getParent());
		
		assertTrue(node8.getChildren().isEmpty());
		assertEquals("A note", node8.getText().getText());
		assertNull(node8.getParent());
		assertEquals(new Rectangle2D.Double(610, 210, 60, 40), node8.getBounds());
		
		assertEquals(new Rectangle2D.Double(538, 169, 0, 0), node9.getBounds());
		assertTrue(node9.getChildren().isEmpty());
		assertNull(node9.getParent());
	}
}

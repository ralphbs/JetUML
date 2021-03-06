package ca.mcgill.cs.stg.jetuml.framework;

import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.Stack;

import ca.mcgill.cs.stg.jetuml.graph.Edge;
import ca.mcgill.cs.stg.jetuml.graph.GraphElement;
import ca.mcgill.cs.stg.jetuml.graph.Node;

/**
 * Manages a set of graph element selections. The list does
 * not accept duplicate graph elements.
 * 
 * @author Martin P. Robillard
 *
 */
public class SelectionList implements Iterable<GraphElement>
{
	private Stack<GraphElement> aSelected = new Stack<>();
	
	/**
	 * Adds an element to the selection set and sets
	 * it as the last selected element.
	 * 
	 * @param pElement The element to add to the list.
	 * Cannot be null.
	 */
	public void add(GraphElement pElement)
	{
		assert pElement != null;
		aSelected.remove(pElement);
		aSelected.push(pElement);
	}
	
	/**
	 * Removes all selections.
	 */
	public void clearSelection()
	{
		aSelected.clear();
	}
	
	/**
	 * @return The last element that was selected, or null
	 * if there are no such elements.
	 */
	public GraphElement getLastSelected()
	{
		if( aSelected.size() > 0 )
		{
			return aSelected.peek();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * @return The last Node that was selected, or null 
	 * if there are no Nodes selected.
	 */
	public Node getLastNode()
	{
		if( aSelected.size() > 0 )
		{
			Iterator<GraphElement> iter = iterator();
			while(iter.hasNext())
			{
				GraphElement cur = iter.next();
				if(cur instanceof Node)
				{
					return (Node)cur;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param pElement The element to test.
	 * @return True if pElement is in the list of selected elements.
	 */
	public boolean contains(GraphElement pElement)
	{
		return aSelected.contains(pElement);
	}
	
	/**
	 * Removes pElement from the list of selected elements,
	 * or does nothing if pElement is not selected.
	 * @param pElement The element to remove. Cannot be null.
	 */
	public void remove(GraphElement pElement)
	{
		assert pElement != null;
		
		if(pElement instanceof Node) //TODO : Fix when fixing whether edges are visible from nodes
		{
			Stack<GraphElement> copy = (Stack<GraphElement>) aSelected.clone();
			for(GraphElement e : copy)
			{
				if(e instanceof Edge)
				{
					if(((Edge) e).getEnd() == pElement || ((Edge) e).getStart() == pElement)
					{
						aSelected.remove(e);
					}
				}
			}
		}
		aSelected.remove(pElement);
	}
	
	/**
	 * Sets pElement as the single selected element.
	 * @param pElement The element to set as selected. Cannot
	 * be null.
	 */
	public void set(GraphElement pElement)
	{
		assert pElement != null;
		aSelected.clear();
		aSelected.add(pElement);
	}

	@Override
	public Iterator<GraphElement> iterator()
	{
		return aSelected.iterator();
	}
	
	/**
	 * @return The number of elements currently selected.
	 */
	public int size()
	{
		return aSelected.size();
	}
}

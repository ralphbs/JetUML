package ca.mcgill.cs.stg.jetuml.framework;

import java.awt.geom.Point2D;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.Statement;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import ca.mcgill.cs.stg.jetuml.graph.AbstractNode;
import ca.mcgill.cs.stg.jetuml.graph.Graph;

/**
 * Services for saving and loading Graph objects (i.e., UML diagrams).
 * We use long-term bean persistence to save the graph data. 
 * 
 * @author Martin P. Robillard
 *
 */
public final class PersistenceService
{
	// workaround for bug #4646747 in J2SE SDK 1.4.0
	private static HashMap beanInfos;
	
	private static PersistenceDelegate staticFieldDelegate = new DefaultPersistenceDelegate()
	{
		@Override
		protected Expression instantiate(Object pOldInstance, Encoder pOut)
		{
			try
			{
				Class<?> cl = pOldInstance.getClass();
				Field[] fields = cl.getFields();
				for(int i = 0; i < fields.length; i++)
				{
					if(Modifier.isStatic(fields[i].getModifiers()) && fields[i].get(null) == pOldInstance)
					{
						return new Expression(fields[i], "get", new Object[] { null });
					}
				}
			}
			catch(IllegalAccessException ex) 
			{
				ex.printStackTrace();
			}
			return null;
		}
            
		@Override
		protected boolean mutatesTo(Object pOldInstance, Object pNewInstance)
		{
			return pOldInstance == pNewInstance;
		}
	};
         
	static
	{
		beanInfos = new HashMap();
		Class[] cls = new Class[]
				{
                  Point2D.Double.class,
                  BentStyle.class,
                  ArrowHead.class,
                  LineStyle.class,
                  Graph.class,
                  AbstractNode.class,
               };
            for (int i = 0; i < cls.length; i++)
            {
               try
               {
                  beanInfos.put(cls[i], java.beans.Introspector.getBeanInfo(cls[i]));
               }         
               catch (java.beans.IntrospectionException ex)
               {
               }
            }
         }

	
	private PersistenceService() {}
	
	/**
	 * Reads a graph file from pIn then close pIn.
	 * @param pIn the input stream to read. Cannot be null.
	 * @return the graph that is read in
	 * @throws IOException if the graph cannot be read.
	 */
	public static Graph read(InputStream pIn) throws IOException
	{
		assert pIn != null;
		try( XMLDecoder reader = new XMLDecoder(pIn) )
		{
			Graph graph = (Graph) reader.readObject();
			return graph;
		}
		finally
		{
			pIn.close();
		}
	}
	
	/**
     * Saves the current graph in a file. 
     * 
     * @param pOut the stream for saving
     */
	public static void saveFile(Graph pGraph, OutputStream pOut)
   {
      XMLEncoder encoder = new XMLEncoder(pOut);
         
      encoder.setExceptionListener(new 
         ExceptionListener() 
         {
            public void exceptionThrown(Exception ex) 
            {
               ex.printStackTrace();
            }
         });
      /*
      The following does not work due to bug #4741757
        
      encoder.setPersistenceDelegate(
         Point2D.Double.class,
         new DefaultPersistenceDelegate(
            new String[]{ "x", "y" }) );
      */
      encoder.setPersistenceDelegate(Point2D.Double.class, new
            DefaultPersistenceDelegate()
            {
               protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) 
               {
                  super.initialize(type, oldInstance, newInstance, out);
                  Point2D p = (Point2D)oldInstance;
                  out.writeStatement( new Statement(oldInstance, "setLocation", new Object[]{ new Double(p.getX()), new Double(p.getY()) }) );
               }
            });
      
      encoder.setPersistenceDelegate(BentStyle.class, staticFieldDelegate);
      encoder.setPersistenceDelegate(LineStyle.class, staticFieldDelegate);
      encoder.setPersistenceDelegate(ArrowHead.class, staticFieldDelegate);
      
      Graph.setPersistenceDelegate(encoder);
      AbstractNode.setPersistenceDelegate(encoder);
      
      encoder.writeObject(pGraph);
      encoder.close();
   }
}

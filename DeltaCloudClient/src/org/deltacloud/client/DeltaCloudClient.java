package org.deltacloud.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DeltaCloudClient implements API 
{
	public static Logger logger = Logger.getLogger(DeltaCloudClient.class);
	
	private static enum DCNS
	{ 
		INSTANCES, REALMS, IMAGES, FLAVORS, START, STOP, REBOOT, DESTROY;
		
		@Override
		public String toString()
		{
			return "/" + name().toLowerCase();
		}
	} 
	
	private static enum RequestType { POST, GET };
	
	private URL baseUrl;
	
	private String username;
	
	private String password;
	
	public DeltaCloudClient(URL url, String username, String password) throws MalformedURLException
	{
		
		logger.debug("Creating new Delta Cloud Client for Server: " + url);
		
		this.baseUrl = url;
		
		this.username = username;
		
		this.password = password;
	}

	private String sendRequest(String path, RequestType requestType) throws DeltaCloudClientException
	{
		DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(new AuthScope(baseUrl.getHost(), baseUrl.getPort()), new UsernamePasswordCredentials(username, password));
        
		String requestUrl = baseUrl.toString() + path;
		logger.debug("Sending Request to: " + requestUrl);
		
		try
		{
			HttpUriRequest request = null;
			if(requestType == RequestType.POST)
			{
				request = new HttpPost(requestUrl);
			}
			else
			{
				request = new HttpGet(requestUrl);
			}
			
			request.setHeader("Accept", "application/xml");
			HttpResponse httpResponse = httpClient.execute(request);
			
			HttpEntity entity = httpResponse.getEntity();
			
			
			if (entity != null)
			{
				InputStream is = entity.getContent();
				String xml = readInputStreamToString(is);
				httpClient.getConnectionManager().shutdown();
				
				logger.debug("Response\n" + xml);
				return xml;
			}
		}
		catch(IOException e)
		{
			logger.error("Error processing request to: " + requestUrl, e);
			throw new DeltaCloudClientException("Error processing request to: " + requestUrl, e);
		}
		throw new DeltaCloudClientException("Could not execute request to:" + requestUrl);
	}
	
	private static String readInputStreamToString(InputStream is) throws DeltaCloudClientException
	{
		try
		{
			try
			{
				if (is != null)
				{
					StringBuilder sb = new StringBuilder();
					String line;
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					while ((line = reader.readLine()) != null) 
					{
						sb.append(line).append("\n");	
					}
					return sb.toString();
				}
			}
			finally
			{
				is.close();
			}
		}
		catch(Exception e)
		{
			throw new DeltaCloudClientException("Error converting Response to String", e);
		}
		return "";
	}
	
	@Override
	public Instance createInstance(String imageId) throws DeltaCloudClientException 
	{
		String query = "?image_id=" + imageId;
		return buildInstance(sendRequest(DCNS.INSTANCES + query, RequestType.POST));
	}
	
	@Override
	public Instance createInstance(String imageId, String flavorId, String realmId, String name) throws DeltaCloudClientException 
	{
		String query = "?image_id=" + imageId + "&flavor_id=" + flavorId + "&realm_id=" + realmId + "&name=" + name + "&commit=create";
		return buildInstance(sendRequest(DCNS.INSTANCES + query, RequestType.POST));
	}

	@Override
	public Flavor listFlavor(String flavorId) throws DeltaCloudClientException 
	{
		String request = DCNS.FLAVORS + "/" + flavorId;
		return JAXB.unmarshal(sendRequest(request, RequestType.GET), Flavor.class);
	}

	@Override
	public List<Flavor> listFlavors() throws DeltaCloudClientException 
	{
		return listDeltaCloudObjects(Flavor.class, DCNS.FLAVORS.toString(), "flavor");
	}
	
	@Override
	public List<Image> listImages() throws DeltaCloudClientException 
	{
		return listDeltaCloudObjects(Image.class, DCNS.IMAGES.toString(), "image");
	}

	@Override
	public Image listImages(String imageId) throws DeltaCloudClientException 
	{
		return JAXB.unmarshal(sendRequest(DCNS.IMAGES + "/" + imageId, RequestType.GET), Image.class);
	}

	@Override
	public List<Instance> listInstances() throws DeltaCloudClientException 
	{
		
		return listDeltaCloudObjects(Instance.class, DCNS.INSTANCES.toString(), "instance");
	}

	@Override
	public Instance listInstances(String instanceId) throws DeltaCloudClientException 
	{
		return buildInstance(sendRequest(DCNS.INSTANCES + "/" + instanceId, RequestType.GET));
	}
	
	@Override
	public List<Realm> listRealms() throws DeltaCloudClientException 
	{
		return listDeltaCloudObjects(Realm.class, DCNS.REALMS.toString(), "realm");
	}

	@Override
	public Realm listRealms(String realmId) throws DeltaCloudClientException 
	{
		return JAXB.unmarshal(sendRequest(DCNS.REALMS + "/" + realmId, RequestType.GET), Realm.class);
	}

	@Override
	public void rebootInstance(String instanceId) throws DeltaCloudClientException
	{
		sendRequest(DCNS.INSTANCES + "/" + instanceId + DCNS.REBOOT, RequestType.GET);
	}

	@Override
	public void shutdownInstance(String instanceId) throws DeltaCloudClientException 
	{
		sendRequest(DCNS.INSTANCES + "/" + instanceId + DCNS.STOP, RequestType.GET);
	}
	
	@Override
	public void startInstance(String instanceId) throws DeltaCloudClientException 
	{
		sendRequest(DCNS.INSTANCES + "/" + instanceId + DCNS.START, RequestType.GET);
	}
	
	public void destroyInstance(String instanceId) throws DeltaCloudClientException
	{
		sendRequest(DCNS.INSTANCES + "/" + instanceId + DCNS.DESTROY, RequestType.GET);
	}
	
	private Instance buildInstance(String xml)
	{
		try
		{
			Instance instance = JAXB.unmarshal(new StringReader(xml), Instance.class);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new InputSource(new StringReader(xml)));
					
			instance.setImageId(getIdFromHref(getAttributeValues(document, "image", "href").get(0)));
			instance.setFlavorId(getIdFromHref(getAttributeValues(document, "flavor", "href").get(0)));
			instance.setRealmId(getIdFromHref(getAttributeValues(document, "realm", "href").get(0)));
			
			ArrayList<Instance.Action> actions = new ArrayList<Instance.Action>();
			for(String s : getAttributeValues(document, "link", "rel"))
			{
				actions.add(Instance.Action.valueOf(s.toUpperCase()));
			}
			instance.setActions(actions);
			
			return instance;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private List<String> getAttributeValues(Document document, String elementName, String attributeName)
	{
		NodeList elements = document.getElementsByTagName(elementName);
		ArrayList<String> values = new ArrayList<String>();
		for(int i = 0; i < elements.getLength(); i++)
		{
			values.add(elements.item(i).getAttributes().getNamedItem(attributeName).getTextContent());
		}
		return values;
	}
	
	private String getIdFromHref(String href)
	{
		return href.substring(href.lastIndexOf("/") + 1, href.length());
	}
	
	private <T extends DeltaCloudObject> List<T> listDeltaCloudObjects(Class<T> clazz, String path, String elementName) throws DeltaCloudClientException 
	{
		try
		{
			InputSource is = new InputSource(new StringReader(sendRequest(path, RequestType.GET)));
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(is);
			
			document.getElementsByTagName(path).toString(); 
						
			ArrayList<T> dco = new ArrayList<T>();
			
			NodeList nodeList = document.getElementsByTagName(elementName);
			for(int i = 0; i < nodeList.getLength(); i ++)
			{
				dco.add(buildDeltaCloudObject(clazz, nodeList.item(i)));
			}
			return dco;
		}
		catch(Exception e)
		{
			throw new DeltaCloudClientException("Could not list object of type " + clazz, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Object> T buildDeltaCloudObject(Class<T> clazz, Node node) throws DeltaCloudClientException
	{
		if(clazz.equals(Instance.class))
		{
			return (T) buildInstance(nodeToString(node));
		}
		else
		{
			return JAXB.unmarshal(new StringReader(nodeToString(node)), clazz);
		}
	}
	
	private String nodeToString(Node node) throws DeltaCloudClientException
	{
		try 
		{
			StringWriter writer = new StringWriter();
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.transform(new DOMSource(node), new StreamResult(writer));
			return writer.toString();
		} 
		catch (TransformerException e) 
		{
			throw new DeltaCloudClientException("Error transforming node to string", e);
		}
		
	}

}

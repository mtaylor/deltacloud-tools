package org.deltacloud.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.deltacloud.HardwareProfile;
import org.deltacloud.HardwareProfiles;
import org.deltacloud.Image;
import org.deltacloud.Images;
import org.deltacloud.Instance;
import org.deltacloud.Instances;
import org.deltacloud.ObjectFactory;
import org.deltacloud.Realm;
import org.deltacloud.Realms;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMSource;

public class DeltaCloudClient implements API
{
	public static Namespace NS = Namespace.getNamespace("dc", "http://www.deltacloud.org/deltacloud.xsd");

	public static Logger logger = Logger.getLogger(DeltaCloudClient.class);

	private Unmarshaller unmarshaller;

	private static enum DCNS
	{ 
		INSTANCES, REALMS, IMAGES, HARDWARE_PROFILES, START, STOP, REBOOT, DESTROY, INSTANCE_STATES;
		
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

	public DeltaCloudClient(URL url, String username, String password) throws DeltaCloudClientException
	{
		try
		{
			logger.debug("Creating new Delta Cloud Client for Server: " + url);

			this.baseUrl = url;
			this.username = username;
			this.password = password;

			JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());

			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(null);
		}
		catch(Exception e)
		{
			throw new DeltaCloudClientException("Error Creating Client", e);
		}
	}

	@Override
	public Instance createInstance(String imageId, String hardwareProfileId, String realmId, String name) throws DeltaCloudClientException 
	{
		String query = "?image_id=" + imageId + "&hardware_profile_id=" + hardwareProfileId + "&realm_id=" + realmId + "&name=" + name + "&commit=create";
		return unmarshall(sendRequest(DCNS.INSTANCES + query, RequestType.POST), Instance.class);
	}

	@Override
	public HardwareProfile listHardwareProfile(String hardwareProfileId) throws DeltaCloudClientException 
	{
		String request = DCNS.HARDWARE_PROFILES + "/" + hardwareProfileId;
		return (HardwareProfile) unmarshall(sendRequest(request, RequestType.GET), HardwareProfile.class);
	}

	@Override
	public List<HardwareProfile> listHardwareProfiles() throws DeltaCloudClientException 
	{
		String request = DCNS.HARDWARE_PROFILES.toString();
		HardwareProfiles hwps = unmarshall(sendRequest(request, RequestType.GET), HardwareProfiles.class);
		return hwps.getHardwareProfile();
	}
	
	@Override
	public List<Image> listImages() throws DeltaCloudClientException 
	{
		String request = DCNS.IMAGES.toString();
		Images images = unmarshall(sendRequest(request, RequestType.GET), Images.class);
		return images.getImage();
	}

	@Override
	public Image listImages(String imageId) throws DeltaCloudClientException 
	{
		String request = DCNS.IMAGES + "/" + imageId;
		return unmarshall(sendRequest(request, RequestType.GET), Image.class);
	}

	@Override
	public List<Instance> listInstances() throws DeltaCloudClientException 
	{
		String request = DCNS.INSTANCES.toString();
		Instances instances = unmarshall(sendRequest(request, RequestType.GET), Instances.class);
		for(Instance i : instances.getInstance())
		{
			populateInstanceObjects(i);
		}
		return instances.getInstance();
	}

	@Override
	public Instance listInstances(String instanceId) throws DeltaCloudClientException 
	{
		String request = DCNS.INSTANCES + "/" + instanceId;
		Instance instance = unmarshall(sendRequest(request, RequestType.GET), Instance.class);
		populateInstanceObjects(instance);
		return instance;
	}
	
	@Override
	public List<Realm> listRealms() throws DeltaCloudClientException 
	{
		String request = DCNS.REALMS.toString();
		Realms realms = unmarshall(sendRequest(request, RequestType.GET), Realms.class);
		return realms.getRealm();
	}

	@Override
	public Realm listRealms(String realmId) throws DeltaCloudClientException 
	{
		String request = DCNS.REALMS + "/" + realmId;
		return unmarshall(sendRequest(request, RequestType.GET), Realm.class);
	}
	
	@Override
	public Instance createInstance(String imageId) throws DeltaCloudClientException 
	{
		String query = "?image_id=" + imageId;
		Instance instance = unmarshall(sendRequest(DCNS.INSTANCES + query, RequestType.POST), Instance.class);
		populateInstanceObjects(instance);
		return instance;
	}

	@Override
	public boolean performInstanceAction(String instanceId, String action) throws DeltaCloudClientException
	{
		Instance instance = listInstances(instanceId);
		if(instance.getActionNames().contains(action))
		{
			String request = DCNS.INSTANCES + "/" + instanceId + "/" + action;
			sendRequest(request, RequestType.POST);
			return true;
		}
		return false;
	}
	
	private InputStream sendRequest(String path, RequestType requestType) throws DeltaCloudClientException
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
			
			if(entity != null)
			{
				return entity.getContent();
			}
		}
		catch(IOException e)
		{
			logger.error("Error processing request to: " + requestUrl, e);
			throw new DeltaCloudClientException("Error processing request to: " + requestUrl, e);
		}
		throw new DeltaCloudClientException("Could not execute request to:" + requestUrl);
	}

	private void populateInstanceObjects(Instance instance) throws DeltaCloudClientException
	{
		instance.setHardwareProfile(listHardwareProfile(instance.getHardwareProfile().getId()));
		instance.setRealm(listRealms(instance.getRealm().getId()));
		instance.setImage(listImages(instance.getImage().getId()));
	}

	private <T> T unmarshall(InputStream is, Class<T> type) throws DeltaCloudClientException
	{
		try
		{
			SAXBuilder sb = new SAXBuilder(false);
            Document doc = sb.build(is);
            logger.debug(docToString(doc));

            addNamespace(doc.getRootElement());

            Source src = new JDOMSource(doc);
            return unmarshaller.unmarshal(src, type).getValue();
		}
		catch(Exception e)
		{
			throw new DeltaCloudClientException("Error Parsing response", e);
		}
	}

    private void addNamespace(Element elem) 
    {
        elem.setNamespace(NS);
        for(Object attribute : elem.getAttributes())
        {
        	((Attribute) attribute).setNamespace(NS);
        }

        for (Object element : elem.getChildren()) 
        {
        	addNamespace((Element) element);
        }
    }
	
    private static String docToString(Document document) throws IOException
    {
        XMLOutputter out = new XMLOutputter();
        StringWriter sw = new StringWriter();

        out.output(document, sw);
        return sw.toString();
    }
}

package org.jboss.deltacloud.client.examples;
import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jboss.deltacloud.client.DeltaCloudClient;
import org.jboss.deltacloud.client.DeltaCloudClientException;
import org.jboss.deltacloud.client.Flavor;
import org.jboss.deltacloud.client.Image;
import org.jboss.deltacloud.client.Instance;
import org.jboss.deltacloud.client.Realm;


public class Driver 
{
	public static Logger logger = Logger.getLogger(Driver.class);
	
	public static void main(String[] args) throws Exception
	{		
		String username = "mockuser";
		
		String password = "mockpassword";
		
		URL deltaCloudURL = new URL("http://localhost:3001/api");
		
		DeltaCloudClient client = new DeltaCloudClient(deltaCloudURL, username, password);

		listRealms(client);
		
		listFlavors(client);
		
		listImages(client);
		
		createInstance(client);
		
		listInstances(client);
	}
	
	private static void listFlavors(DeltaCloudClient client) throws DeltaCloudClientException
	{
		System.out.println(" ==== Flavors ====");
		for(Flavor flavor : client.listFlavors())
		{
			System.out.println(flavor.toString());
		}
	}
	
	private static void listImages(DeltaCloudClient client) throws DeltaCloudClientException
	{
		System.out.println(" ==== Images ====");
		for(Image image : client.listImages())
		{
			System.out.println(image.toString());
		}
	}
	
	private static void listRealms(DeltaCloudClient client) throws DeltaCloudClientException
	{
		System.out.println(" ==== Realms ====");
		for(Realm realm : client.listRealms())
		{
			System.out.println(realm.toString());
		}
	}
		
	private static void createInstance(DeltaCloudClient client) throws DeltaCloudClientException
	{
		Image image = client.listImages().get(0);
		String imageId = image.getId();
		
		client.createInstance(imageId);
	}
	
	private static void listInstances(DeltaCloudClient client) throws DeltaCloudClientException
	{
		for(Instance instance : client.listInstances())
		{
			System.out.println(instance.toString());
		}
	}
}

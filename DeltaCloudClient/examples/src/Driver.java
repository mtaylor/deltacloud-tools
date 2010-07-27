import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.deltacloud.HardwareProfile;
import org.deltacloud.Image;
import org.deltacloud.Instance;
import org.deltacloud.Link;
import org.deltacloud.Property;
import org.deltacloud.Realm;
import org.deltacloud.client.DeltaCloudClient;
import org.deltacloud.client.DeltaCloudClientException;

public class Driver 
{	
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
		
		// Reboot an Instance
		performInstanceAction(client, "reboot");
		
		// Stop an Instance
		performInstanceAction(client, "stop");
		
		// Start an Instance
		performInstanceAction(client, "start");
		
		//Terminate an Instance
		performInstanceAction(client, "destroy");
		
		listInstances(client);
	}

	private static void createInstance(DeltaCloudClient client) throws DeltaCloudClientException
	{
		Image image = client.listImages().get(0);
		String imageId = image.getId();
		client.createInstance(imageId);
	}

	private static void performInstanceAction(DeltaCloudClient client, String action) throws DeltaCloudClientException
	{
		for(Instance i : client.listInstances())
		{
			if(i.getActionNames().contains(action))
			{
				System.out.println("Before Instance State: ");
				printInstance(client.listInstances(i.getId()));
				
				System.out.println("Performing Action: " + action + " on instnace: " + i.getId());
				client.performInstanceAction(i.getId(), action);
				
				System.out.println("After Instnace State: ");
				printInstance(client.listInstances(i.getId()));
				return;
			}
		}
		System.out.println("No instance found that can perform action: " + action);
	}

	private static void listFlavors(DeltaCloudClient client) throws DeltaCloudClientException
	{
		System.out.println(" ==== Hardware Profiles ====");
		for(HardwareProfile hwp : client.listHardwareProfiles())
		{
			System.out.println("Hardware Profile: " + hwp.getId());
			for(Property property : hwp.getProperty())
			{
				System.out.println("\t" + property.getName() + " " + property.getValue() + " " + property.getUnit());
			}
		}
 	}
	
	private static void listImages(DeltaCloudClient client) throws DeltaCloudClientException
	{
		System.out.println(" ==== Hardware Profiles ====");
		for(Image image : client.listImages())
		{
			System.out.println("Image: " + image.getId());
			System.out.println("\tName: " + image.getName());
			System.out.println("\tArch: " + image.getArchitecture());
			System.out.println("\tDesc: " + image.getDescription());
			System.out.println("\tOwner: " + image.getOwnerId());
			System.out.println("\tHRef: " + image.getHref());
		}
	}
	
	private static void listRealms(DeltaCloudClient client) throws DeltaCloudClientException
	{
		System.out.println(" ==== Realms ====");
		for(Realm realm : client.listRealms())
		{
			System.out.println("Realm: " + realm.getId());
			System.out.println("\tName: " + realm.getName());
			System.out.println("\tState: " + realm.getState());
			System.out.println("\tLimit: " + realm.getLimit());
			System.out.println("\tHref: " + realm.getHref());
		}
	}
	
	private static void printInstance(Instance instance)
	{
		System.out.println("Instance: " + instance.getId());
		System.out.println("\tName: " + instance.getName());
		System.out.println("\tOwnerId: " + instance.getOwnerId());
		System.out.println("\tImage: " + instance.getImage().getId());
		System.out.println("\tRealm: " + instance.getRealm().getId());
		System.out.println("\tHardwareProfile: " + instance.getHardwareProfile().getId());
		System.out.println("\tState: " + instance.getState());
		System.out.println("\tPublic Addresses:");
		for(String address : instance.getPublicAddresses().getAddresses())
		{
			System.out.println("\t\t" + address);
		}

		System.out.println("\tPrivate Addresses:");
		for(String address : instance.getPrivateAddresses().getAddresses())
		{
			System.out.println("\t\t" + address);
		}
		
		System.out.println("\tActions:");
		for(Link link : instance.getActions().getLink())
		{
			System.out.println("\t\tMethod: " + link.getMethod() + ", Rel: " + link.getRel() + ", Href: " + link.getHref());
		}
	}

	private static void listInstances(DeltaCloudClient client) throws DeltaCloudClientException
	{
		for(Instance instance : client.listInstances())
		{
			printInstance(instance);
		}
	}
}

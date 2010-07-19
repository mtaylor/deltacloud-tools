package org.deltacloud.client;

import java.util.List;

public interface API 
{
	/**
	 * Returns a list of Delta Cloud Realms
	 * @return List of Delta Cloud Realms
	 * @throws DeltaCloudClientException
	 */
	public List<Realm> listRealms() throws DeltaCloudClientException;
	
	/**
	 * Returns a single Delta Cloud Realm given its ID
	 * @param realmId
	 * @return Delta Cloud Realm
	 * @throws DeltaCloudClientException
	 */
	public Realm listRealms(String realmId) throws DeltaCloudClientException;
	
	public List<Flavor> listFlavors() throws DeltaCloudClientException;
	
	/**
	 * Returns a Delta Cloud Flavors
	 * @param flavorId
	 * @return Delta Cloud Flavor
	 * @throws DeltaCloudClientException
	 */
	public Flavor listFlavor(String flavorId) throws DeltaCloudClientException;
	
	/**
	 * Returns a List of Delta Cloud Images 
	 * @return List of Delta Cloud Images
	 * @throws DeltaCloudClientException
	 */
	public List<Image> listImages() throws DeltaCloudClientException;
	
	/**
	 * Returns a Delta Cloud Image given its ID
	 * @param imageId
	 * @return
	 * @throws DeltaCloudClientException
	 */
	public Image listImages(String imageId) throws DeltaCloudClientException;
	
	/**
	 * Returns a list of all Instances from the Delta Cloud Provider
	 * @return
	 * @throws DeltaCloudClientException
	 */
	public List<Instance> listInstances() throws DeltaCloudClientException;
	
	/**
	 * Returns an Instance from the Delta Cloud Provider given on the Instances ID
	 * @param instanceId
	 * @return
	 * @throws DeltaCloudClientException
	 */
	public Instance listInstances(String instanceId) throws DeltaCloudClientException;
	
	/**
	 * Creates a new Delta Cloud Instance based on the Image specified by the Image ID.  Default parameters are used for the
	 * Flavor, Realm and Name.  These are specified by the Delta Cloud Provider
	 * @param imageId 
	 * @return The newly created Delta Cloud Instance
	 * @throws DeltaCloudClientException
	 */
	public Instance createInstance(String imageId) throws DeltaCloudClientException;
	
	/**
	 * Creates a new Delta Cloud Instance, the instance will be based on the Image specified by the instance ID.  It will be of 
	 * type flavor and in the location realm
	 * @param imageId
	 * @param flavor
	 * @param realm
	 * @param name
	 * @return
	 * @throws DeltaCloudClientException
	 */
	public Instance createInstance(String imageId, String flavor, String realm, String name) throws DeltaCloudClientException;
	
	/**
	 * Starts a previously stopped Instance given the Instance ID
	 * @param instanceId
	 * @throws DeltaCloudClientException
	 */
	public void startInstance(String instanceId) throws DeltaCloudClientException;
	
	/**
	 * Reboots a running Instance
	 * @param instanceId
	 * @throws DeltaCloudClientException
	 */
	public void rebootInstance(String instanceId) throws DeltaCloudClientException;
	
	/** Shuts down a running instance
	 * @param instanceId
	 * @throws DeltaCloudClientException
	 */
	public void shutdownInstance(String instanceId) throws DeltaCloudClientException;
	
	/** Destroys a previously Stopped Instance
	 * 
	 * @param instanceId
	 * @throws DeltaCloudClientException
	 */
	public void destroyInstance(String instanceId) throws DeltaCloudClientException;
}

package org.deltacloud.client;

import java.util.List;

import org.deltacloud.HardwareProfile;
import org.deltacloud.Image;
import org.deltacloud.Instance;
import org.deltacloud.Realm;

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
	
	public List<HardwareProfile> listHardwareProfiles() throws DeltaCloudClientException;
	
	/**
	 * Returns a Delta Cloud HardwareProfile
	 * @param hardware profile Id
	 * @return Delta Cloud HardwareProfile
	 * @throws DeltaCloudClientException
	 */
	public HardwareProfile listHardwareProfile(String hardwareProfileId) throws DeltaCloudClientException;
	
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
	 * HardwareProfile, Realm and Name.  These are specified by the Delta Cloud Provider
	 * @param imageId 
	 * @return The newly created Delta Cloud Instance
	 * @throws DeltaCloudClientException
	 */
	public Instance createInstance(String imageId) throws DeltaCloudClientException;
	
	/**
	 * Creates a new Delta Cloud Instance, the instance will be based on the Image specified by the instance ID.  It will be of 
	 * type hardwareProfile and in the location realm
	 * @param imageId
	 * @param hardwareProfile
	 * @param realm
	 * @param name
	 * @return
	 * @throws DeltaCloudClientException
	 */
	public Instance createInstance(String imageId, String hardwareProfile, String realm, String name) throws DeltaCloudClientException;
	

	/**
	 * Performs an action on an instance, given the String action.  The available actions can be obtained from the 
	 * getAvailableActions() method
	 * @returns true if the action is available, false if it is not
	 * @param instanceId
	 * @throws DeltaCloudClientException in the event of a failure
	 */
	public boolean performInstanceAction(String instanceId, String action) throws DeltaCloudClientException;
}

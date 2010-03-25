package org.jboss.deltacloud.client;

import javax.xml.bind.annotation.XmlElement;

public class Image extends DeltaCloudObject
{	
	private static final long serialVersionUID = 1L;

	@XmlElement(name="owner_id")
	private String ownerId;
	
	@XmlElement
	private String name;
	
	@XmlElement
	private String description;
	
	@XmlElement
	private String architecture;
	
	private Image()
	{
	}
		
	@SuppressWarnings("unused")
	private void setOwnerId(String ownerId)
	{
		this.ownerId = ownerId;
	}

	@SuppressWarnings("unused")
	private void setName(String name)
	{
		this.name = name;
	}

	@SuppressWarnings("unused")
	private void setDescription(String description)
	{
		this.description = description;
	}

	@SuppressWarnings("unused")
	private void setArchitecture(String architecture)
	{
		this.architecture = architecture;
	}
	
	public String getOwnerId() 
	{
		return ownerId;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String getArchitecture()
	{
		return architecture;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		s += "Image:\t\t" + getId() + "\n";
		s += "Owner:\t\t" + getOwnerId() + "\n";
		s += "Name:\t\t" + getName() + "\n";
		s += "Desc:\t\t" + getDescription() + "\n";
		s += "Arch:\t\t" + getArchitecture() + "\n";
		return s;
	}
}

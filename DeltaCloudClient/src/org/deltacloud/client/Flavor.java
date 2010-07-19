package org.deltacloud.client;

import javax.xml.bind.annotation.XmlElement;

public class Flavor extends DeltaCloudObject
{
	private static final long serialVersionUID = 1L;

	@XmlElement
	private String architecture;
	
	@XmlElement
	private String memory;
	
	@XmlElement
	private String storage;
	
	private Flavor()
	{	
	}

	@SuppressWarnings("unused")
	private void setArchitecture(String architecture)
	{
		this.architecture = architecture;
	}

	@SuppressWarnings("unused")
	private void setMemory(String memory)
	{
		this.memory = memory;
	}

	@SuppressWarnings("unused")
	private void setStorage(String storage)
	{
		this.storage = storage;
	}

	public String getArchitecture()
	{
		return architecture;
	}

	public String getMemory()
	{
		return memory;
	}

	public String getStorage()
	{
		return storage;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		s += "Flavor:\t\t" + getId() + "\n";
		s += "Arch:\t\t" + getArchitecture() + "\n";
		s += "Memory:\t\t" + getMemory() + "\n";
		s += "Storage:\t" + getStorage() + "\n";
		return s;
	}
}

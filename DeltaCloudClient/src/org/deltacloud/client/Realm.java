package org.deltacloud.client;

import javax.xml.bind.annotation.XmlElement;

public class Realm extends DeltaCloudObject
{
	private static final long serialVersionUID = 1L;

	@XmlElement
	private String name;
	
	@XmlElement
	private String state;
	
	@XmlElement
	private int limit;
		
	private Realm() 
	{
	}

	@SuppressWarnings("unused")
	private void setName(String name)
	{
		this.name = name;
	}

	@SuppressWarnings("unused")
	private void setState(String state)
	{
		this.state = state;
	}

	@SuppressWarnings("unused")
	private void setLimit(int limit)
	{
		this.limit = limit;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public String getName()
	{
		return name;
	}

	public String getState()
	{
		return state;
	}

	public int getLimit()
	{
		return limit;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		s += "Realm:\t\t" + getId() + "\n";
		s += "Name\t\t" + getName()+ "\n";
		s += "State:\t\t" + getState() + "\n";
		s += "Limit:\t\t" + getLimit() + "\n";
		return s;
	}
	
	
}

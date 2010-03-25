package org.jboss.deltacloud.client;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public abstract class DeltaCloudObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@XmlElement
	protected String id;
	
	@SuppressWarnings("unused")
	private void setId(String id) 
	{
		this.id = id;
	}

	public String getId() 
	{
		return id;
	}
}

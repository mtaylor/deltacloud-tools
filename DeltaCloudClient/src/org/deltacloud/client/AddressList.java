package org.deltacloud.client;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class AddressList
{
	private static final long serialVersionUID = 1L;
	
	private AddressList()
	{
	}
	
	@XmlElement
	@XmlList
	private List<String> address;
	
	@SuppressWarnings("unused")
	private void setAddress(List<String> address)
	{
		this.address = address;
	}

	public List<String> getAddress()
	{
		return address;
	}
}

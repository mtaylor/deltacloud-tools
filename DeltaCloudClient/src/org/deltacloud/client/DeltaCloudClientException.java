package org.deltacloud.client;

public class DeltaCloudClientException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	public DeltaCloudClientException(String message, Throwable clause)
	{
		super(message, clause);
	}
	
	public DeltaCloudClientException(Throwable clause)
	{
		super(clause);
	}
	
	public DeltaCloudClientException(String message)
	{
		super(message);
	}
}

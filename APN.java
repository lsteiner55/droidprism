package com.droidprism;

import java.io.Serializable;

/**
 * The mms APN
 * 
 *
 */
public class APN implements Serializable {
	public static final long serialVersionUID = 1L;

	/**
    * mmsc
    */
	public String mmsc;
	
	/**
	 * mms proxy, set to "" if none for this apn
	 */
	public String proxy;
	
	
	public String name;
	
	/**
	 * mms port, set to 0 if none for this apn
	 */
	public int port;
	public String user;
	public String password;

	public APN(String mmsc, String proxy, int port, String name) {

		this.mmsc = mmsc;
		this.proxy = proxy;
		this.port = port;
		this.name = name;

	}

	public APN(APN apn) {
		this(apn.mmsc,apn.proxy,apn.port,apn.name);
		this.user = apn.user;
		this.password = apn.password;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof APN) {
			APN m = (APN) o;
			return this.mmsc.equals(m.mmsc) && this.proxy.equals(m.proxy)
					&& this.name.equals(m.name) && this.port == m.port;
		}
		return false;
	}

	@Override
	public int hashCode() {

		return new String(mmsc + proxy + port + name + user + password)
				.hashCode();
	}

	@Override
	public String toString() {

		return mmsc + "  " + proxy + "  " + port + "  " + name + "  " + user
				+ "  " + password;
	}

}
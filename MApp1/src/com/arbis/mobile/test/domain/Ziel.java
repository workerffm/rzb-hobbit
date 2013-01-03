package com.arbis.mobile.test.domain;

import com.arbis.mobile.tools.DoubleLineItem;

public class Ziel implements DoubleLineItem{
	private final String name, address;

	public Ziel(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public String getLine1() {
		return getName();
	}

	@Override
	public String getLine2() {
		return getAddress();
	}

}

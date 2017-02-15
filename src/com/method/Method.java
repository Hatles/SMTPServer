package com.method;

public abstract class Method
{
	protected String title;

	public Method(String title)
	{
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

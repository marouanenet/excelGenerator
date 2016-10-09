package com.msaidi;

import java.io.Serializable;

public class HeaderVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String label;

	public HeaderVo(final String id, final String label) {
		this.id = id;
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

}

package com.msaidi;

import java.io.Serializable;

public class EntityVo implements Serializable {
	private static final long serialVersionUID = -2983325761880437187L;

	public EntityVo(String id, String titre) {
		this.id = id;
		this.titre = titre;
	}

	private String id;
	private String titre;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
}

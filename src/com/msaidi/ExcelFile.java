package com.msaidi;

import java.util.ArrayList;
import java.util.List;

public class ExcelFile extends AbstractReportingBuilder<EntityVo> {

	// titre dans le fichier excel
	private static final String ID = "id";
	private static final String TITRE = "titre";

	// nom des attributs de l'entite vo
	private static final String ID_ID = "id";
	private static final String TITRE_ID = "titre";

	@Override
	protected List<HeaderVo> buildHeader() {
		List<HeaderVo> headerVos = new ArrayList<HeaderVo>();
		headerVos.add(new HeaderVo(ID_ID, ID));
		headerVos.add(new HeaderVo(TITRE_ID, TITRE));
		return headerVos;
	}

	@Override
	protected List<EntityVo> buildData() {
		List<EntityVo> entityVos = new ArrayList<EntityVo>();
		entityVos.add(new EntityVo("1", "titre1"));
		entityVos.add(new EntityVo("2", "titre2"));
		entityVos.add(new EntityVo("3", "titre3"));
		return entityVos;
	}

}

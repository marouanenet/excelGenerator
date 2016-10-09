package com.msaidi;

import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Classe abstraite qui trace les étapes essentielles pour générer un rapport
 * 
 * @author asayad
 * 
 */
public abstract class AbstractReportingBuilder<T extends Serializable> {

	private static final Logger LOG = Logger
			.getLogger(AbstractReportingBuilder.class);

	protected static final String DATE_FORMAT = "dd-MM-yyyy";
	protected static final String IGNORE = "";
	private static final String RAPPORTS_AUCUNE_DONNEE_INFO = "RAPPORTS.AUCUNE.DONNEE.INFO";

	// private static final String RAPPORTS_HEADER_NO_FOUND_ERROR =
	// "RAPPORTS.HEADER.NO.FOUND.ERROR";

	/**
	 * Construire l'entite du rapport
	 * 
	 * @return List<HeaderVo>
	 */
	protected abstract List<HeaderVo> buildHeader();

	/**
	 * Remplir le contenu du rapport
	 */
	protected abstract List<T> buildData();

	/**
	 * récupérer les valeurs de l'attribut header.getId() de l'objet
	 * {@code object} en param
	 * 
	 * @param object
	 * @param header
	 * 
	 * @return String[]
	 */
	protected String[] getValue(final Object object, final HeaderVo header) {
		PropertyDescriptor attributePd;

		try {
			attributePd = PropertyUtils.getPropertyDescriptor(object,
					header.getId());
			if (attributePd == null || attributePd.getReadMethod() == null) {
				throw new TechnicalException(
						"No getter found to get the value of the property "
								+ header.getId());
			}
			Object obj = attributePd.getReadMethod().invoke(object,
					new Object[0]);
			if (obj != null) {
				return new String[] { obj.toString() };
			}
			return new String[] { StringUtils.EMPTY };
		} catch (final IllegalAccessException e) {
			throw new TechnicalException(e.getMessage(), e);
		} catch (final InvocationTargetException e) {
			throw new TechnicalException(e.getMessage(), e);
		} catch (final NoSuchMethodException e) {
			throw new TechnicalException(e.getMessage(), e);
		}

	}

	/**
	 * Construire le rapport en se basant sur l'entête et le contenu construit
	 * 
	 * @param reponse
	 * @param intitule
	 * @throws IOException
	 */
	public String getExcel(final String intitule) throws IOException {

		// content
		List<T> content = buildData();
		if (content == null || content.isEmpty()) {
			return RAPPORTS_AUCUNE_DONNEE_INFO;
		}

		final Date today = new Date();
		final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		final String fileExportName = intitule + "_" + dateFormat.format(today)
				+ ".xls";
		// respondse passe en parametre dans le controleur
		// reponse.setHeader("Content-Disposition", "attachment; filename="
		// + fileExportName);
		// reponse.setContentType("application/Excel");
		// OutputStream outputStream = reponse.getOutputStream();
		OutputStream outputStream = new FileOutputStream("C:\\"
				+ fileExportName);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(intitule);
		HSSFRow row = null;

		// header
		List<HeaderVo> header = buildHeader();
		if (header != null && !header.isEmpty()) {
			row = sheet.createRow(0);
			for (int colonneIndex = 0; colonneIndex < header.size(); colonneIndex++) {
				HSSFCell cell = row.createCell(colonneIndex);
				cell.setCellValue(header.get(colonneIndex).getLabel());
			}

			String[] value = new String[header.size()];
			for (int objIndex = 0; objIndex < content.size(); objIndex++) {
				int j = 0;

				// créer une ligne
				row = sheet.createRow(objIndex + 1);
				for (int i = 0; i < header.size(); i++) {

					if (!IGNORE.equalsIgnoreCase(header.get(i).getId())) {
						value = getValue(content.get(objIndex), header.get(i));
					}

					if (value != null && value.length > 0) {

						// créer une case (colonne)
						HSSFCell cell = row.createCell(i);
						cell.setCellValue(value[j]);
						j++;
						if (value.length == j) {
							j = 0;
							value = null;
						}
					}
				}
			}

			// générer le fichier excel

			wb.write(outputStream);
			outputStream.flush();
		} else {
			LOG.info("No header found to generate the report ...");
			// return RAPPORTS_HEADER_NO_FOUND_ERROR;
		}

		return StringUtils.EMPTY;
	}
}

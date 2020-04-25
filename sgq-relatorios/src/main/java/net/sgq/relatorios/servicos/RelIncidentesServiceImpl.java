package net.sgq.relatorios.servicos;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sgq.relatorios.clientes.GestaoIncidentesClient;
import net.sgq.relatorios.clientes.to.IncidenteTO;
import net.sgq.relatorios.modelos.enums.Periodo;

@Service
public class RelIncidentesServiceImpl implements RelIncidentesService {

	@Autowired
	private GestaoIncidentesClient incidentesClient;

	@Value("${sgq.relatorios.diretorio}")
	private String diretorioRelatorios;

	private Logger logger = LoggerFactory.getLogger(RelIncidentesServiceImpl.class);

	@PostConstruct
	public void postConstruct() {
		File dirRelatorios = new File(diretorioRelatorios);
		
		if(dirRelatorios.exists() || dirRelatorios.mkdirs()) {
			logger.debug("Diretório para relatorios alocado em: {}", diretorioRelatorios);
		} else {
			logger.error("Não foi possível criar/acessar diretório para relatórios: {}", diretorioRelatorios);
		}
	}
	
	@Override
	public File geraRelatorioPor(Periodo periodo) {

		Map<String, Object> params = new HashMap<>();
		params.put("PeriodoReportado", formataPeriodo(periodo));

		List<IncidenteTO> incidentes = incidentesClient.consultaIncidentesConcluidos();

		return geraRelatorio(incidentes, params);
	}

	@Override
	public File geraRelatorioPor(Date inicio, Date fim) {

		Map<String, Object> params = new HashMap<>();
		params.put("PeriodoReportado", formataPeriodoQualquer(inicio, fim));

		List<IncidenteTO> incidentes = incidentesClient.consultaIncidentesConcluidos();

		return geraRelatorio(incidentes, params);
	}

	private File geraRelatorio(List<IncidenteTO> incidentes, Map<String, Object> params) {

		try {

			File jrxml = ResourceUtils.getFile("classpath:relatorios/Incidentes.jrxml");
			JasperReport compiledReport = JasperCompileManager.compileReport(jrxml.getAbsolutePath());

			JRBeanCollectionDataSource beanDS = new JRBeanCollectionDataSource(incidentes);
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, params, beanDS);

			final String arqRelatorio = String.format("%s/rel_incidente_%d.pdf", diretorioRelatorios, System.currentTimeMillis());

			JasperExportManager.exportReportToPdfFile(jasperPrint, arqRelatorio);
			
			return new File(arqRelatorio);

		} catch (FileNotFoundException | JRException e) {
			logger.info("Erro ao gerar relatório: {}", e.getMessage(), e);
		}

		return null;

	}

	private Object formataPeriodo(Periodo periodo) {

		Calendar cal = Calendar.getInstance();
		Date fimPeriodo = new Date();
		Date inicioPeriodo = null;

		switch (periodo) {
		case MES_ATUAL:
			cal.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case SEMESTRE:
			cal.add(Calendar.MONTH, -6);
			break;
		case DOZE_MESES:
			cal.add(Calendar.MONTH, -12);
			break;
		case ANO_CORRENTE:
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.MONTH, 0);
		default:
			break;
		}

		inicioPeriodo = cal.getTime();
		return formataPeriodoQualquer(inicioPeriodo, fimPeriodo);
	}

	private String formataPeriodoQualquer(Date inicioPeriodo, Date fimPeriodo) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		return String.format("%s a %s", sdf.format(inicioPeriodo), sdf.format(fimPeriodo));
	}

}

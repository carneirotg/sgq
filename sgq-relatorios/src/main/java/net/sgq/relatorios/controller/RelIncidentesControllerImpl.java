package net.sgq.relatorios.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.relatorios.modelos.enums.Periodo;
import net.sgq.relatorios.servicos.RelIncidentesService;
import net.sgq.relatorios.utils.GeracaoRelatorioException;

@RestController
@RequestMapping("/relatorios/incidentes")
public class RelIncidentesControllerImpl implements RelIncidentesController {

	@Autowired
	private RelIncidentesService relService;

	private Logger logger = LoggerFactory.getLogger(RelIncidentesControllerImpl.class);

	@Override
	@GetMapping
	public ResponseEntity<byte[]> incidentesMesAtual() {
		return geraDownload(relService.geraRelatorioPor(Periodo.MES_ATUAL), "Incidentes-Mes_Atual.pdf");
	}

	@Override
	@GetMapping({"/semestre", "ultimos-seis-meses"})
	public ResponseEntity<byte[]> incidentesSemestre() {
		return geraDownload(relService.geraRelatorioPor(Periodo.SEMESTRE), "Incidentes-Seis_Meses.pdf");
	}

	@Override
	@GetMapping("/ano-corrente")
	public ResponseEntity<byte[]> incidentesAnoCorrente() {
		return geraDownload(relService.geraRelatorioPor(Periodo.ANO_CORRENTE), "Incidentes-Ano.pdf");
	}

	@Override
	@GetMapping("/ultimos-doze-meses")
	public ResponseEntity<byte[]> incidentesUltimosDozeMeses() {
		return geraDownload(relService.geraRelatorioPor(Periodo.DOZE_MESES), "Incidentes-Doze_Meses.pdf");
	}

	@Override
	@GetMapping("/de/{inicio}/ate/{fim}")
	public ResponseEntity<byte[]> incidentesPorPeriodo(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fim) {

		validaPeriodo(inicio, fim);

		return geraDownload(relService.geraRelatorioPor(inicio, fim), "Incidentes-Por_Periodo.pdf");
	}

	private ResponseEntity<byte[]> geraDownload(File relatorio, String nomeRelatorio){
		
		if(relatorio == null) {
			throw new GeracaoRelatorioException();
		}
		
		try {
			
			HttpHeaders downloadHeaders = new HttpHeaders();
			downloadHeaders.add("Content-Disposition", "attachment; filename=" + nomeRelatorio);
			downloadHeaders.add("Content-Length", "" + relatorio.length());
			
			ResponseEntity<byte[]> relatorioDownload = ResponseEntity.ok()
					.headers(downloadHeaders)
					.body(IOUtils.toByteArray(new FileInputStream(relatorio)));
			
			return relatorioDownload;
		} catch (IOException e) {
			logger.error("Erro gerando byte array para download de relatório: {}", e.getMessage(), e);
			throw new GeracaoRelatorioException();
		}
		
	}

	private void validaPeriodo(Date inicio, Date fim) {
		if (Period.between(toLocalDate(inicio), toLocalDate(fim)).getYears() >= 1) {
			throw new IllegalStateException("Período maior que 12 meses");
		} else if(inicio.after(fim)) {
			throw new IllegalStateException("Data de início maior que a de fim");
		}
	}

	private LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

}

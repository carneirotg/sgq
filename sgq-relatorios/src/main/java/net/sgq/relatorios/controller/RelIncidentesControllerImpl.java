package net.sgq.relatorios.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sgq.relatorios.modelos.enums.Periodo;
import net.sgq.relatorios.servicos.RelIncidentesService;

@RestController
@RequestMapping("/relatorios/incidentes")
public class RelIncidentesControllerImpl implements RelIncidentesController {

	@Autowired
	private RelIncidentesService relService;
	
	@Override
	@GetMapping
	public Object incidentesMesAtual() {
		relService.geraRelatorioPor(Periodo.MES_ATUAL);
		return null;
	}

	@Override
	@GetMapping("/semestre")
	public Object incidentesSemestre() {
		relService.geraRelatorioPor(Periodo.SEMESTRE);
		return null;
	}

	@Override
	@GetMapping("/ano-corrente")
	public Object incidentesAnoCorrente() {
		relService.geraRelatorioPor(Periodo.ANO_CORRENTE);
		return null;
	}

	@Override
	@GetMapping("/ultimos-doze-meses")
	public Object incidentesUltimosDozeMeses() {
		relService.geraRelatorioPor(Periodo.DOZE_MESES);
		return null;
	}

	@Override
	@GetMapping("/de/{inicio}/ate/{fim}")
	public Object incidentesPorPeriodo(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date fim) {
		
		validaPeriodo(inicio, fim);
		
		relService.geraRelatorioPor(inicio, fim);
		
		return null;
	}

	private void validaPeriodo(Date inicio, Date fim) {
		if(Period.between(toLocalDate(inicio), toLocalDate(fim)).getYears() >= 1) {		
			throw new IllegalStateException("Período maior que 12 meses");
		}
	}

	private LocalDate toLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

}

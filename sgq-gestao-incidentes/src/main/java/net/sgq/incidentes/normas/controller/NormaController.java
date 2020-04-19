package net.sgq.incidentes.normas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import net.sgq.incidentes.conformidades.modelos.Norma;

public interface NormaController {

	ResponseEntity<List<Norma>> listaNormas();

	ResponseEntity<Norma> consultaNorma(Long id);

}
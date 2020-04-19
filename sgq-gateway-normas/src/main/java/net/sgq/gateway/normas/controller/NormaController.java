package net.sgq.gateway.normas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import net.sgq.gateway.normas.modelos.Norma;

public interface NormaController {

	ResponseEntity<List<Norma>> listaNormas();

	ResponseEntity<Norma> consultaNorma(Long id);

}
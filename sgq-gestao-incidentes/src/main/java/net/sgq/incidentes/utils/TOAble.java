package net.sgq.incidentes.utils;

public interface TOAble<T, TO, TOID> {

	TO toTO();
	TOID toTOId();
	
	T fromTO(TO to);
	T fromTOID(TOID toid);			
	
}

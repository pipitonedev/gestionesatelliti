package it.prova.gestionesatelliti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long> {
	
	List<Satellite> findAllByDataLancioLessThanAndStatoNot(Date data, StatoSatellite stato);
	
	List<Satellite> findAllByStatoLikeAndDataRientroIsNull(StatoSatellite stato);
	
	List<Satellite> findAllByDataLancioLessThanAndStatoEquals(Date data, StatoSatellite stato);

}

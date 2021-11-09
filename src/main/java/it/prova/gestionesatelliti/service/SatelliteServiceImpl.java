package it.prova.gestionesatelliti.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.repository.SatelliteRepository;

@Service
public class SatelliteServiceImpl implements SatelliteService {

	@Autowired
	private SatelliteRepository repository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> listAllElements() {
		return (List<Satellite>) repository.findAll();

	}

	@Override
	@Transactional(readOnly = true)
	public Satellite caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Satellite satelliteInstance) {
		repository.save(satelliteInstance);

	}

	@Override
	@Transactional
	public void inserisciNuovo(Satellite satelliteInstance) {
		repository.save(satelliteInstance);

	}

	@Override
	@Transactional
	public void rimuovi(Satellite satelliteInstance) {
		repository.delete(satelliteInstance);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> findByExample(Satellite example) {
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select s from Satellite s where s.id = s.id ");
		if (StringUtils.isNotEmpty(example.getDenominazione())) {
			whereClauses.add(" s.denominazione  like :denominazione ");
			paramaterMap.put("denominazione", "%" + example.getDenominazione() + "%");
		}
		if (StringUtils.isNotEmpty(example.getCodice())) {
			whereClauses.add(" s.codice like :codice ");
			paramaterMap.put("codice", "%" + example.getCodice() + "%");
		}
		if (example.getStato() != null) {
			whereClauses.add(" s.stato =:stato ");
			paramaterMap.put("stato", example.getStato());
		}
		if (example.getDataLancio() != null) {
			whereClauses.add("s.dataLancio >= :dataLancio ");
			paramaterMap.put("dataLancio", example.getDataLancio());
		}
		if (example.getDataRientro() != null) {
			whereClauses.add("s.dataRientro >= :dataRientro ");
			paramaterMap.put("dataRientro", example.getDataRientro());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Satellite> typedQuery = entityManager.createQuery(queryBuilder.toString(), Satellite.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> cercaLanciatiDaPiuDiDueAnniENonDisattivati() {
		Date data = new Date();
		data.setYear(data.getYear()-2);	
		return repository.findAllByDataLancioLessThanAndStatoNot(data, StatoSatellite.DISATTIVATO);	
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> cercaDisattivatiMaNonRientrati() {
		return repository.findAllByStatoLikeAndDataRientroIsNull(StatoSatellite.DISATTIVATO);
	}

}

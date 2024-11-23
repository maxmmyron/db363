package com.mmyron.db363.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mmyron.db363.entitiy.Station;
import com.mmyron.db363.entitiy.StationPK;

public interface StationRepo extends CrudRepository<Station, StationPK> {
	
//	@Query("""
//		SELECT name, trainRoute, loadingTime FROM Station
//		WHERE Station.trainRoute = trainRoute
//			""")
//	Iterable<Station> findAllByRoute(@Param("trainRoute") String route);
}

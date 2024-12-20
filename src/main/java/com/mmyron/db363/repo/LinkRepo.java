package com.mmyron.db363.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mmyron.db363.entitiy.Link;
import com.mmyron.db363.entitiy.LinkPK;

public interface LinkRepo extends CrudRepository<Link, LinkPK> {
	
//	@Query("""
//			SELECT * FROM Link WHERE (Link.origin_route = trainRoute AND Link.dest_route = trainRoute);
//			""")
//	public Iterable<Link> getAllByRoute(@Param("trainRoute") String route);
//	
//	@Query("""
//			SELECT * FROM Link WHERE (Link.origin_station = station);
//			""")
//	public Iterable<Link> getAllByOriginStation(@Param("station") String station);
//	
//	@Query("""
//			SELECT * FROM Link WHERE (Link.dest_station = station);
//			""")
//	public Iterable<Link> getAllByDestinationStation(@Param("station") String station);
}

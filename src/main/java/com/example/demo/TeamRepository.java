package com.example.demo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {

}

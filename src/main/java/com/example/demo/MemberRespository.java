package com.example.demo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRespository extends PagingAndSortingRepository<Member, Long> {

	Page<Member> findAllByTeamId(Long id, Pageable pageable);

}

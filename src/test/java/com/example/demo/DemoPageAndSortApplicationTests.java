package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class DemoPageAndSortApplicationTests {

	@Autowired
	TeamRepository teamRepository;

	@Autowired
	MemberRespository memberRepository;

	@Autowired
	Environment environment;

	@Test
	void contextLoads() {
		log.info("active profiles {}", environment.getActiveProfiles());
		log.info("default profiles {}", environment.getDefaultProfiles());
		log.info("property PWD {}", environment.getProperty("PWD"));
	}

	@Test
	void readManifest() throws IOException {
		var is = this.getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");

		var prop = new Properties();
		prop.load(is);
		var set = prop.entrySet();
		set.forEach(a -> {
			log.info("key {}, value {} ", a.getKey(), a.getValue());
		});

	}

	int teamMax = 12;
	int teamTotal = 0;

	int memberMax = 12;
	int memberTotal = 0;

	@Test
	void testPaging() {

		for (int i = 0; i < teamMax; i++) {
			var t = new Team();
			t.setName("team name " + (i + 1));
			var savedTeam = teamRepository.save(t);
			for (int j = 0; j < memberMax; j++) {
				var member = new Member();
				member.setName("member name " + (j + 1) + " in  " + savedTeam.getName());
				member.setTeamId(savedTeam.getId());
				memberRepository.save(member);
			}

		}

		getAllTeams();
		Assertions.assertEquals(teamMax, teamTotal);
		Assertions.assertEquals(memberMax * teamMax, memberTotal);
	}

	private void getAllTeams() {
		var teams = teamRepository.findAll(PageRequest.of(0, 2));
		getTeams(teams);
		for (int i = 1; i <= teams.getTotalPages(); i++) {
			var teamsRest = teamRepository.findAll(PageRequest.of(i, 2));
			getTeams(teamsRest);
		}
	}

	private void getTeams(Page<Team> teamsRest) {
		for (var t : teamsRest) {
			log.info(t.getName());
			teamTotal++;

			getAllMembers(t);

		}
	}

	private void getAllMembers(Team t) {
		var members = memberRepository.findAllByTeamId(t.getId(), PageRequest.of(0, 2));
		getMembers(members);
		for (int i = 1; i <= members.getTotalPages(); i++) {
			var membersRest = memberRepository.findAllByTeamId(t.getId(), PageRequest.of(i, 2));
			getMembers(membersRest);
		}
	}

	private void getMembers(Page<Member> membersRest) {
		for (var m : membersRest) {
			log.info(m.getName());
			memberTotal++;

		}
	}
}

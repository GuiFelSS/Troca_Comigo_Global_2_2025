package br.com.fiap.globalSolution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching // <-- 1. HABILITA O CACHING
@EnableJpaAuditing // <-- 2. HABILITA O @CreatedDate E @LastModifiedDate NAS ENTIDADES
public class GlobalSolutionApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlobalSolutionApplication.class, args);
	}

}
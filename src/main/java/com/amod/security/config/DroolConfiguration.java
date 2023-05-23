package com.amod.security.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolConfiguration {

	private KieServices kieServices = KieServices.Factory.get();

	private KieFileSystem getKieFileSystem() {
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		kieFileSystem.write(ResourceFactory.newClassPathResource("offer.drl"));
		return kieFileSystem;
	}

	@Bean
	public KieContainer getKieContainer() {
		System.out.println("Container created..");
		getKieRepository();
		KieBuilder builder = kieServices.newKieBuilder(getKieFileSystem());
		builder.buildAll();
		KieModule kieModule = builder.getKieModule();
		return kieServices.newKieContainer(kieModule.getReleaseId());
	}

	private void getKieRepository() {
		final KieRepository kieRepository = kieServices.getRepository();
		kieRepository.addKieModule(new KieModule() {

			@Override
			public ReleaseId getReleaseId() {
				return kieRepository.getDefaultReleaseId();
			}
		});

	}

	@Bean
	public KieSession getKieSession() {
		System.out.println("Session created");
		return getKieContainer().newKieSession();
	}

}

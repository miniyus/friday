package com.miniyus.friday;


import com.miniyus.friday.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class HexagonalArchitectureTest {
    private final String basePackage = "com.miniyus.friday";
    private final String domainPackage = "domain";
    private final String applicationPackage = "application";

    @Test
    void validateRegistrationContextArchitecture() {
        String adapterPackage = "adapter";
        String configurationPackage = "common.config";

        HexagonalArchitecture.boundedContext(basePackage)
            .withDomainLayer(domainPackage)
            .withAdaptersLayer(adapterPackage)
            .incoming("in.rest")
            .outgoing("out.persistence")
            .and()
            .withApplicationLayer(applicationPackage)
            .services("service")
            .incomingPorts("port.in")
            .outgoingPorts("port.out")
            .and()
            .withConfiguration(configurationPackage)
            .check(new ClassFileImporter()
                .importPackages(basePackage + ".."));
    }

    @Test
    void testPackageDependencies() {
        noClasses()
            .that()
            .resideInAPackage(String.format("%s.%s..", basePackage, domainPackage))
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(String.format("%s.%s..", basePackage, applicationPackage))
            .check(new ClassFileImporter()
                .importPackages(basePackage + ".."));
    }
}

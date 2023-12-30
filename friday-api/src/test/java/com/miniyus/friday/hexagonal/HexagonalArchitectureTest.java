package com.miniyus.friday.hexagonal;

import com.miniyus.friday.hexagonal.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public abstract class HexagonalArchitectureTest {
    protected final String basePackage = "com.miniyus.friday";
    protected final String domainPackage = "domain";
    protected final String domain;

    protected HexagonalArchitectureTest(String domain) {
        this.domain = domain;
    }

    @Test
    void validateRegistrationContextArchitecture() {
        String adapterPackage = "adapter";
        String configurationPackage = "common.config";
        String applicationPackage = "application";
        String pkg = basePackage + "." + domain;
        HexagonalArchitecture.boundedContext(pkg)
            .withDomainLayer(domainPackage)
            .withAdaptersLayer(adapterPackage)
            .incoming("in")
            .outgoing("out")
            .and()
            .withApplicationLayer(applicationPackage)
            .services("service")
            .incomingPorts("port.in")
            .outgoingPorts("port.out")
            .and()
            .withConfiguration(configurationPackage)
            .check(new ClassFileImporter()
                .importPackages(pkg + ".."));
    }

    @Test
    void testPackageDependencies() {
        String pkg = basePackage + "." + domain;
        String applicationPackage = "application";
        noClasses()
            .that()
            .resideInAPackage(String.format("%s.%s..", pkg, domainPackage))
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(String.format("%s.%s..", pkg, applicationPackage))
            .check(new ClassFileImporter()
                .importPackages(pkg + ".."));
    }

}

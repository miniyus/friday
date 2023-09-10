package com.miniyus.friday;

import org.junit.jupiter.api.Test;
import com.miniyus.friday.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/10
 */
public class DependencyRuleTests {
    @Test
    public void validateRegistrationContextArchitecture() {
        HexagonalArchitecture.boundedContext("com.miniyus.friday.users")
                .withDomainLayer("domain")
                .withAdaptersLayer("adapter")
                .incoming("in.rest")
                .outgoing("out.persistence")
                .and()
                .withApplicationLayer("application")
                .services("service")
                .incomingPorts("port.in")
                .outgoingPorts("port.out")
                .and()
                .withConfiguration("configuration")
                .check(new ClassFileImporter()
                        .importPackages("com.miniyus.friday.users.."));
    }

    @Test
    public void testPackageDependencies() {
        noClasses()
                .that()
                .resideInAPackage("com.miniyus.friday.users.domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("com.miniyus.friday.users.application..")
                .check(new ClassFileImporter()
                        .importPackages("com.miniyus.friday.users.."));
    }
}

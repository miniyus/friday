package com.miniyus.friday;

import com.miniyus.friday.archunit.PackageParseUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.miniyus.friday.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/10
 */
public class DependencyRuleTests {
    static List<String> packages;
    static String HEXAGONAL_ROOT = "com.miniyus.friday.api";

    @BeforeAll
    public static void setPackages() {
        packages = PackageParseUtil.setPackages(HEXAGONAL_ROOT);
        System.out.println(packages);
    }


    @Test
    public void validateRegistrationContextArchitecture() {
        packages.forEach((pkg) ->
            HexagonalArchitecture.boundedContext(pkg)
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
                    .importPackages(pkg + ".."))
        );
    }

    @Test
    public void testPackageDependencies() {
        packages.forEach((pkg) ->
            noClasses()
                .that()
                .resideInAPackage(pkg + ".domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(pkg + ".application..")
                .check(new ClassFileImporter()
                    .importPackages(pkg + ".."))
        );
    }
}

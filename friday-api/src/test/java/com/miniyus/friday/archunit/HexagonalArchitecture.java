package com.miniyus.friday.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HexagonalArchitecture extends ArchitectureElement {

    private Adapter adapters;
    private Application application;
    private String configurationPackage;
    private final List<String> domainPackages = new ArrayList<>();

    public static HexagonalArchitecture boundedContext(String basePackage) {
        return new HexagonalArchitecture(basePackage);
    }

    public HexagonalArchitecture(String basePackage) {
        super(basePackage);
    }

    public Adapter withAdaptersLayer(String adaptersPackage) {
        this.adapters = new Adapter(this, fullQualifiedPackage(adaptersPackage));
        return this.adapters;
    }

    public HexagonalArchitecture withDomainLayer(String domainPackage) {
        this.domainPackages.add(fullQualifiedPackage(domainPackage));
        return this;
    }

    public Application withApplicationLayer(String applicationPackage) {
        this.application = new Application(fullQualifiedPackage(applicationPackage), this);
        return this.application;
    }

    public HexagonalArchitecture withConfiguration(String packageName) {
        this.configurationPackage = fullQualifiedPackage(packageName);
        return this;
    }

    private void domainDoesNotDependOnOtherPackages(JavaClasses classes) {
        denyAnyDependency(
            this.domainPackages, Collections.singletonList(adapters.basePackage), classes);
        denyAnyDependency(
            this.domainPackages, Collections.singletonList(application.basePackage), classes);
    }

    public void check(JavaClasses classes) {
        this.adapters.doesNotContainEmptyPackages();
        this.adapters.dontDependOnEachOther(classes);
        this.adapters.doesNotDependOn(this.configurationPackage, classes);
        this.application.doesNotContainEmptyPackages();
        this.application.doesNotDependOn(this.adapters.getBasePackage(), classes);
        this.application.doesNotDependOn(this.configurationPackage, classes);
        this.application.incomingAndOutgoingPortsDoNotDependOnEachOther(classes);
        this.domainDoesNotDependOnOtherPackages(classes);
    }
}

package com.miniyus.friday.archunit;

import com.tngtech.archunit.core.importer.ClassFileImporter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public interface PackageParseUtil {
    static List<String> setPackages(String packageRoot) {
        List<String> packages = new ArrayList<>();
        var classes = new ClassFileImporter().importPackages(packageRoot);
        int rootPackageDepth = packageRoot.split("\\.").length;

        classes.forEach(cls -> {
                var splitPackage = cls.getPackageName().split("\\.");
                StringBuilder domainRoot = new StringBuilder();
                for (int i = 0; i < (rootPackageDepth + 1); i++) {
                    if (i == 0) {
                        domainRoot.append(splitPackage[i]);
                    } else {
                        domainRoot.append(".").append(splitPackage[i]);
                    }
                }
                packages.add(domainRoot.toString());
            }
        );

        return new ArrayList<>(new HashSet<>(packages));
    }
}

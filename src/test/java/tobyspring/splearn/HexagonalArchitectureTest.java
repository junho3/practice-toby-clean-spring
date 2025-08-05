package tobyspring.splearn;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.DisplayName;

@AnalyzeClasses(packages = "tobyspring.splearn", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

    @ArchTest
    @DisplayName("헥사고날 아키텍처 테스트")
    void test1(JavaClasses classes) {
        Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("domain").definedBy("tobyspring.splearn.domain..")
                .layer("application").definedBy("tobyspring.splearn.application..")
                .layer("adapter").definedBy("tobyspring.splearn.adapter..")
                .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
                .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
                .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
                .check(classes);
    }
}

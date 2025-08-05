package tobyspring.learningtest.arcunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.junit.jupiter.api.DisplayName;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "tobyspring.learningtest.arcunit")
public class ArchUnitLearningTest {

    @ArchTest
    @DisplayName("Application 클래스를 의존하는 클래스는 application, adapter에만 존재해야 한다.")
    void test1 (JavaClasses classes) {
        classes().that().resideInAPackage("..application..")
                .should().onlyHaveDependentClassesThat().resideInAnyPackage("..application..", "..adapter..")
                .check(classes);
    }

    @ArchTest
    @DisplayName("Application 클래스는 adapter의 클래스를 의존하면 안된다.")
    void test2(JavaClasses classes) {
        noClasses().that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAPackage("..adapter..")
                .check(classes);
    }

    @ArchTest
    @DisplayName("Domain 클래스는 domain, java 패키지에만 의존해야한다.")
    void test3(JavaClasses classes) {
        classes().that().resideInAPackage("..domain..")
                .should().onlyDependOnClassesThat().resideInAnyPackage("..domain..", "java..")
                .check(classes);
    }
}

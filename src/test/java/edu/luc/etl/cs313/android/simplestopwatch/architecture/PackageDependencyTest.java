package edu.luc.etl.cs313.android.simplestopwatch.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeJars;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

/**
 * Architecture tests to verify structural constraints and design rules.
 *
 * @author laufer
 */
public class PackageDependencyTest {

  /**
   * Verifies that there are no cyclic dependencies between packages.
   * Package cycles make code harder to understand, test, and maintain.
   */
  @Test
  public void packages_should_be_free_of_cycles() {
    final var importedClasses =
        new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("edu.luc.etl.cs313.android.simplestopwatch");

    SlicesRuleDefinition.slices()
        .matching("edu.luc.etl.cs313.android.simplestopwatch.(*)..")
        .should()
        .beFreeOfCycles()
        .check(importedClasses);
  }

  /**
   * Verifies that there are no cyclic dependencies between sub-packages
   * at a more granular level (e.g., model.time, model.state, model.clock).
   */
  @Test
  public void subpackages_should_be_free_of_cycles() {
    final var importedClasses =
        new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("edu.luc.etl.cs313.android.simplestopwatch");

    SlicesRuleDefinition.slices()
        .matching("edu.luc.etl.cs313.android.simplestopwatch.(*).(*)..")
        .should()
        .beFreeOfCycles()
        .check(importedClasses);
  }

  /**
   * Verifies that classes in model packages only depend on:
   * - Standard Java types (java.., javax..)
   * - Types from the common package
   * - Other types from model packages
   *
   * This ensures the model layer is self-contained and doesn't depend on
   * external frameworks or UI-specific code.
   */
  @Test
  public void model_should_only_depend_on_java_common_or_model() {
    // TODO look for more elegant way to import only production classes
    final var importedClasses =
        new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("edu.luc.etl.cs313.android.simplestopwatch");

    classes()
        .that()
        .resideInAPackage("..model..")
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage(
            "java..",
            "javax..",
            "..common..",
            "..model..")
        .because("Model classes should be independent of external frameworks and UI code")
        .check(importedClasses);
  }

  /**
   * Ensures that model classes do not depend on the R class from the root package.
   * This verifies that model classes don't have UI-specific resource dependencies.
   *
   * Note: R.java uses Integer.valueOf() instead of literal constants to prevent
   * compile-time constant inlining, which allows ArchUnit to detect these dependencies
   * at the bytecode level. With literal constants, the compiler would inline them and
   * ArchUnit wouldn't be able to detect the dependency.
   */
  @Test
  public void model_should_not_depend_on_R_class() {
    final var importedClasses =
        new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("edu.luc.etl.cs313.android.simplestopwatch");

    noClasses()
        .that()
        .resideInAPackage("..model..")
        .should()
        .accessClassesThat()
        .haveFullyQualifiedName("edu.luc.etl.cs313.android.simplestopwatch.R")
        .orShould()
        .accessClassesThat()
        .haveNameMatching("edu\\.luc\\.etl\\.cs313\\.android\\.simplestopwatch\\.R\\$.*")
        .because("Model classes should not depend on Android resource class R")
        .check(importedClasses);
  }
}

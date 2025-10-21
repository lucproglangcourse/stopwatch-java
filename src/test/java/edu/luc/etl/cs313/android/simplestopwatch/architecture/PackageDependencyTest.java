package edu.luc.etl.cs313.android.simplestopwatch.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.importer.ClassFileImporter;
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
        new ClassFileImporter().importPackages("edu.luc.etl.cs313.android.simplestopwatch");

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
        new ClassFileImporter().importPackages("edu.luc.etl.cs313.android.simplestopwatch");

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
    final var importedClasses =
        new ClassFileImporter().importPackages("edu.luc.etl.cs313.android.simplestopwatch");

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
}

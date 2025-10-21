# Architecture Tests

This project uses [ArchUnit](https://www.archunit.org/) to enforce architectural constraints and detect design smells.

## Current Architecture Tests

### ✅ Passing Tests

1. **`packages_should_be_free_of_cycles()`**
   - Verifies no cyclic dependencies between top-level packages
   - Pattern: `edu.luc.etl.cs313.android.simplestopwatch.(*)`

2. **`subpackages_should_be_free_of_cycles()`**
   - Verifies no cyclic dependencies at sub-package level
   - Pattern: `edu.luc.etl.cs313.android.simplestopwatch.(*).(*)`

### ❌ Failing Tests (Demonstrating Architecture Violations)

3. **`model_should_only_depend_on_java_common_or_model()`**
   - **Purpose**: Ensures model layer is self-contained
   - **Allowed Dependencies**: `java.*`, `javax.*`, `..common.*`, `..model.*`
   - **Current Violations** (4):
     - `LapRunningState.getId()` → `R$string.LAP_RUNNING`
     - `LapStoppedState.getId()` → `R$string.LAP_STOPPED`
     - `RunningState.getId()` → `R$string.RUNNING`
     - `StoppedState.getId()` → `R$string.STOPPED`

4. **`model_should_not_depend_on_R_class()`**
   - **Purpose**: Explicitly forbids dependencies on Android resource class `R`
   - **Rationale**: Model classes should not depend on UI-specific resources
   - **Current Violations** (4): Same as test #3 above

## Technical Notes

### Why R.java Uses `Integer.valueOf()`

The `R` class uses `Integer.valueOf()` instead of literal integer constants:

```java
public static final int STOPPED = Integer.valueOf(0);  // Not: = 0;
```

**Reason**: Java's compiler performs **compile-time constant inlining** for `static final` primitive fields with literal values. This means:

- With `int STOPPED = 0;` → Compiler replaces `R.string.STOPPED` with `0` in bytecode
- With `int STOPPED = Integer.valueOf(0);` → Bytecode contains `getstatic R$string.STOPPED`

ArchUnit operates on **compiled bytecode**, not source code. If constants were inlined, ArchUnit couldn't detect the dependency because it wouldn't exist in the bytecode—only in the source.

By using `Integer.valueOf()`, we ensure the dependency exists at runtime, making it detectable by ArchUnit.

### Bytecode Comparison

**With literal constant** (inlined):
```
public int getId();
  Code:
    0: iconst_0    // Push constant 0
    1: ireturn
```

**With Integer.valueOf()** (not inlined):
```
public int getId();
  Code:
    0: getstatic #37  // Field R$string.STOPPED:I
    3: ireturn
```

## Running the Tests

```bash
# Run all architecture tests
./gradlew test --tests "*.PackageDependencyTest"

# Run specific test
./gradlew test --tests "*.PackageDependencyTest.model_should_not_depend_on_R_class"

# Run with detailed violation output
./gradlew test --tests "*.PackageDependencyTest" --info
```

## How to Fix the Violations

To eliminate the architectural violations, the `R` class constants should be moved to the `common` package or the state classes should use a different mechanism for state identification that doesn't depend on UI resources.

For example:
1. Move constants to `Constants.java` in the `common` package
2. Have states return their own type/class as identification
3. Use an enum for state identification

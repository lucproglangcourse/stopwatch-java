package edu.luc.etl.cs313.android.simplestopwatch.test.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import edu.luc.etl.cs313.android.simplestopwatch.model.clock.DefaultClockModel;

/**
 * Concrete testcase subclass for the default clock model implementation.
 *
 * @author laufer
 * @see http://xunitpatterns.com/Testcase%20Superclass.html
 */
public class DefaultClockModelTest extends AbstractClockModelTest {

  @BeforeEach
  public void setUp() throws Exception {
    setModel(new DefaultClockModel());
  }

  @AfterEach
  public void tearDown() throws Exception {
    setModel(null);
  }
}

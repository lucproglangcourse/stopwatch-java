package edu.luc.etl.cs313.android.simplestopwatch.model.impl.time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import edu.luc.etl.cs313.android.simplestopwatch.model.time.AbstractTimeModelTest;

/**
 * Concrete testcase subclass for the default time model implementation.
 *
 * @author laufer
 * @see http://xunitpatterns.com/Testcase%20Superclass.html
 */
public class DefaultTimeModelTest extends AbstractTimeModelTest {

  @BeforeEach
  public void setUp() throws Exception {
    setModel(new DefaultTimeModel());
  }

  @AfterEach
  public void tearDown() throws Exception {
    setModel(null);
  }
}

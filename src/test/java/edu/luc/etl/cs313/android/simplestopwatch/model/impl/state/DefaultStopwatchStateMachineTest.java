package edu.luc.etl.cs313.android.simplestopwatch.model.impl.state;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import edu.luc.etl.cs313.android.simplestopwatch.model.state.AbstractStopwatchStateMachineTest;

/**
 * Concrete testcase subclass for the default stopwatch state machine
 * implementation.
 *
 * @author laufer
 * @see http://xunitpatterns.com/Testcase%20Superclass.html
 */
public class DefaultStopwatchStateMachineTest extends AbstractStopwatchStateMachineTest {

  @BeforeEach
  public void setUp() throws Exception {
    super.setUp();
    setModel(new DefaultStopwatchStateMachine(getDependency(), getDependency()));
  }

  @AfterEach
  public void tearDown() {
    setModel(null);
    super.tearDown();
  }
}

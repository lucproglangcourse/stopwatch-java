package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.TickListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * Testcase superclass for the stopwatch state machine model. Unit-tests the
 * state machine in fast-forward mode by directly triggering successive tick
 * events without the presence of a pseudo-real-time clock. Uses a single
 * unified mock object for all dependencies of the state machine model.
 *
 * @author laufer
 * @see http://xunitpatterns.com/Testcase%20Superclass.html
 */
public abstract class AbstractStopwatchStateMachineTest {

  private StopwatchStateMachine model;

  private UnifiedMockDependency dependency;

  @BeforeEach
  public void setUp() throws Exception {
    dependency = new UnifiedMockDependency();
  }

  @AfterEach
  public void tearDown() {
    dependency = null;
  }

  protected void setModel(final StopwatchStateMachine model) {
    this.model = model;
    if (model == null)
      return;
    this.model.setModelListener(dependency);
    this.model.actionInit();
  }

  protected UnifiedMockDependency getDependency() {
    return dependency;
  }

  @Test
  public void testPreconditions() {
    assertEquals(R.string.STOPPED, dependency.getState());
  }

  @Test
  public void testScenarioRun() {
    assertTimeEquals(0);
    assertFalse(dependency.isStarted());
    model.onStartStop();
    assertTrue(dependency.isStarted());
    onTickRepeat(5);
    assertTimeEquals(5);
  }

  @Test
  public void testScenarioRunLapReset() {
    assertTimeEquals(0);
    assertFalse(dependency.isStarted());
    model.onStartStop();
    assertEquals(R.string.RUNNING, dependency.getState());
    assertTrue(dependency.isStarted());
    onTickRepeat(5);
    assertTimeEquals(5);
    model.onLapReset();
    assertEquals(R.string.LAP_RUNNING, dependency.getState());
    assertTrue(dependency.isStarted());
    onTickRepeat(4);
    assertTimeEquals(5);
    model.onStartStop();
    assertEquals(R.string.LAP_STOPPED, dependency.getState());
    assertFalse(dependency.isStarted());
    assertTimeEquals(5);
    model.onLapReset();
    assertEquals(R.string.STOPPED, dependency.getState());
    assertFalse(dependency.isStarted());
    assertTimeEquals(9);
    model.onLapReset();
    assertEquals(R.string.STOPPED, dependency.getState());
    assertFalse(dependency.isStarted());
    assertTimeEquals(0);
  }

  protected void onTickRepeat(final int n) {
    for (var i = 0; i < n; i++)
      model.onTick();
  }

  protected void assertTimeEquals(final int t) {
    assertEquals(t, dependency.getTime());
  }
}

class UnifiedMockDependency implements TimeModel, ClockModel, StopwatchModelListener {

  private int timeValue = -1, stateId = -1;

  private int runningTime = 0, lapTime = -1;

  private boolean started = false;

  public int getTime() {
    return timeValue;
  }

  public int getState() {
    return stateId;
  }

  public boolean isStarted() {
    return started;
  }

  @Override
  public void onTimeUpdate(final int timeValue) {
    this.timeValue = timeValue;
  }

  @Override
  public void onStateUpdate(final int stateId) {
    this.stateId = stateId;
  }

  @Override
  public void setTickListener(TickListener listener) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void start() {
    started = true;
  }

  @Override
  public void stop() {
    started = false;
  }

  @Override
  public void resetRuntime() {
    runningTime = 0;
  }

  @Override
  public void incRuntime() {
    runningTime++;
  }

  @Override
  public int getRuntime() {
    return runningTime;
  }

  @Override
  public void setLaptime() {
    lapTime = runningTime;
  }

  @Override
  public int getLaptime() {
    return lapTime;
  }
}

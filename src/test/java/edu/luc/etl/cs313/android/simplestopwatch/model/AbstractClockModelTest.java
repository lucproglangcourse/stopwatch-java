package edu.luc.etl.cs313.android.simplestopwatch.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;

/**
 * Test case superclass for the autonomous clock model abstraction. Unit-tests
 * the pseudo-real-time behavior of the clock. Uses a simple stub object to
 * satisfy the clock's dependency.
 *
 * @author laufer
 */
public abstract class AbstractClockModelTest {

  private ClockModel model;

  protected void setModel(final ClockModel model) {
    this.model = model;
  }

  protected ClockModel getModel() {
    return model;
  }

  @Test
  public void testStopped() throws InterruptedException {
    final var i = new AtomicInteger(0);
    model.setTickListener(i::incrementAndGet);
    Thread.sleep(5500);
    assertEquals(0, i.get());
  }

  @Test
  public void testRunning() throws InterruptedException {
    final var i = new AtomicInteger(0);
    model.setTickListener(i::incrementAndGet);
    model.start();
    Thread.sleep(5500);
    model.stop();
    assertEquals(5, i.get());
  }
}

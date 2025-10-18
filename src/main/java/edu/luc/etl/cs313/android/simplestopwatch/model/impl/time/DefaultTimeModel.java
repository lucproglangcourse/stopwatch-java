package edu.luc.etl.cs313.android.simplestopwatch.model.impl.time;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.*;

import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * An implementation of the stopwatch data model.
 */
public class DefaultTimeModel implements TimeModel {

  private int runningTime = 0;

  private int lapTime = -1;

  @Override
  public void resetRuntime() {
    runningTime = 0;
  }

  @Override
  public void incRuntime() {
    runningTime = (runningTime + SEC_PER_TICK) % SEC_PER_HOUR;
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

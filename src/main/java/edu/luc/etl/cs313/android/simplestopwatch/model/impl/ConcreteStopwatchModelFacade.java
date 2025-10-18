package edu.luc.etl.cs313.android.simplestopwatch.model.impl;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.StopwatchModelFacade;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.impl.clock.DefaultClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.impl.state.DefaultStopwatchStateMachine;
import edu.luc.etl.cs313.android.simplestopwatch.model.impl.time.DefaultTimeModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.state.StopwatchStateMachine;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * An implementation of the model facade.
 *
 * @author laufer
 */
public class ConcreteStopwatchModelFacade implements StopwatchModelFacade {

  private final StopwatchStateMachine stateMachine;

  private final ClockModel clockModel;

  private final TimeModel timeModel;

  public ConcreteStopwatchModelFacade() {
    timeModel = new DefaultTimeModel();
    clockModel = new DefaultClockModel();
    stateMachine = new DefaultStopwatchStateMachine(timeModel, clockModel);
    clockModel.setTickListener(stateMachine);
  }

  @Override
  public void start() {
    stateMachine.actionInit();
  }

  @Override
  public void setModelListener(final StopwatchModelListener listener) {
    stateMachine.setModelListener(listener);
  }

  @Override
  public void onStartStop() {
    stateMachine.onStartStop();
  }

  @Override
  public void onLapReset() {
    stateMachine.onLapReset();
  }

}

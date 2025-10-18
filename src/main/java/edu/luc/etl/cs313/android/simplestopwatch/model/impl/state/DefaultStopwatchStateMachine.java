package edu.luc.etl.cs313.android.simplestopwatch.model.impl.state;

import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.clock.ClockModel;
import edu.luc.etl.cs313.android.simplestopwatch.model.state.StopwatchState;
import edu.luc.etl.cs313.android.simplestopwatch.model.state.StopwatchStateMachine;
import edu.luc.etl.cs313.android.simplestopwatch.model.time.TimeModel;

/**
 * An implementation of the state machine for the stopwatch.
 *
 * @author laufer
 */
public class DefaultStopwatchStateMachine implements StopwatchStateMachine {

  public DefaultStopwatchStateMachine(final TimeModel timeModel, final ClockModel clockModel) {
    this.timeModel = timeModel;
    this.clockModel = clockModel;

    // initialize state instances after construction to avoid 'this' escaping
    STOPPED = new StoppedState(this);
    RUNNING = new RunningState(this);
    LAP_RUNNING = new LapRunningState(this);
    LAP_STOPPED = new LapStoppedState(this);
  }

  private final TimeModel timeModel;

  private final ClockModel clockModel;

  /**
   * The internal state of this adapter component. Required for the State pattern.
   */
  private StopwatchState state;

  protected void setState(final StopwatchState state) {
    this.state = state;
    listener.onStateUpdate(state.getId());
  }

  private StopwatchModelListener listener;

  @Override
  public void setModelListener(final StopwatchModelListener listener) {
    this.listener = listener;
  }

  // forward event uiUpdateListener methods to the current state
  // these must be synchronized because events can come from the
  // UI thread or the timer thread
  @Override
  public synchronized void onStartStop() {
    state.onStartStop();
  }

  @Override
  public synchronized void onLapReset() {
    state.onLapReset();
  }

  @Override
  public synchronized void onTick() {
    state.onTick();
  }

  @Override
  public void updateUIRuntime() {
    listener.onTimeUpdate(timeModel.getRuntime());
  }

  @Override
  public void updateUILaptime() {
    listener.onTimeUpdate(timeModel.getLaptime());
  }

  // known states
  private final StopwatchState STOPPED;
  private final StopwatchState RUNNING;
  private final StopwatchState LAP_RUNNING;
  private final StopwatchState LAP_STOPPED;

  // transitions
  @Override
  public void toRunningState() {
    setState(RUNNING);
  }
  @Override
  public void toStoppedState() {
    setState(STOPPED);
  }
  @Override
  public void toLapRunningState() {
    setState(LAP_RUNNING);
  }
  @Override
  public void toLapStoppedState() {
    setState(LAP_STOPPED);
  }

  // actions
  @Override
  public void actionInit() {
    toStoppedState();
    actionReset();
  }
  @Override
  public void actionReset() {
    timeModel.resetRuntime();
    actionUpdateView();
  }
  @Override
  public void actionStart() {
    clockModel.start();
  }
  @Override
  public void actionStop() {
    clockModel.stop();
  }
  @Override
  public void actionLap() {
    timeModel.setLaptime();
  }
  @Override
  public void actionInc() {
    timeModel.incRuntime();
    actionUpdateView();
  }
  @Override
  public void actionUpdateView() {
    state.updateView();
  }
}

package edu.luc.etl.cs313.android.simplestopwatch.model.impl.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.model.state.StopwatchSMStateView;
import edu.luc.etl.cs313.android.simplestopwatch.model.state.StopwatchState;

class StoppedState implements StopwatchState {

  public StoppedState(final StopwatchSMStateView sm) {
    this.sm = sm;
  }

  private final StopwatchSMStateView sm;

  @Override
  public void onStartStop() {
    sm.actionStart();
    sm.toRunningState();
  }

  @Override
  public void onLapReset() {
    sm.actionReset();
    sm.toStoppedState();
  }

  @Override
  public void onTick() {
    throw new UnsupportedOperationException("onTick");
  }

  @Override
  public void updateView() {
    sm.updateUIRuntime();
  }

  @Override
  public int getId() {
    return R.string.STOPPED;
  }

  public StopwatchSMStateView getSm() {
    return sm;
  }
}

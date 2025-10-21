package edu.luc.etl.cs313.android.simplestopwatch;

public class R {
  public interface string {
    // Using Integer.valueOf() prevents compile-time constant inlining
    // This ensures ArchUnit can detect dependencies on this class
    int STOPPED = Integer.valueOf(0);
    int RUNNING = Integer.valueOf(1);
    int LAP_STOPPED = Integer.valueOf(2);
    int LAP_RUNNING = Integer.valueOf(3);
  }
}

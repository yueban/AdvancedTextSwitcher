package sumimakito.android.advtextswitcher;

import android.os.Handler;
import java.lang.ref.WeakReference;

public class Switcher {
  public Handler hlUpdt = new Handler();
  private WeakReference<AdvTextSwitcher> advTsViewRef;
  private boolean isPaused;
  private int mDuration = 1000;
  public Runnable rbUpdt = new Runnable() {
    @Override
    public void run() {
      if (!isPaused && advTsViewRef.get() != null) {
        advTsViewRef.get().next();
        hlUpdt.postDelayed(this, mDuration);
      }
    }
  };

  public Switcher() {
  }

  public Switcher(AdvTextSwitcher view, int duration) {
    this.advTsViewRef = new WeakReference<>(view);
    this.mDuration = duration;
  }

  public Switcher setDuration(int duration) {
    this.mDuration = duration;
    return this;
  }

  public Switcher attach(AdvTextSwitcher view) {
    this.pause();
    this.advTsViewRef = new WeakReference<>(view);
    return this;
  }

  public void start() {
    isPaused = false;
    if (this.advTsViewRef != null) {
      hlUpdt.postDelayed(rbUpdt, mDuration);
    }
  }

  public void pause() {
    isPaused = true;
  }
}

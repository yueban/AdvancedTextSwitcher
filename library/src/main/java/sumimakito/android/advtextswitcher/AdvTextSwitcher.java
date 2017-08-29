package sumimakito.android.advtextswitcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

public class AdvTextSwitcher extends TextSwitcher {
  private Context mContext;
  private String[] mTexts = {};
  private int currentPos;
  private Callback mCallback = new Callback() {
    @Override
    public void onItemClick(int position) {
    }
  };

  public AdvTextSwitcher(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AdvTextSwitcher);

    final int textColor = a.getColor(R.styleable.AdvTextSwitcher_ts_textColor, Color.BLACK);
    final float textSize = a.getDimensionPixelSize(R.styleable.AdvTextSwitcher_ts_textSize, 20);
    final int animInRes = a.getResourceId(R.styleable.AdvTextSwitcher_ts_animIn, R.anim.fade_in_slide_in);
    final int animOutRes = a.getResourceId(R.styleable.AdvTextSwitcher_ts_animOut, R.anim.fade_out_slide_out);
    final int gravityValue = a.getInt(R.styleable.AdvTextSwitcher_ts_gravity, -1);
    boolean right = (gravityValue & 0x01) == 0x01;
    boolean left = (gravityValue & 0x02) == 0x02;
    boolean center = (gravityValue & 0x03) == 0x03;
    final int gravity = center ? Gravity.CENTER : (right ? Gravity.RIGHT | Gravity.CENTER_VERTICAL
        : (left ? (Gravity.LEFT | Gravity.CENTER_VERTICAL) : Gravity.NO_GRAVITY));
    final int lines = a.getInt(R.styleable.AdvTextSwitcher_ts_lines, -1);
    final int ellipsize = a.getInt(R.styleable.AdvTextSwitcher_ts_ellipsize, -1);

    a.recycle();
    this.setFactory(new ViewFactory() {
      public View makeView() {
        TextView innerText = new TextView(mContext);
        innerText.setGravity(gravity);
        innerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        innerText.setTextColor(textColor);
        if (lines != -1) {
          innerText.setLines(lines);
        }
        switch (ellipsize) {
          case 0:
            innerText.setEllipsize(TextUtils.TruncateAt.START);
            break;
          case 1:
            innerText.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            break;
          case 2:
            innerText.setEllipsize(TextUtils.TruncateAt.END);
            break;
          case 3:
            innerText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            break;
        }
        innerText.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View p1) {
            AdvTextSwitcher.this.onClick();
          }
        });
        return innerText;
      }
    });

    Animation animIn = AnimationUtils.loadAnimation(mContext, animInRes);
    Animation animOut = AnimationUtils.loadAnimation(mContext, animOutRes);

    this.setInAnimation(animIn);
    this.setOutAnimation(animOut);
  }

  public void overrideText(String text) {
    this.setText(text);
  }

  public void setAnim(int animInRes, int animOutRes) {
    Animation animIn = AnimationUtils.loadAnimation(mContext, animInRes);
    Animation animOut = AnimationUtils.loadAnimation(mContext, animOutRes);
    this.setInAnimation(animIn);
    this.setOutAnimation(animOut);
  }

  public void setAnim(Animation animIn, Animation animOut) {
    this.setInAnimation(animIn);
    this.setOutAnimation(animOut);
  }

  public void setTexts(String[] texts) {
    if (texts.length > 0) {
      this.mTexts = texts;
      this.currentPos = 0;
    }
    updateDisp();
  }

  public void setCallback(Callback callback) {
    this.mCallback = callback;
  }

  public void next() {
    if (mTexts.length > 0) {
      if (currentPos < mTexts.length - 1) {
        currentPos++;
      } else {
        currentPos = 0;
      }
      updateDisp();
    }
  }

  public void previous() {
    if (mTexts.length > 0) {
      if (currentPos > 0) {
        currentPos--;
      } else {
        currentPos = mTexts.length - 1;
      }
      updateDisp();
    }
  }

  public boolean isEmpty() {
    return mTexts == null || mTexts.length == 0;
  }

  private void updateDisp() {
    this.setText(mTexts[currentPos]);
  }

  private void onClick() {
    mCallback.onItemClick(currentPos);
  }

  public interface Callback {
    public void onItemClick(int position);
  }
}
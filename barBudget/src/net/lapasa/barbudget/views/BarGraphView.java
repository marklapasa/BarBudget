package net.lapasa.barbudget.views;

import net.lapasa.barbudget.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Custom component view lifecycle
 * 
 * onMeasure -> onLayout -> onDraw
 * 
 * @author mlapasa
 * 
 */
public class BarGraphView extends RelativeLayout
{
	private static final String TAG = BarGraphView.class.getName();
	private static final int VALUE_BAR_ID = 2014;
	protected RelativeLayout emptyBar;
	protected RelativeLayout valueBar;
	protected TextView valueTextView;
	protected TextView smValueTextView;
	public double numerator;
	public double denominator;
	public int color;
	private double ratio;
	public int height;

	public int maxWidth;
	public int barGraphWidth = 0;
	private int containerWidth;
	private TextView innerTextView;
	private TextView outerTextView;
	private int valueBarWidth;
	private int innerTextViewWidth;
	private int innerTextViewHeight;

	public BarGraphView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setWillNotDraw(false);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BarGraphView, 0, 0);

		try
		{
			this.color = a.getColor(R.styleable.BarGraphView_color, 0x000000);
			this.numerator = a.getInteger(R.styleable.BarGraphView_numerator, 25);
			this.denominator = a.getInteger(R.styleable.BarGraphView_denominator, 100);
			this.ratio = numerator / denominator;
		}
		finally
		{
			a.recycle();
		}

		if (ratio == 0 || (ratio == Double.NaN))
		{
			this.addView(getEmptyBar());
		}
		else
		{
			setBarGraph();
			setOuterTextView();
			
			this.addView(valueBar);
			this.addView(outerTextView);
		}

	}

	/**
	 * Return sub view that represents this bar has no value; prompt the user to
	 * tap this bar so that there will be values to graph
	 * 
	 * @return
	 */
	private View getEmptyBar()
	{
		TextView tv = new TextView(getContext());
		tv.setText("Tap To Enter A Value");
		emptyBar = new RelativeLayout(getContext());
		emptyBar.addView(tv);
		return emptyBar;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		containerWidth = MeasureSpec.getSize(widthMeasureSpec);
		
		int measuredHeight = getMeasuredHeight();
		setMeasuredDimension(containerWidth, measuredHeight);

	}


	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);

		if (valueBar == null)
		{
			return;
		}
		
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) valueBar.getLayoutParams();
		valueBarWidth = (lp.width > -1) ? lp.width : valueBarWidth;
		innerTextViewWidth = (innerTextViewWidth == 0) ? innerTextView.getMeasuredWidth() : innerTextViewWidth;
		innerTextViewHeight = (innerTextViewHeight == 0) ? innerTextView.getMeasuredHeight() : innerTextViewHeight;

		Log.d(TAG, "inner3: " + innerTextViewWidth + ", valueBar: " + valueBarWidth);

		lp.width = getBarGraphWidth();
		lp.height = innerTextViewHeight;

		if (valueBarWidth > 0 && innerTextViewWidth > valueBarWidth)
		{
			// Hide the inner view, show the outer view
			innerTextView.setVisibility(View.INVISIBLE);
		}

		requestLayout();
	}

	/**
	 * If the TextView could be rendered from within the valueBar, then display
	 * the value from within it. Otherwise, display it on the right outside edge
	 * 
	 * @return
	 */
	private void setBarGraph()
	{
		valueBar = new RelativeLayout(getContext());
		valueBar.setId(VALUE_BAR_ID);

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		valueBar.setLayoutParams(rlp);
		setInnerTextView();

		valueBar.addView(innerTextView);
		valueBar.setBackgroundColor(color);

	}

	private void setInnerTextView()
	{
		innerTextView = new TextView(getContext());
		innerTextView.setText("Inner Inner");
		innerTextView.setBackgroundColor(0xff0000);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		innerTextView.setLayoutParams(rlp);
	}

	private void setOuterTextView()
	{
		outerTextView = new TextView(getContext());
		outerTextView.setText("Outer Outer");
	}

	private int getBarGraphWidth()
	{
		return (int) (containerWidth * ratio);
	}

	public double getRatio()
	{
		return ratio;
	}

	public void setRatio(int ratio)
	{
		this.ratio = ratio;
		this.barGraphWidth = (int) (maxWidth / ratio);
		requestLayout();
	}

}

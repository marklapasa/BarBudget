package net.lapasa.barbudget.views;

import java.text.NumberFormat;
import java.util.Currency;

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
	
	/**
	 * This inner  viewgroup is a visual bargraph representation of a value
	 */
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
	
	/**
	 * This textview is used to display the numerical value of the bar graph from within the bar graph
	 */
	private TextView innerTextView;
	
	/**
	 * This textview is used to display the numerical value of the bar graph from outside the bar graph; When there is not enough space internally
	 */
	private TextView outerTextView;
	private int valueBarWidth;
	private int innerTextViewWidth;
	private int innerTextViewHeight;

	public BarGraphView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
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
			initBarGraph();
			initOuterTextView();
			
			this.addView(valueBar);
			this.addView(outerTextView);
			
			RelativeLayout.LayoutParams outerLayoutParams = (RelativeLayout.LayoutParams) outerTextView.getLayoutParams();
			outerLayoutParams.addRule(RelativeLayout.RIGHT_OF, valueBar.getId());
			
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

	
	/**
	 * Derive the actual width of the parent view 
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		containerWidth = MeasureSpec.getSize(widthMeasureSpec);
		
		int measuredHeight = getMeasuredHeight();
		setMeasuredDimension(containerWidth, measuredHeight);
	}


	/**
	 * Evaluate whether to display the label inside or outside the bar graph
	 */
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

		if (innerTextViewWidth > lp.width)
		{
			// Hide the inner view, show the outer view
			innerTextView.setVisibility(View.GONE);
			outerTextView.setVisibility(View.VISIBLE);
		}
		else
		{
			innerTextView.setVisibility(View.VISIBLE);
			outerTextView.setVisibility(View.GONE);
		}

		requestLayout();
	}

	/**
	 * If the TextView could be rendered from within the valueBar, then display
	 * the value from within it. Otherwise, display it on the right outside edge
	 * 
	 * @return
	 */
	private void initBarGraph()
	{
		valueBar = new RelativeLayout(getContext());
		valueBar.setId(VALUE_BAR_ID);

		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		valueBar.setLayoutParams(rlp);
		
		// By default, show the value internally anyways
		initInnerTextView();
		valueBar.addView(innerTextView);
		valueBar.setBackgroundColor(color);
	}

	private void initInnerTextView()
	{
		innerTextView = new TextView(getContext());
		innerTextView.setText(getFormattedValue());
		innerTextView.setBackgroundColor(0xff0000);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		innerTextView.setLayoutParams(rlp);		
	}

	private String getFormattedValue()
	{
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
		return numberFormat.format(numerator);
	}

	private void initOuterTextView()
	{
		outerTextView = new TextView(getContext());
		outerTextView.setText(getFormattedValue());
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
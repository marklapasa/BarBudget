package net.lapasa.barbudget.views;

import net.lapasa.barbudget.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
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
	 * This inner viewgroup is a visual bargraph representation of a value
	 */
	protected RelativeLayout valueBar;
	private double numerator;
	private double denominator;
	private int color;
	protected double ratio;
	public int height;

	public int maxWidth;
	public int barGraphWidth = 0;
	private int containerWidth;
	
	protected int valueBarWidth;
	protected LayoutParams valueBarLayoutParams;

	public BarGraphView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BarGraphView, 0, 0);

		setWillNotDraw(false);
		
		/**
		 * This will read values defined as properties in the xml version of this class
		 */
		try
		{
			this.setColor(a.getColor(R.styleable.BarGraphView_color, 0xCCCCCC));
			this.setNumerator(a.getInteger(R.styleable.BarGraphView_numerator, 25));
			this.setDenominator(a.getInteger(R.styleable.BarGraphView_denominator, 100));
		}
		finally
		{
			a.recycle();
		}
	}
	
	protected void reset()
	{
		this.removeAllViews();
		valueBar = null;		
	}

	protected void composeBarGraph()
	{
		reset();
		
		if (ratio == 0 || (ratio == Double.NaN))
		{
			valueBar = getEmptyBar();
		}
		else
		{
			initBarGraph();
		}
		
		this.addView(valueBar);
	}

	/**
	 * Return sub view that represents this bar has no value; prompt the user to
	 * tap this bar so that there will be values to graph
	 * 
	 * @return
	 */
	protected RelativeLayout getEmptyBar()
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
	 * If the TextView could be rendered from within the valueBar, then display
	 * the value from within it. Otherwise, display it on the right outside edge
	 * 
	 * @return
	 */
	protected void initBarGraph()
	{
		valueBar = (RelativeLayout) inflate(getContext(), R.layout.grey_budget_bar, null);//new RelativeLayout(getContext());
		valueBar.setId(VALUE_BAR_ID);
		valueBar.setBackgroundColor(getColor());
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		valueBar.setLayoutParams(rlp);
		
		
		// HACK!
		if (isRootGraph())
		{
			TextView tv = new TextView(getContext());
			tv.setText("Testing");
			valueBar.addView(tv);
		}
	}



	

	protected int getBarGraphWidth()
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

	public double getNumerator()
	{
		return numerator;
	}

	public void setNumerator(double numerator)
	{
		this.numerator = numerator;
		refreshRatio();
	}

	public double getDenominator()
	{
		return denominator;
	}

	public void setDenominator(double denominator)
	{
		this.denominator = denominator;
		refreshRatio();
	}

	private void refreshRatio()
	{
		if (getNumerator() > -1 && getDenominator() > 0)
		{
			this.ratio = getNumerator() / getDenominator();
		}
		
	}

	public int getColor()
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}
	
	
	public void refresh()
	{		
		composeBarGraph();
		invalidate();
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
		
		if (valueBar == null)
		{
			return;
		}
		valueBarLayoutParams = (RelativeLayout.LayoutParams) valueBar.getLayoutParams();
		valueBarWidth = (valueBarLayoutParams.width > -1) ? valueBarLayoutParams.width : valueBarWidth;
		valueBarLayoutParams.width = getBarGraphWidth() > 0 ? getBarGraphWidth() : valueBarLayoutParams.width;
		valueBarLayoutParams.height = getMeasuredHeight();
		Log.d(TAG, "valueBar " + valueBarLayoutParams.width + " x " + valueBarLayoutParams.height);
		if (isRootGraph())
		{
			Log.d(TAG, "dispatchDraw()");
			requestLayout();
		}
	}
	
	private boolean isRootGraph()
	{
		return this.getClass() == BarGraphView.class;
	}
}
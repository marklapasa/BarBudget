package net.lapasa.barbudget.views;

import java.text.NumberFormat;

import net.lapasa.barbudget.R;
import net.lapasa.barbudget.Util;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LabeledBarGraphView extends BarGraphView
{
	private static final String TAG = LabeledBarGraphView.class.getName();
	private int innerTextViewWidth;
	private int innerTextViewHeight;

	protected TextView valueTextView;
	protected TextView smValueTextView;

	/**
	 * This textview is used to display the numerical value of the bar graph
	 * from within the bar graph
	 */
	private TextView innerTextView;

	/**
	 * This textview is used to display the numerical value of the bar graph
	 * from outside the bar graph; When there is not enough space internally
	 */
	private TextView outerTextView;

	
	
	/**
	 * Constructor
	 * 
	 * @param context
	 * @param attrs
	 */
	public LabeledBarGraphView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void reset()
	{
		super.reset();
		innerTextView = null;
		outerTextView = null;
	}

	@Override
	protected void composeBarGraph()
	{
		reset();

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

		
		innerTextViewWidth = (innerTextViewWidth == 0) ? innerTextView.getMeasuredWidth() : innerTextViewWidth;
		innerTextViewHeight = (innerTextViewHeight == 0) ? innerTextView.getMeasuredHeight() : innerTextViewHeight;

		Log.d(TAG, "inner3: " + innerTextViewWidth + ", valueBar: " + valueBarWidth);

		
		valueBarLayoutParams.height = innerTextViewHeight;

		if (innerTextViewWidth > valueBarLayoutParams.width)
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

	@Override
	protected void initBarGraph()
	{
		super.initBarGraph();
		// By default, show the value internally anyways
		initInnerTextView();
		valueBar.addView(innerTextView);
	}

	private void initInnerTextView()
	{
		boolean isDarkColor = Util.isDarkColor(Color.red(getColor()), Color.blue(getColor()), Color.green(getColor()));

		int textViewId = isDarkColor ? R.layout.light_text_view : R.layout.dark_text_view;

		innerTextView = (TextView) inflate(getContext(), textViewId, null);
		innerTextView.setText(getFormattedValue());
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		innerTextView.setLayoutParams(rlp);
		innerTextView.invalidate();
	}

	private String getFormattedValue()
	{
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
		return numberFormat.format(getNumerator());
	}

	private void initOuterTextView()
	{
		outerTextView = (TextView) inflate(getContext(), R.layout.dark_text_view, null);
		outerTextView.setText(getFormattedValue());
	}
}

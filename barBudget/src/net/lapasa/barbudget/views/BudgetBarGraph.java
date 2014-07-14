package net.lapasa.barbudget.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

public class BudgetBarGraph extends View
{
	private ShapeDrawable mDrawable;
	private double denominator;
	private double numerator;
	private int color;
	private int containerWidth;
	private double ratio;


	public BudgetBarGraph(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setWillNotDraw(false);
		
	}


	public void draw(int width, int height, int color)
	{
		mDrawable = new ShapeDrawable(new RectShape());
		mDrawable.getPaint().setColor(color);
		mDrawable.setBounds(0, 0, width, height);
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

	protected void onDraw(Canvas canvas)
	{
		if (mDrawable != null)
		{
			mDrawable = null;
		}

		int width = (int) (containerWidth * ratio);
		draw(width, getMeasuredHeight(), color);
		mDrawable.draw(canvas);

	}

	public void setDenominator(double denominator)
	{
		this.denominator = denominator;
		refreshRatio();
	}

	public void setNumerator(double numerator)
	{
		this.numerator = numerator;
		refreshRatio();
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	private void refreshRatio()
	{
		if (numerator > -1 && denominator > 0)
		{
			this.ratio = numerator / denominator;
		}

	}
	
	public void refresh()
	{		
		requestLayout();
//		invalidate();
	}

}

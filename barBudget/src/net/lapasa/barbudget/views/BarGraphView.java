package net.lapasa.barbudget.views;

import net.lapasa.barbudget.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class BarGraphView extends View
{
	public int numerator;
	public int denominator;
	public int color;
	private float ratio;
	public int height;
	
	public int maxWidth;
	public int barGraphWidth = 0;

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
		
	}

	
	/**
	 * Draw a rectangle based on ratio and
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		Rect r = new Rect(0, 0, getMeasuredWidth(), height);
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setStrokeWidth(1);
		canvas.drawRect(r, paint);
		super.onDraw(canvas);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// 1) Get container's dimensions
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		// 2) Calculate this components drawable area
		int measuredWidth = widthMeasureSpec;
		maxWidth = widthMeasureSpec;
		height = 10;
		
		// 3) Finally, make call to setMesauredDimensions
		setMeasuredDimension(measuredWidth, height);
	}




	public float getRatio()
	{
		return ratio;
	}




	public void setRatio(float ratio)
	{
		this.ratio = ratio;
		this.barGraphWidth = (int) (maxWidth / ratio);
	}

}

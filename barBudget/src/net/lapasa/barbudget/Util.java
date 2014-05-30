package net.lapasa.barbudget;

public class Util
{
	public static boolean isDarkColor(int r, int b, int g)
	{
		// Compute luminance first
		double lum = 0.2126 * r + 0.7152 * g + 0.0722 * b;

		// Check if it's closer to black (0) or white (255)
		return lum < 128 ? true : false;
	}
}
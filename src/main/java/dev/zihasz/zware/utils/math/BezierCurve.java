package dev.zihasz.zware.utils.math;

public class BezierCurve {
	private float a, b, c, d;

	public BezierCurve(float a, float b, float c, float d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public double getY(double t) { // thank you google
		final double rt = 1.0-t;
		return (a * trip(rt)) + (b*3*t*doub(rt)) + (c*3 * doub(t)) * rt + (d*trip(t));
	}

	private double trip(double val) {
		return val*val*val;
	}

	private double doub(double val) {
		return val*val;
	}

	public float getA() {
		return a;
	}

	public void setA(float a) {
		this.a = a;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}

	public float getC() {
		return c;
	}

	public void setC(float c) {
		this.c = c;
	}

	public float getD() {
		return d;
	}

	public void setD(float d) {
		this.d = d;
	}
}

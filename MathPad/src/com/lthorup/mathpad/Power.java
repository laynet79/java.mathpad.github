package com.lthorup.mathpad;

import java.awt.Graphics;
import java.awt.Point;

public class Power extends Expression {

	static final long serialVersionUID = 1;
	
	public Power(Expression parent, Point origin, int sign, Expression base, Expression exponent) {
		super(parent, origin, sign);
		base.setParent(this);
		exponent.setParent(this);
		children.add(base);
		children.add(exponent);
	}
	
    @Override
    public Expression duplicate() {
    	Expression base = children.get(0).duplicate();
    	Expression exponent = children.get(1).duplicate();
    	Expression n = new Power(null, origin, sign, base, exponent);
    	return n;
    }
    
	@Override
    public void format(int x) {
		bbox.x = x;
		bbox.y = 0;
		bbox.width = 0;
		bbox.height = 0;
    	Expression base = children.get(0);
    	Expression exponent = children.get(1);
        base.format(x);
        exponent.format(x);
        exponent.move(base.bbox.width, -base.bbox.height/2);
        bbox.add(base.bbox);
        bbox.add(exponent.bbox);
        offY = base.bbox.height/2 + exponent.offY;
        super.format(x);
	}

	@Override
	public void paint(Graphics g) {
		setColor(g);
		super.paint(g);
		resetColor(g);
	}
	
    @Override
    public double evaluate() throws Exception {
    	double base = children.get(0).evaluate();
    	double exp = children.get(1).evaluate();
    	return sign * Math.pow(base, exp);
    }
	
}

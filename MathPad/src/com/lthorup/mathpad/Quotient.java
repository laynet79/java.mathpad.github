package com.lthorup.mathpad;

import java.awt.*;

public class Quotient extends Expression {

	static final long serialVersionUID = 1;
	
	public Quotient(Expression parent, Point origin, int sign, Expression num, Expression den) {
		super(parent, origin, sign);
		num.setParent(this);
		den.setParent(this);
		children.add(num);
		children.add(den);
	}

    @Override
    public Expression duplicate() {
    	Expression num = children.get(0).duplicate();
    	Expression den = children.get(1).duplicate();
    	Expression n = new Quotient(null, origin, sign, num, den);
    	return n;
    }
    
    @Override
    public void format(int x) {
    	bbox.x = x;
    	bbox.y = bbox.width = bbox.height = 0;
    	Expression num = children.get(0);
    	Expression den = children.get(1);
    	num.format(x);
    	num.move(0, num.offY - num.bbox.height - fBase);
        den.format(x);
        den.move(0, den.offY - fBase + 3);
        if (num.bbox.width < den.bbox.width)
        	num.move(den.bbox.width/2 - num.bbox.width/2, 0);
        else if (den.bbox.width < num.bbox.width)
        	den.move(num.bbox.width/2 - den.bbox.width/2, 0);
        bbox.add(num.bbox);
        bbox.add(den.bbox);
        offY = num.bbox.height;
        super.format(x);
	}
	
    @Override
    protected void deleteChild(Expression child) {
    	children.remove(child);
    	selection = children.get(0);
    	parent.replaceChild(this, children.get(0));
    }	
	
	@Override
	public void paint(Graphics g) {
		setColor(g);
		g.drawLine(bbox.x+1+offX, bbox.y+offY, bbox.x + bbox.width-3, bbox.y+offY);
		super.paint(g);
		resetColor(g);
	}
	
    @Override
    public double evaluate() throws Exception {
    	return sign * (children.get(0).evaluate() / children.get(1).evaluate());
    }
}

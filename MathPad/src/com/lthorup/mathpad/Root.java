package com.lthorup.mathpad;

import java.awt.Graphics;
import java.awt.Point;

public class Root extends Expression {
	
	static final long serialVersionUID = 1;
	
	public Root(Expression parent, Point origin, int sign, Expression base, Expression root) {
		super(parent, origin, sign);
		base.setParent(this);
		root.setParent(this);
		children.add(base);
		children.add(root);
	}
	
    @Override
    public Expression duplicate() {
    	Expression base = children.get(0).duplicate();
    	Expression root = children.get(1).duplicate();
    	Expression n = new Power(null, origin, sign, base, root);
    	return n;
    }
    
	@Override
    public void format(int x) {
		bbox.x = x;
		bbox.y = 0;
		bbox.width = 0;
		bbox.height = 0;
    	Expression base = children.get(0);
    	Expression root = children.get(1);
        base.format(x);
        root.format(x);
        base.move((int)fWidth, 0);
        root.move(0, -base.bbox.height/2);
        bbox.add(base.bbox);
        bbox.add(root.bbox);
        offY = base.bbox.height/2 + root.offY;
        super.format(x);
	}

	@Override
	public void paint(Graphics g) {
		setColor(g);
		
    	Expression base = children.get(0);
    	Expression root = children.get(1);
		g.drawLine(root.bbox.x, root.bbox.y + root.bbox.height, root.bbox.x + (int)(fWidth/2), base.bbox.y+base.bbox.height);
		g.drawLine(root.bbox.x + (int)(fWidth/2), base.bbox.y+base.bbox.height, base.bbox.x, base.bbox.y);
		g.drawLine(base.bbox.x, base.bbox.y, base.bbox.x+base.bbox.width, base.bbox.y);
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lthorup.mathpad;

import java.awt.*;

/**
 *
 * @author Layne
 */
public class Sum extends Expression
{
	static final long serialVersionUID = 1;

    public Sum(Expression parent, Point origin, int sign, Expression ... terms)
    {
    	super(parent, origin, sign);
    	for (Expression t : terms) {
    		t.setParent(this);
    		children.add(t);
    	}
    }
    
    @Override
    public Expression duplicate() {
    	Expression n = new Sum(null, origin, sign);
    	n.children = duplicateChildren(n);
    	return n;
    }

    @Override
    public void format(int x) {
    	bbox.x = x;
    	bbox.y = bbox.width = bbox.height = 0;
    	int dx = x;
    	offY = 0;
    	for (Expression term : children) {
    		term.format(dx);
    		offY = Math.max(offY,  term.offY);
    		bbox.add(term.bbox);
    		dx += term.bbox.width;
    	}
    	super.format(x);
    }

    @Override
    public void paint(Graphics g)
    {
    	setColor(g);
    	super.paint(g);
    	resetColor(g);
    }

    @Override
    public double evaluate() throws Exception {
    	double sum = 0;
    	for (Expression c : children)
    		sum += c.evaluate();
    	return sum * sign;
    }
}

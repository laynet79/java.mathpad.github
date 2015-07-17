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
public class Product extends Expression
{
	static final long serialVersionUID = 1;

    public Product(Expression parent, Point origin, int sign, Expression ... factors)
    {
    	super(parent, origin, sign);
        for (Expression f : factors) {
        	f.setParent(this);
        	children.add(f);
        }
    }
    
    @Override
    public Expression duplicate() {
    	Expression n = new Product(null, origin, sign);
    	n.children = duplicateChildren(n);
    	return n;
    }

    @Override
    public void format(int x)
    {
    	bbox.x = x;
    	bbox.y = bbox.width = bbox.height = 0;
    	int dx = x;
    	offY = 0;
    	for (Expression factor : children) {
    		factor.format(dx);
    		offY = Math.max(offY, factor.offY);
    		bbox.add(factor.bbox);
    		dx += factor.bbox.width;
    	}
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
    	double product = 1;
    	for (Expression c : children)
    		product *= c.evaluate();
    	return product * sign;
    }
}

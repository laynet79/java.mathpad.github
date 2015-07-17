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
public class Equation extends Expression
{
	static final long serialVersionUID = 1;

    public Equation(Expression parent, Point origin, Expression left, Expression right)
    {
    	super(parent, origin, 1);
    	left.setParent(this);
    	right.setParent(this);
    	children.add(left);
    	children.add(right);
    }
    
    @Override
    public Expression duplicate() {
    	Expression left = children.get(0).duplicate();
    	Expression right = children.get(1).duplicate();
    	Expression n = new Equation(null, origin, left, right);
    	return n;
    }
    
    @Override
    public void format(int x)
    {
    	bbox.x = x;
    	bbox.y = bbox.width = bbox.height = 0;
    	Expression left = children.get(0);
    	Expression right = children.get(1);
        left.format(x);
        right.format(x + left.bbox.width + (int)(fWidth*3));
        bbox.add(left.bbox);
        bbox.add(right.bbox);
        super.format(x);
    }
    
    @Override
    public void paint(Graphics g)
    {
        setColor(g);
        Expression left = children.get(0);
        g.setFont(font);
        g.drawString("=",  bbox.x + left.bbox.width + (int)(fWidth), origin.y + 10);
        super.paint(g);
        resetColor(g);
    }

}

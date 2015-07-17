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
public class Empty extends Expression
{
	static final long serialVersionUID = 1;
	
    public Empty(Expression parent, Point origin, int sign) {
    	super(parent, origin, sign);
    }
    
    @Override
    public Expression duplicate() {
    	return new Empty(null, origin, sign);
    }
    
    @Override
    public void edit(char key) {
        Expression e = null;
        if (Character.isDigit(key))
            e = new Number(parent, null, sign, key - '0');
        else if (Character.isLetter(key))
            e = new Variable(parent, null, sign, String.valueOf(key));
        if (e != null) {
            parent.replaceChild(this, e);
            selection = e;
        }
        else
        	super.edit(key);
    }

    @Override
    public void format(int x) {
        bbox.x = x;
        bbox.y = -(int)(fWidth/2);
        bbox.width = (int)fWidth;
        bbox.height = fHeight;
        offY = fOrig;
        super.format(x);
    }

    @Override
    public void paint(Graphics g) {
        setColor(g);
        g.fillRect(bbox.x+(int)(fWidth/2)-2 + offX, bbox.y+(fHeight/2)-2, 4, 4);
        super.paint(g);
        resetColor(g);
    }
    
    @Override
    public double evaluate() throws Exception {
    	throw new Exception("Empty Expression found");
    }
}

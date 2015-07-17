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
public class SimpleFactor extends Expression
{
	static final long serialVersionUID = 1;

    protected String name;

    public SimpleFactor(Expression parent, Point origin, int sign) {
    	super(parent, origin, sign);
    }

    @Override
    public void format(int x) {
        bbox.x = x;
        bbox.y = -(int)(fWidth/2);
        bbox.width = (int)(name.length() * fWidth);
        bbox.height = fHeight;
        offY = fOrig;
        super.format(x);
    }

    @Override
    public void paint(Graphics g) {
        setColor(g);
        g.setFont(font);
        g.drawString(name, bbox.x + offX, bbox.y+fOrig);
        super.paint(g);
        resetColor(g);
    }
}

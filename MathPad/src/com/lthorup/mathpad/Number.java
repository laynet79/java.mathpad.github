/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lthorup.mathpad;

import java.awt.Point;
import java.awt.event.*;

/**
 *
 * @author Layne
 */
public class Number extends SimpleFactor
{
	static final long serialVersionUID = 1;
	
    private double value;

    public Number(Expression parent, Point origin, int sign, double value) {
    	super(parent, origin, sign);
        setValue(value);
    }
    
    @Override
    public Expression duplicate() {
    	return new Number(null, origin, sign, value);
    }
    
    private void setValue(double value) {
    	this.value = value;
        name = String.format("%d", (int)value);
    }
    
    @Override
    public void edit(char key) {
        if ((key == KeyEvent.VK_DELETE  || key == KeyEvent.VK_BACK_SPACE) && value >= 10) {
        	setValue(value / 10);
        }
        else if (Character.isDigit(key)) {
            setValue(value * 10 + key - '0');
        }
        else
        	super.edit(key);
    }
    
    @Override
    public double evaluate() throws Exception {
    	return value * sign;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lthorup.mathpad;

import java.awt.Point;

/**
 *
 * @author Layne
 */
public class Variable extends SimpleFactor
{
	static final long serialVersionUID = 1;

    public Variable(Expression parent, Point origin, int sign, String name) {
    	super(parent, origin, sign);
        this.name = name;
    }
    
    @Override
    public Expression duplicate() {
    	return new Variable(null, origin, sign, name);
    }
    
    @Override
    public double evaluate() throws Exception {
    	for (Expression e : environment.children) {
    		if (e instanceof Equation) {
    			Expression left = e.children.get(0);
    			Expression right = e.children.get(1);
    			if ((left instanceof Variable) && name.equals(((Variable)left).name)) {
    				try {
    					return sign * right.evaluate();
    				}
    				catch (Exception ex) { throw ex; }
    			}
    		}
    	}
    	throw new Exception(String.format("%s not defined", name));
    }
}

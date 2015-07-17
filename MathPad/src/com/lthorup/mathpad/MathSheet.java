package com.lthorup.mathpad;

import java.awt.Graphics;

public class MathSheet extends Expression {
	
	static final long serialVersionUID = 1;
	
	public MathSheet() {
		super(null, null, 1);
		environment = this;
	}
	
	@Override
	public boolean select(int x, int y) {
        if (Expression.selection != null)
            Expression.selection.selectChild(x, y);
        if (Expression.selection == null)
            for (Expression c : children)
                if (c.select(x,y))
                    break;
        return Expression.selection != null;
	}
	
	@Override
	public void paint(Graphics g) {
		for (Expression e : children)
			e.paint(g);
	}

}

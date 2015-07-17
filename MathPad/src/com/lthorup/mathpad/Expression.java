/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lthorup.mathpad;

import java.io.Serializable;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.KeyEvent;
import java.awt.font.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Layne
 */
public abstract class Expression implements Serializable, Cloneable
{
	static final long serialVersionUID = 1;
	
    protected Expression parent;
    protected Point origin = new Point(0,0);
    protected Rectangle bbox = new Rectangle();
    protected ArrayList<Expression> children;
    protected int sign;
    protected int offX, offY;
    
	enum EditMode { EDIT, SOLVE }
    public static EditMode mode;
    protected static final Font font;
    protected static final double fWidth;
    protected static final int fHeight, fOrig, fBase;
    protected static final int pWidth = 5;
    public static Expression selection;
    public static Expression environment;
    public static JPanel view;

    static
    {
    	mode = EditMode.EDIT;
        selection = null;
        font = new Font("Courier New", Font.PLAIN, 20);
        Rectangle2D fSize = font.getMaxCharBounds(new FontRenderContext(null, false, false));
        fWidth = fSize.getWidth();
        fHeight = (int)fSize.getHeight();
        fOrig = -(int)fSize.getY();
        fBase = fHeight - fOrig;
    }

    public Expression(Expression parent, Point origin, int sign) {
    	this.parent = parent;
    	if (origin != null) {
    		this.origin.x = origin.x;
    		this.origin.y = origin.y;
    	}
    	children = new ArrayList<Expression>();
    	this.sign = sign;
    	offX = offY = 0;
    }
    
    public Expression duplicate() {
    	return null;
    }
    
    protected ArrayList<Expression> duplicateChildren(Expression newParent) {
    	ArrayList<Expression> newChildren = new ArrayList<Expression>();
    	for (Expression c : children) {
    		Expression newChild = c.duplicate();
    		newChild.setParent(newParent);
    		newChildren.add(newChild);
    	}
    	return newChildren;
    }

    protected void setParent(Expression parent) {
        this.parent = parent;
    }
    
    protected void setOrigin(Point p) {
    	if (p == null)
    		origin.x = origin.y = 0;
    	else {
    		origin.x = p.x;
    		origin.y = p.y;
    	}
    }
    
    public Expression root() {
    	Expression e = this;
    	while (! (e.parent instanceof MathSheet))
    		e = e.parent;
    	return e;
    }
    
    public Point rootOrigin()  {
    	return root().origin;
    }
    
    protected void setSign(int sign) {
    	this.sign = sign;
    }

    protected void setColor(Graphics g) {
        if (this == selection)
            g.setColor(Color.RED);
    }

    protected void resetColor(Graphics g) {
        if (this == selection)
            g.setColor(Color.BLACK);
    }

    //------------------------------------------
    // Selection
    public boolean select(int x, int y) {
        if (bbox.contains(x,y)) {
            selection = this;
            return true;
        }
        selection = null;
        return false;
    }
    public boolean selectChild(int x, int y) {
    	for (Expression e : children)
    		if (e.select(x, y))
    			return true;
        if (parent instanceof MathSheet)
            return select(x,y);
        else
            return parent.selectChild(x, y);
    }
    public void selectUp()
    {
        if (! (parent instanceof MathSheet))
            selection = parent;
    }
    public void selectDown()
    {
        if (children.size() >= 1)
            selection = children.get(0);
    }
    public void selectPrev()
    {
    	int index = parent.children.indexOf(this);
    	index -= 1;
    	if (index >= 0)
    		selection = parent.children.get(index);
    }
    public void selectNext() {
        int index = parent.children.indexOf(this);
        index += 1;
        if (index < parent.children.size())
        	selection = parent.children.get(index);
    }
    
    //------------------------------------------
    // Editing Children
    protected void addChild(Expression child) {
    	child.setParent(this);
    	children.add(child);
    }
    
    protected void deleteChild(Expression child) {
		int index = children.indexOf(child);
		children.remove(child);
		if (children.size() == 1) {
			if (!(this instanceof MathSheet))
				parent.replaceChild(this, children.get(0));
			else
				selection = children.get(0);
		}
		else if (children.size() > 0) {
			if (index < children.size())
				selection = children.get(index);
			else
				selection = children.get(index-1);
		}
    }
    
    protected void replaceChild(Expression oldChild, Expression newChild) {
    	newChild.setParent(this);
    	newChild.setOrigin(oldChild.origin);
    	int index = children.indexOf(oldChild);
        children.set(index, newChild);
        newChild.reformat();
        selection = newChild;
    } 

    public void move(int dx, int dy) {
    	if (parent instanceof MathSheet)
    		origin.translate(dx, dy);
    	bbox.translate(dx, dy);
    	for (Expression e : children)
    		e.move(dx, dy);
    }
    
    protected void reformat() {
    	if (this instanceof MathSheet)
    		return;
    	Expression e = this;
    	while (! (e.parent instanceof MathSheet))
    		e = e.parent;
    	e.format(0);
    	int dx = e.origin.x - e.bbox.width/2;
    	int dy = e.origin.y;
    	e.bbox.translate(dx, dy);
    	for (Expression c : e.children)
    		c.move(dx, dy);
    }
    
    public void format(int x) {
    	offX = 0;
    	if (requiresSign()) {
    		bbox.width += fWidth;
    		offX += (int)fWidth;
    	}
    	if (requiresParen()) {
    		bbox.width += 2*pWidth;
    		offX += pWidth;
    	}
		for (Expression c : children)
			c.move(offX, 0);
    }

    protected boolean firstChild() {
    	return (parent != null) && (parent.children.get(0) == this);
    }
    
    protected boolean complexExpression() {
    	return (this instanceof Sum) || (this instanceof Product) || (this instanceof Quotient) || (this instanceof Power) || (this instanceof Root);
    }
    
    protected boolean requiresSign() {
    	return (sign == -1) || ((parent instanceof Sum) && ! firstChild());
    }
    
    protected boolean requiresParen() {
		return  ((this instanceof Sum) && (parent instanceof Product)) ||
				((parent instanceof Product) && sign == -1) ||
				((parent instanceof Power) && firstChild() && (sign == -1 || complexExpression()));
    }
    
    public void edit(char key) {
    	if (key == KeyEvent.VK_DELETE || key == KeyEvent.VK_BACK_SPACE) {
    		if (this instanceof Empty) {
    			parent.deleteChild(this);
    		}
    		else
    			parent.replaceChild(this, new Empty(parent, null, 1));
    	}
    	
    	else if (key == KeyEvent.VK_ENTER) {
    		selection = new Empty(null, null,1);
    		selection.setOrigin(new Point(root().origin.x, root().origin.y + bbox.height));
    		root().parent.addChild(selection);
    	}
    	
    	else if (this instanceof Equation)
    		return;

    	else if (key == KeyEvent.VK_DELETE || key == KeyEvent.VK_BACK_SPACE) {
    		if (this instanceof Empty) {
    			parent.deleteChild(this);
    		}
    		else
    			parent.replaceChild(this, new Empty(parent, null, 1));
    	}
    	else if (key == '?') {
    		try {
    			double value = evaluate();
    			selection = new Equation(null, null, duplicate(), new Number(null, null, 1, value));
        		selection.setOrigin(new Point(root().origin.x, root().origin.y + bbox.height));
        		root().parent.addChild(selection);
    		}
    		catch (Exception ex) {
    			JOptionPane.showMessageDialog(view, ex.getMessage(), "Unable to evaluate", JOptionPane.ERROR_MESSAGE);
    		}
    	}
    	else if (Character.isDigit(key))
    		addFactor(new Number(null, null, 1, key - '0'));
    	else if (Character.isLetter(key))
    		addFactor(new Variable(null, null, 1, String.valueOf(key)));
    	else if (key == '=')
    		makeEquation();
    	else if (key == '/')
    		makeQuotient();
    	else if (key == '^')
    		makePower();
    	else if (key == '*')
    		addFactor(new Empty(null, null, 1));
    	else if (key == '-' && (this instanceof Empty))
    		sign = -1;
    	else if (key == '+' || key == '-') {
    		Expression term = new Empty(null, null, (key == '+') ? 1 : -1);
    		addTerm(term);
    	}
    }
        
    public void paint(Graphics g) {
    	int dx = 0;
    	if (requiresParen()) {
        	g.drawArc(bbox.x, bbox.y, (int)pWidth*2, bbox.height-3, 90, 180);
        	g.drawArc(bbox.x+bbox.width-(int)2*pWidth, bbox.y, (int)pWidth*2, bbox.height-3, 270, 180);
    		dx += pWidth;
    	}
    	if (requiresSign()) {
            g.setFont(font);
    		g.drawString((sign == 1) ? "+" : "-",  bbox.x+dx, bbox.y+offY);
    	}
    	
    	//g.drawRect(bbox.x, bbox.y, bbox.width, bbox.height);
    	
    	for (Expression c : children)
    		c.paint(g);
    }
    
    public void makeEquation() {
    	if (! (parent instanceof MathSheet))
    		return;
    	Expression p = parent;
    	Expression left = this;
        Expression right = new Empty(null, null, 1);
        Expression eq = new Equation(parent, null, left, right);
        p.replaceChild(this, eq);
        selection = right;
    }
    
    public void makeQuotient() {
    	if (this instanceof Equation)
    		return;
    	Expression p = parent;
    	Expression num = this;
        Expression den = new Empty(null, null, 1);
        Expression q = new Quotient(parent, null, 1, num, den);
        p.replaceChild(this, q);
        selection = den;
    }
    
    public void makePower() {
    	if (this instanceof Equation)
    		return;
    	Expression p = parent;
    	Expression base = this;
    	int sign = base.sign;
    	base.sign = 1;
    	Expression exponent = new Empty(null, null, 1);
    	Expression e = new Power(parent, null, sign, base, exponent);
    	p.replaceChild(this, e);
    	selection = exponent;
    }
    
    public void addFactor(Expression factor) {
    	if (this instanceof Product)
    		addChild(factor);
    	else if (parent instanceof Product)
    		parent.addChild(factor);
    	else {
    		Expression p = parent;
        	Expression product = new Product(parent, null, 1, this, factor);
        	p.replaceChild(this, product);
    	}
    	selection = factor;
    }
    
    public void addTerm(Expression term) {
    	if (parent instanceof Sum)
    		parent.addChild(term);
    	else {
    		Expression p = parent;
        	Expression sum = new Sum(parent, null, 1, this, term);
        	p.replaceChild(this, sum);
    	}
    	selection = term;
    }
    
    public void reorder() {
    	if (this instanceof Equation)
    		return;
    	if ((parent instanceof Equation) || (parent instanceof Sum) || (parent instanceof Product)) {
    		Expression after = null;
    		for (Expression c : parent.children) {
    			if (bbox.x > c.bbox.x)
    				after = c;
    		}
    		ArrayList<Expression> newChildren = new ArrayList<Expression>();
    		if (after == null)
    			newChildren.add(this);
    		for (Expression c : parent.children) {
    			if (c != this)
    				newChildren.add(c);
    			if (after == c)
    				newChildren.add(this);
    		}
    		parent.children = newChildren;
    	}
    }
    
    public double evaluate() throws Exception {
    	return 1;
    }
}

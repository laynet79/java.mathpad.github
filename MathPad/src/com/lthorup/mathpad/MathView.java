package com.lthorup.mathpad;

import javax.swing.JPanel;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MathView extends JPanel {
	
	static final long serialVersionUID = 1;
	
	private MathSheet mathSheet = new MathSheet();
    private int startX, startY;
    private boolean dragging = false;
    private boolean creatingExpression = false;
    
	public MathView() {
		Expression.view = this;
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
		        if (Expression.selection == null)
		            return;
		        
		        switch (evt.getKeyCode())
		        {
		            case KeyEvent.VK_UP:
		                Expression.selection.selectUp();
		                break;
		            case KeyEvent.VK_DOWN:
		                Expression.selection.selectDown();
		                break;
		            case KeyEvent.VK_LEFT:
		                Expression.selection.selectPrev();
		                break;
		            case KeyEvent.VK_RIGHT:
		                Expression.selection.selectNext();
		                break;
		            default:
		                Expression.selection.edit(evt.getKeyChar());
		                break;
		        }
		        Expression.selection.reformat();
		        repaint();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
		        int x = evt.getX();
		        int y = evt.getY();
		        
		        if (evt.getButton() == MouseEvent.BUTTON1) {
		            
		            if (creatingExpression) {
		                Expression e = new Empty(null, new Point(x,y), 1);
		                mathSheet.addChild(e);
		                e.reformat();
		                Expression.selection = e;
		                creatingExpression = false;
		                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		            }
		            else {   
		            	if (mathSheet.select(x, y)) {
		                    startX = x;
		                    startY = y;
		                    dragging = true;
		                }
		            }
		        }
		        repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
		        dragging = false;
		        if (Expression.selection != null) {
		        	Expression.selection.reorder();
		        	Expression.selection.reformat();
		        	repaint();
		        }
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent evt) {
		        if (! dragging)
		            return;
		        int x = evt.getX();
		        int y = evt.getY();
		        int dx = x - startX;
		        int dy = y - startY;
		        startX = x;
		        startY = y;
		        Expression.selection.move(dx, dy);
		        repaint();
			}
		});

	}
	
    public void newExpression()
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        creatingExpression = true;
    }
    
    public void duplicateExpression() {
    	if (Expression.selection != null) {
    		Expression e = Expression.selection.duplicate();
       		mathSheet.addChild(e);
    		e.setOrigin(Expression.selection.rootOrigin());
    		e.reformat();
    		e.move(0, 50);
     		Expression.selection = e;
    		repaint();
    	}
    }
    
    public void makeQuotient() {
		if (Expression.selection != null) {
			Expression.selection.makeQuotient();
			Expression.selection.reformat();
			repaint();
		}
    }
    
    public void makePower() {
		if (Expression.selection != null) {
			Expression.selection.makePower();
			Expression.selection.reformat();
			this.repaint();
		}
    }
    
    public void negate() {
    	if (Expression.selection != null) {
    		Expression.selection.sign *= -1;
    		Expression.selection.reformat();
    		this.repaint();
    	}
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        mathSheet.paint(g);
    }

}

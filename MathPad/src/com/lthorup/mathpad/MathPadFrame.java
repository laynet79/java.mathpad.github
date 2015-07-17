package com.lthorup.mathpad;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.BevelBorder;

import com.lthorup.mathpad.Expression.EditMode;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MathPadFrame extends JFrame {
	
	static final long serialVersionUID = 1;

	private JPanel contentPane;
	private MathView mathView;
	private JButton btnMode;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MathPadFrame frame = new MathPadFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MathPadFrame() {
        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) { mathView.requestFocusInWindow(); }});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 793, 722);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFocusable(false);
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);
		
		JMenuItem mntmOpen = new JMenuItem("Open...");
		mnFile.add(mntmOpen);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mnFile.add(mntmSaveAs);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFocusable(false);
		toolBar.setOrientation(SwingConstants.VERTICAL);
		contentPane.add(toolBar, BorderLayout.WEST);
		
		JButton btnNew = new JButton("");
		btnNew.setFocusable(false);
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mathView.newExpression();
			}
		});
		
		btnMode = new JButton(" Edit ");
		btnMode.setFocusable(false);
		btnMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Expression.mode == EditMode.EDIT) {
					Expression.mode = EditMode.SOLVE;
					btnMode.setText("Solve");
				}
				else {
					Expression.mode = EditMode.EDIT;
					btnMode.setText("  Edit ");
				}
			}
		});
		toolBar.add(btnMode);
		btnNew.setIcon(new ImageIcon(MathPadFrame.class.getResource("/images/Equation.png")));
		toolBar.add(btnNew);
		
		JButton btnDuplicate = new JButton("");
		btnDuplicate.setFocusable(false);
		btnDuplicate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mathView.duplicateExpression();
			}
		});
		btnDuplicate.setIcon(new ImageIcon(MathPadFrame.class.getResource("/images/Duplicate.png")));
		toolBar.add(btnDuplicate);
		
		JButton btnPower = new JButton("");
		btnPower.setFocusable(false);
		btnPower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mathView.makePower();
			}
		});
		btnPower.setIcon(new ImageIcon(MathPadFrame.class.getResource("/images/Exponent.png")));
		toolBar.add(btnPower);
		
		JButton btnQuotient = new JButton("");
		btnQuotient.setFocusable(false);
		btnQuotient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mathView.makeQuotient();
			}
		});
		btnQuotient.setIcon(new ImageIcon(MathPadFrame.class.getResource("/images/Quotient.png")));
		toolBar.add(btnQuotient);
		
		JButton btnMinus = new JButton("");
		btnMinus.setFocusable(false);
		btnMinus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mathView.negate();
			}
		});
		btnMinus.setIcon(new ImageIcon(MathPadFrame.class.getResource("/images/Minus.png")));
		toolBar.add(btnMinus);
		
		mathView = new MathView();
		mathView.setFocusCycleRoot(true);
		mathView.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(mathView, BorderLayout.CENTER);
	}

}

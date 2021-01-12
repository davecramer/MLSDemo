package com.crunchydata;

import com.crunchydata.gui.LowerPanel;
import com.crunchydata.gui.UpperPanel;

import javax.swing.*;
import java.awt.*;


public class MLSTest {

    public static void main(String [] args) {
        new MLSTest().init();;
    }

    public void init() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

// get 2/3 of the height, and 2/3 of the width
        int height = screenSize.height * 2 / 3;
        int width = screenSize.width * 2 / 3;

        JFrame f = new JFrame();//creating instance of JFrame

        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setDividerLocation(height/2);

        LowerPanel lowerPanel = new LowerPanel(width, height);
        /* adding the lower panel for the update */
        JSplitPane upperPanel = new UpperPanel(width, height, lowerPanel);
        jSplitPane.setTopComponent(upperPanel);
        jSplitPane.setBottomComponent(lowerPanel);

        f.setLayout(new FlowLayout());
        f.getContentPane().add(jSplitPane);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);//making the frame visible
    }

}



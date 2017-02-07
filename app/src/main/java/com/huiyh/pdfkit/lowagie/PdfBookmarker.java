package com.huiyh.pdfkit.lowagie;

/**
 * Created by huiyh on 2016/2/24.
 */

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/******************
 * 19  * bookmarks(list) item:
 * 20  *
 * 21  * HashMap =
 * 22  * (key, value)
 * 23  * "Title" "some title"
 * 24  * "Action" "GoTo"
 * 25  * "Page" "some page"
 * 26  *
 * 27  * others may be:
 * 28  * "Style" "some style"
 * 29  * "Color" "some color"
 * 30  * "Open" "True | False"
 * 31  *
 * 32
 ******************/

public class PdfBookmarker extends JFrame implements ActionListener {
    private JButton btn_edit_file;
    private JButton btn_save_path;
    private JButton btn_script_file;
    private JButton btn_run;

    private JPanel leftPanel;
    private JFileChooser chooser;

    private String oldFile;
    private String newFile;
    private String scriptFile;

    public PdfBookmarker() {
        init();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PdfBookmarker();
            }
        });
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == btn_save_path) {
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("pdf files", "pdf"));
            int returnValue = chooser.showSaveDialog(PdfBookmarker.this);
            if (returnValue == 0) {
                this.newFile = chooser.getSelectedFile().getAbsolutePath();
                this.btn_script_file.setEnabled(true);
            }
        } else if (event.getSource() == btn_edit_file) {
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("pdf files", "pdf"));
            int returnValue = chooser.showOpenDialog(PdfBookmarker.this);
            if (returnValue == 0) {
                this.oldFile = chooser.getSelectedFile().getAbsolutePath();
                this.btn_save_path.setEnabled(true);
            }
        } else if (event.getSource() == btn_script_file) {
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("text files", "txt"));
            int returnValue = chooser.showOpenDialog(PdfBookmarker.this);
            if (returnValue == 0) {
                this.scriptFile = chooser.getSelectedFile().getAbsolutePath();
                this.btn_run.setEnabled(true);
            }
        } else if (event.getSource() == btn_run) {
            new BookmarkCreater().setOutlines(oldFile, newFile, scriptFile);

            this.btn_save_path.setEnabled(false);
            this.btn_script_file.setEnabled(false);
            this.btn_run.setEnabled(false);
        }
    }

    private void init() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EtchedBorder());

        btn_edit_file = new JButton("选择要添加书签的文件");
        btn_edit_file.setPreferredSize(new Dimension(180, btn_edit_file.getPreferredSize().height));
        btn_edit_file.setMaximumSize(new Dimension(180, btn_edit_file.getPreferredSize().height));
        btn_edit_file.addActionListener(this);
        leftPanel.add(btn_edit_file);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 3)));

        btn_save_path = new JButton("选择新文件的保存路径");
        btn_save_path.setPreferredSize(new Dimension(180, btn_save_path.getPreferredSize().height));
        btn_save_path.setMaximumSize(new Dimension(180, btn_save_path.getPreferredSize().height));
        btn_save_path.addActionListener(this);
        btn_save_path.setEnabled(false);
        leftPanel.add(btn_save_path);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 3)));

        btn_script_file = new JButton("选择定义好的书签文件");
        btn_script_file.setPreferredSize(new Dimension(180, btn_script_file.getPreferredSize().height));
        btn_script_file.setMaximumSize(new Dimension(180, btn_script_file.getPreferredSize().height));
        btn_script_file.addActionListener(this);
        btn_script_file.setEnabled(false);
        leftPanel.add(btn_script_file);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 3)));

        btn_run = new JButton("执行");
        btn_run.setPreferredSize(new Dimension(180, btn_run.getPreferredSize().height));
        btn_run.setMaximumSize(new Dimension(180, btn_run.getPreferredSize().height));
        btn_run.addActionListener(this);
        btn_run.setEnabled(false);
        leftPanel.add(btn_run);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 3)));

        chooser = new JFileChooser();

        this.setSize(180, 160);
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((scrSize.width - getSize().width) / 2,
                (scrSize.height - getSize().height) / 2);

        this.getContentPane().add(leftPanel);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}





package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import model1.Hunzipping;
import model1.Hzipping;
import model2.Lunzipping;
import model2.Lzipping;

public class Tool extends JFrame implements ActionListener {
    // Definition of global values and items that are part of the GUI.

    static public File opened_file, other_file;
    static long past, future;
    static JLabel redLabel, blueLabel, redScore, blueScore;
    static JPanel buttonPanel, titlePanel, scorePanel;
    static JButton ZH, UH, ZL, UL, EX;

    public JPanel createContentPane() {
        // We create a bottom JPanel to place everything on.
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);

        titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setLocation(90, 20);
        titlePanel.setSize(170, 70);
        totalGUI.add(titlePanel);

        redLabel = new JLabel("选择文件的大小 ");
        redLabel.setLocation(43, 0);
        redLabel.setSize(150, 30);
        redLabel.setHorizontalAlignment(0);
        titlePanel.add(redLabel);

        blueLabel = new JLabel("进行压缩 / 解压后的文件大小 ");
        blueLabel.setLocation(10, 30);
        blueLabel.setSize(170, 30);
        blueLabel.setHorizontalAlignment(0);
        titlePanel.add(blueLabel);

        scorePanel = new JPanel();
        scorePanel.setLayout(null);
        scorePanel.setLocation(270, 20);
        scorePanel.setSize(120, 60);
        totalGUI.add(scorePanel);

        redScore = new JLabel("");
        redScore.setLocation(0, 0);
        redScore.setSize(100, 30);
        redScore.setHorizontalAlignment(0);
        scorePanel.add(redScore);

        blueScore = new JLabel("");
        blueScore.setLocation(0, 30);
        blueScore.setSize(100, 30);
        blueScore.setHorizontalAlignment(0);
        scorePanel.add(blueScore);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setLocation(10, 130);
        buttonPanel.setSize(5200, 150);
        totalGUI.add(buttonPanel);

        ZH = new JButton("ZIP HuffZ");
        ZH.setLocation(0, 0);
        ZH.setSize(120, 30);
        ZH.addActionListener(this);
        buttonPanel.add(ZH);

        UH = new JButton("UNZIP HuffZ");
        UH.setLocation(130, 0);
        UH.setSize(120, 30);
        UH.addActionListener(this);
        buttonPanel.add(UH);

        ZL = new JButton("ZIP LmpZ");
        ZL.setLocation(260, 0);
        ZL.setSize(120, 30);
        ZL.addActionListener(this);
        buttonPanel.add(ZL);

        UL = new JButton("UNZIP LmpZ");
        UL.setLocation(390, 0);
        UL.setSize(120, 30);
        UL.addActionListener(this);
        buttonPanel.add(UL);

        EX = new JButton("退出");
        EX.setLocation(130, 70);
        EX.setSize(250, 30);
        EX.addActionListener(this);
        buttonPanel.add(EX);

        totalGUI.setOpaque(true);
        return totalGUI;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ZH) {
            Hzipping.beginHzipping(opened_file.getPath());
            JOptionPane.showMessageDialog(null, "..........................压缩完毕..........................",
                    "Status", JOptionPane.PLAIN_MESSAGE);
            redScore.setText(opened_file.length() + "Bytes");
            other_file = new File(opened_file.getPath() + ".huffz");
            future = other_file.length();
            blueScore.setText(future + "Bytes");
        } else if (e.getSource() == UH) {
            Hunzipping.beginHunzipping(opened_file.getPath());
            JOptionPane.showMessageDialog(null,
                    "..........................解压完毕..........................", "Status",
                    JOptionPane.PLAIN_MESSAGE);
            redScore.setText(opened_file.length() + "Bytes");
            String s = opened_file.getPath();
            s = s.substring(0, s.length() - 6);
            other_file = new File(s);
            future = other_file.length();
            blueScore.setText(future + "Bytes");
        } else if (e.getSource() == ZL) {
            Lzipping.beginLzipping(opened_file.getPath());
            JOptionPane.showMessageDialog(null, "..........................压缩完毕..........................",
                    "Status", JOptionPane.PLAIN_MESSAGE);
            redScore.setText(opened_file.length() + "Bytes");
            other_file = new File(opened_file.getPath() + ".LmZWp");
            future = other_file.length();
            blueScore.setText(future + "Bytes");
        } else if (e.getSource() == UL) {
            Lunzipping.beginLunzipping(opened_file.getPath());
            JOptionPane.showMessageDialog(null,
                    "..........................解压完毕..........................", "Status",
                    JOptionPane.PLAIN_MESSAGE);
            redScore.setText(opened_file.length() + "Bytes");
            String s = opened_file.getPath();
            s = s.substring(0, s.length() - 6);
            other_file = new File(s);
            future = other_file.length();
            blueScore.setText(future + "Bytes");
        } else if (e.getSource() == EX) {
            System.exit(0);
        }
    }

    public static void createAndShowGUI() {

        // JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("File Compresser");

        // Create and set up the content pane.
        Tool demo = new Tool();
        frame.setContentPane(demo.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(350, 170, 550, 300);

        frame.setVisible(true);

        JMenu fileMenu = new JMenu("文件");

        JMenuBar bar = new JMenuBar();
        
        frame.setJMenuBar(bar);
        bar.add(fileMenu);

        JMenuItem openItem = new JMenuItem("打开");
        fileMenu.add(openItem);
        openItem.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int result = fileChooser.showOpenDialog(null);
                opened_file = fileChooser.getSelectedFile();
                past = opened_file.length();
                redScore.setText(past + "Bytes");
                blueScore.setText("NotYetCalculated");
            }
        });

        JMenuItem exitItem = new JMenuItem("退出");
        fileMenu.add(exitItem);
        exitItem.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        JMenuItem helpMenu = new JMenuItem("帮助");
        frame.setJMenuBar(bar);
        bar.add(helpMenu);
        helpMenu.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null,
                        "两个关于文件压缩的算法" + "\n" + "1. Huffman 算法" + "\n"
                        + "2. Lampel-Ziv 算法" + "\n"
                        + "点击“文件”，“打开”你需要进行压缩的文件" + "\n"
                        + "点击“ZIP HuffZ” 以哈夫曼算法进行文件压缩" + "\n"
                        + "点击“UNZIP HuffZ” 以进行解压哈夫曼文件" + "\n"
                        + "点击“ZIP LamZ” 以Lampel-Ziv算法进行文件压缩" + "\n"
                        + "点击“UNZIP LamZ” 以进行解压Lampel-Ziv文件" + "\n" + "\n"
                        + "注意：必须使用相同的算法进行解压对应压缩的文件" + "\n",
                        "How To...", JOptionPane.PLAIN_MESSAGE);
            }
        });

        /*JMenuItem helpItem = new JMenuItem("关于");
        helpMenu.add(helpItem);
        helpItem.addActionListener(
                new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null,
                        "两个关于文件压缩的算法" + "\n" + "1. Huffman 算法" + "\n"
                        + "2. Lampel-Ziv 算法" + "\n"
                        + "点击“文件”，“打开”你需要进行压缩的文件" + "\n"
                        + "点击“ZIP HuffZ” 以哈夫曼算法进行文件压缩" + "\n"
                        + "点击“UNZIP HuffZ” 以进行解压哈夫曼文件" + "\n"
                        + "点击“ZIP LamZ” 以Lampel-Ziv算法进行文件压缩" + "\n"
                        + "点击“UNZIP LamZ” 以进行解压Lampel-Ziv文件" + "\n" + "\n"
                        + "注意：必须使用相同的算法进行解压对应压缩的文件" + "\n",
                        "How To...", JOptionPane.PLAIN_MESSAGE);
            }
        });*/

    }

}

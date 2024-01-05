import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {

    JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.LEFT,JTabbedPane.SCROLL_TAB_LAYOUT);

    public MainPage(){
        this.setTitle("数据安全性实验");
        this.setVisible(true);
        init();
        this.setBounds(500,200,500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    void init(){
        JPanel panel1 = new jiou();
        JPanel panel2 = new haiming();
        JPanel panel3 = new CRC();
        tabbedPane.addTab("奇偶校验",panel1);
        tabbedPane.addTab("海明码校验",panel2);
        tabbedPane.addTab("CRC校验",panel3);
        tabbedPane.setTabPlacement(JTabbedPane.TOP); // 设置选项卡位置为上方
        this.add(tabbedPane);
    }
}

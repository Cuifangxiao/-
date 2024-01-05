import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class jiou extends JPanel {

    String[] columnNames;
    String[] columnNames1;
    JTable table;
    JTable table1;
    Object[][] data;
    Object[][] data1;
    int count1=0,count2=0;
    List<Integer> c2 = new Vector<>();
    List<Integer> c1 = new Vector<>();

    JLabel Red;
    JLabel Green;

    public jiou(){
        this.setLayout(null);
        init();
    }



    void init(){

        JButton jieshou = new JButton("接受数据");
        JButton chansheng = new JButton("产生数据");
        this.add(chansheng);
        this.add(jieshou);

        chansheng.setBounds(40,20,100,18);
        jieshou.setBounds(300,20,100,18);

        chansheng.addActionListener(e -> chanSheng());
        jieshou.addActionListener(e -> jieShou());

        //第一个表格
        columnNames = new String[]{"序号", "信息项", "校验码"};
        UIManager.put("Table.gridColor", new Color(0, 0, 0, 0));
        UIManager.put("Table.intercellSpacing", new Dimension(0, 0));

        Object[][] data = new Object[0][0];

        table = new JTable(data,columnNames);
        table.setEnabled(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20,40,200,300);
        this.add(scrollPane);


        //第二个表格
        columnNames1 = new String[]{"序号", "信息项", "校验码", "计算"};
        table1 = new JTable(data,columnNames1);
        table1.setEnabled(false);
        table1.getColumnModel().getColumn(0).setPreferredWidth(30);
        table1.getColumnModel().getColumn(1).setPreferredWidth(100);
        table1.getColumnModel().getColumn(2).setPreferredWidth(50);
        table1.getColumnModel().getColumn(3).setPreferredWidth(50);

        JScrollPane scrollPane1 = new JScrollPane(table1);
        scrollPane1.setBounds(230,40,240,300);
        this.add(scrollPane1);

        JLabel jLabel1 = new JLabel("出错检验到：");
        JLabel jLabel2 = new JLabel("出错未检验到：");

        jLabel1.setBounds(20,350,100,30);
        jLabel2.setBounds(220,350,100,30);


        Red = new JLabel();
        Green = new JLabel();
        Red.setBounds(90,350,100,30);
        Green.setBounds(300,350,100,30);

        this.add(jLabel1);
        this.add(jLabel2);
        this.add(Red);
        this.add(Green);
    }



    void chanSheng(){
        Red.setText("");
        Green.setText("");
        c1.clear();
        c2.clear();
        count1=0;
        count2=0;
        String[] binaryNumbers = BinaryNumberGenerator.generateUniqueBinaryNumbers(100);
        data = new Object[binaryNumbers.length][columnNames.length];
        for (int i = 0; i < binaryNumbers.length; i++) {
            data[i][0] = i+1;
            data[i][1] = binaryNumbers[i];
            data[i][2] = generateParityBit(binaryNumbers[i]);
        }
        DefaultTableModel newModel = new DefaultTableModel(data, columnNames);
        table.setModel(newModel); // 将新的TableModel设置给table对象

    }

    void jieShou(){
        Red.setText("");
        Green.setText("");
        c1.clear();
        c2.clear();
        count1=0;
        count2=0;
        Random r = new Random();
        data1 = new Object[100][columnNames1.length];

        for (int i = 0; i < 100; i++) {
            data1[i][0] = data[i][0];
            data1[i][1] = data[i][1];
            data1[i][2] = data[i][2];
        }

        c1 = new Vector<>();
        c2 = new Vector<>();

        for(int i=0;i<100;i++){
            if(r.nextInt(100)<10){
                String temp = (String) data1[i][1];
                StringBuilder sb = new StringBuilder(temp);
                for(int j=0;j<8;j++){
                    if(r.nextInt(10)<2){
                        sb.setCharAt(j,temp.charAt(j) == '0' ? '1' : '0');
                    }
                }
                data1[i][1] = sb.toString();

            }
            data1[i][3] = generateParityBit((String) data1[i][1]);

            if(!data1[i][1].equals(data[i][1])){
                if(data1[i][3] == data[i][2] ){
                    //改变第i行为绿色
                    c1.add(i);
                    count1++;
                }else{
                    //改变第i行为红色
                    c2.add(i);
                    count2++;
                }
            }


        }

        // 创建自定义的单元格渲染器
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


                if (c1.contains(row)) {
                    c.setForeground(Color.GREEN); // 行包含在 c1 中，设置为绿色
                } else if (c2.contains(row)) {
                    c.setForeground(Color.RED); // 行包含在 c2 中，设置为红色
                } else {
                    c.setForeground(Color.BLACK); // 其他行不变，设置为黑色
                }

                return c;
            }
        };

        // 设置表的默认渲染器
        table1.setDefaultRenderer(Object.class, renderer);
        table1.repaint();

        DefaultTableModel newModel = new DefaultTableModel(data1, columnNames1);

        table1.setModel(newModel); // 将新的TableModel设置给table对象
        Red.setText(String.valueOf(count2));
        Green.setText(String.valueOf(count1));
    }





    //计算校验位
    public static String generateParityBit(String binaryString) {
        if (binaryString.length() != 8) {
            throw new IllegalArgumentException("输入的二进制字符串长度必须为8位");
        }

        int countOnes = 0;
        for (char digit : binaryString.toCharArray()) {
            if (digit != '0' && digit != '1') {
                throw new IllegalArgumentException("输入的字符串必须只包含0和1");
            }
            if (digit == '1') {
                countOnes++;
            }
        }

        // 计算奇偶校验位
        if (countOnes % 2 == 0) {
            return "0";  // 偶校验，校验位为0
        } else {
            return "1";  // 奇校验，校验位为1
        }
    }

}


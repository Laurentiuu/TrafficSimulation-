package view;

import controller.GenerateVehicles;
import controller.Map;
import controller.ReadFromFile;
import controller.WriteToFile;
import model.Car;
import model.Crossroad;
import model.Road;
import model.StartPoints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class Traffic implements ActionListener, Runnable {

    private final Integer WIDTH = 1300;
    private final Integer HEIGHT = 750;

    JFrame frame = new JFrame("Traffic Simulation");

    public static Map map = new Map();

    GenerateVehicles generateVehicles = new GenerateVehicles();

    JButton getMap = new JButton("    Generate Map    ");
    JButton start = new JButton("Start");
    JButton stop = new JButton("Pause");
    JButton showNamesButton = new JButton("Show Names");

    ReadFromFile readFromFile;

    ArrayList<Crossroad> crossroads;
    ArrayList<Road> roads;
    ArrayList<StartPoints> startPoints;

    BackgroundMenuBar menuBar = new BackgroundMenuBar();
    JLabel mapLabel = new JLabel("Map File Name:");
    JTextField mapFile = new RoundedJTextField(2);
    JLabel flowLabel = new JLabel("Flow File Name:");
    JTextField flowFile = new RoundedJTextField(2);
    JLabel countDownLabel = new JLabel("Simulation Time: 0 sec");
    JComboBox comboBox = new JComboBox(new Object[]{"Slow", "Normal", "Fast", "Super Fast", "Get Results"});
    static JLabel status = new JLabel("✓");

    Thread thread;
    public static boolean running = false;
    public static boolean ok = false;
    public static boolean idle = true;

    private int countDown;
    public static int mils = 0;
    private int simulationType;
    public static boolean showNames = false;

    private static int scrollPaneWidth;
    private static int scrollPaneHeight;
    JScrollPane scrollPane;


    public static void main(String[] args) {

//        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
//            System.out.println(lookAndFeelInfo.getClassName());
//        }

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        System.setProperty("awt.useSystemAAFontSettings", "on");

        new Traffic();
    }

    public Traffic() {
        frame.setSize(WIDTH, HEIGHT);
        frame.setLayout(new BorderLayout());

        scrollPane = new JScrollPane(map);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        scrollPaneWidth = WIDTH - 40;
        scrollPaneHeight = HEIGHT - 100;
        map.setPreferredSize(new Dimension(scrollPaneWidth, scrollPaneHeight));

        frame.add(scrollPane, BorderLayout.CENTER);

        getMap.setFocusable(false);
        comboBox.setPreferredSize(new Dimension(100, comboBox.getPreferredSize().height));

        //setez fonturile
        Font font = new Font("SansSerif", Font.PLAIN, 12);
        mapLabel.setFont(font);
        flowLabel.setFont(font);
        mapFile.setFont(font);
        flowFile.setFont(font);
        getMap.setFont(font);
        comboBox.setFont(font);
        start.setFont(font);
        stop.setFont(font);
        showNamesButton.setFont(font);
        countDownLabel.setFont(font);
        status.setFont(new Font("SansSerif", Font.BOLD, 20));


        menuBar.add(new JSeparator());
        menuBar.add(getMap);
        menuBar.setBorder(BorderFactory.createMatteBorder(
                4, 4, 3, 0, Color.white));
        menuBar.add(Box.createHorizontalStrut(10));  //adaug spatiu de 10 pixeli
        menuBar.add(mapLabel);
        menuBar.add(Box.createHorizontalStrut(10));  //adaug spatiu de 10 pixeli
        menuBar.add(mapFile);
        menuBar.add(Box.createHorizontalStrut(10));  //adaug spatiu de 10 pixeli
        menuBar.add(flowLabel);
        menuBar.add(Box.createHorizontalStrut(10));  //adaug spatiu de 10 pixeli
        menuBar.add(flowFile);
        menuBar.add(Box.createHorizontalStrut(5));  //adaug spatiu de 10 pixeli

        comboBox.setSelectedIndex(1);
        comboBox.addActionListener(e -> {
            if (comboBox.getSelectedIndex() == 0)
                simulationType = 25;
            if (comboBox.getSelectedIndex() == 1)
                simulationType = 10;
            if (comboBox.getSelectedIndex() == 2)
                simulationType = 5;
            if (comboBox.getSelectedIndex() == 3)
                simulationType = 1;
            if (comboBox.getSelectedIndex() == 4)
                simulationType = 0;
        });
        menuBar.add(comboBox);
        menuBar.add(start);
        menuBar.add(stop);
        menuBar.add(showNamesButton);
        menuBar.add(Box.createHorizontalStrut(10));  //adaug spatiu de 10 pixeli
        menuBar.add(countDownLabel);
        menuBar.add(Box.createHorizontalStrut(10));  //adaug spatiu de 10 pixeli
        menuBar.add(status);
        menuBar.add(Box.createHorizontalStrut(5));  //adaug spatiu de 10 pixeli
        frame.add(menuBar, BorderLayout.NORTH);

        startFrame();

        getMap.addActionListener(this);
        start.addActionListener(this);
        stop.addActionListener(this);
        showNamesButton.addActionListener(this);

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        //placeholdere la textfielduri
//        mapFile.addFocusListener(new FocusListener() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                if (mapFile.getText().equals("   Map File Name ....   ")) {
//                    mapFile.setText("");
//                    mapFile.setForeground(Color.BLACK);
//                }
//            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                if (mapFile.getText().isEmpty()) {
//                    mapFile.setForeground(Color.GRAY);
//                    mapFile.setText("   Map File Name ....   ");
//                }
//            }
//        });

//        flowFile.addFocusListener(new FocusListener() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                if (flowFile.getText().equals("   Flow File Name ....   ")) {
//                    flowFile.setText("");
//                    flowFile.setForeground(Color.BLACK);
//                }
//            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                if (flowFile.getText().isEmpty()) {
//                    flowFile.setForeground(Color.GRAY);
//                    flowFile.setText("   Flow File Name ....   ");
//                }
//            }
//        });

        //in caz ca vreau sa aflu coordonatele la cursor pe frame
//        frame.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                System.out.println(e.getX() + "," + e.getY());
//            }
//        });

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(getMap)) {
            map.clear();
            mils = 0;
            generateVehicles.setMils(0);
            running = false;
            setStatus(true);
            comboBox.setSelectedIndex(1);

            if (mapFile.getText().equals("")) {
                setStatus(false);
                JOptionPane.showMessageDialog(null, "Map not selected");
            } else if (flowFile.getText().equals("")) {
                setStatus(false);
                JOptionPane.showMessageDialog(null, "Flow not selected");
            } else {
                System.out.println("Map: " + mapFile.getText());
                System.out.println("Flow: " + flowFile.getText());
                readFromFile = new ReadFromFile("json/maps/" + mapFile.getText() + ".json",
                        "json/flows/" + flowFile.getText() + ".json");

                if (ok) {
                    idle = false;
                    this.countDown = readFromFile.getFlowTime();
                    this.countDownLabel.setText("Simulation Time: " + countDown + " sec");
                    this.crossroads = readFromFile.getCrossroads();
                    for (Crossroad crossroad : crossroads) {
                        map.addCrossroad(crossroad);
                    }
                    this.roads = readFromFile.getRoads();
                    for (Road road : roads) {
                        map.addRoad(road);
                    }
                    this.startPoints = readFromFile.getStartPoints();
                    frame.repaint();
                    frame.setSize(new Dimension(WIDTH - 1, HEIGHT - 1));
                    frame.setResizable(true);
                } else {
                    startFrame();
                }

            }
        }
        if (event.getSource().equals(start)) {
            if (!running && ok) {
                running = true;
                thread = new Thread(this);
                thread.start();
            }
        }
        if (event.getSource().equals(stop)) {
            running = false;
        }
        if (event.getSource().equals(showNamesButton)) {
            if (!showNames) {
                showNamesButton.setText("Hide Names");
                showNames = true;
                frame.repaint();
            } else {
                showNamesButton.setText("Show Names");
                showNames = false;
                frame.repaint();
            }
        }
    }

    @Override
    public void run() {
        while (running && ok) {
            if (countDown > 0) {
                mils++;
                map.step();
                for (Crossroad crossroad : crossroads) {
                    crossroad.run();
                }
                if (countDown > 0) {
                    for (StartPoints startPoint : startPoints) {
                        if (generateVehicles.run(startPoint.getTime())) {
                            try {
                                Car car = new Car(startPoint.getRoad(),
                                        startPoint.getRoad().getStartX(), startPoint.getRoad().getStartY());
                                car.setDistance(car.getRoad().getDistance());
                                map.addCar(car);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Nu exista puncte de start pentru masini");
                                System.exit(0);
                            }
                        }
                    }
                }
                this.timer();
                frame.repaint();
                try {
                    Thread.sleep(simulationType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                frame.repaint();
                running = false;
                ok = false;
                WriteToFile.createFile();

                String[] buttons = {"OK", "Open File"};

                int result = JOptionPane.showOptionDialog(frame, "Simulation done!\nCheck Results.txt\nAverage Speed is " + WriteToFile.getAverageSpeed() + "km/h",
                        "Done", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
                if (result == 1) {
                    WriteToFile.openFile();
                }

            }
        }

    }

    static class BackgroundMenuBar extends JMenuBar {
        Color bgColor = Color.white;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(bgColor);
            g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

        }
    }

    // implement a round-shaped JTextField
    static class RoundedJTextField extends JTextField {
        public RoundedJTextField(int size) {
            super(size);
            setOpaque(false);
            setMargin(new Insets(2, 2, 2, 2));
        }

        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);
            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            g.setColor(getForeground());
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);
        }

    }

    private void timer() {
        if (countDown > 0) {
            if (mils % 100 == 0) {
                countDown--;
                this.countDownLabel.setText("Simulation Time: " + countDown + " sec");
            }
        }
    }

    public static void fileNotFound() {
        idle = true;
        setStatus(false);
        JOptionPane.showMessageDialog(null,
                "File not found!",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void startPointsNotFound() {
        idle = true;
        setStatus(false);
        JOptionPane.showMessageDialog(null,
                "Start points not found!\n" +
                        "(Check file name OR road names from flow file)",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void setStatus(boolean b) {
        if (b) {
            ok = true;
            status.setForeground(Color.GREEN);
        } else {
            ok = false;
            status.setForeground(Color.BLACK);
        }
    }

    private void startFrame() {
        countDown = 100;
        map.clear();
        idle = true;
        readFromFile = new ReadFromFile("json/maps/start.json");
        this.crossroads = readFromFile.getCrossroads();
        for (Crossroad crossroad : crossroads) {
            map.addCrossroad(crossroad);
        }
        this.roads = readFromFile.getRoads();
        for (Road road : roads) {
            map.addRoad(road);
        }
        frame.repaint();
    }

    public static void changePanel(int width, int height) {
        scrollPaneWidth = width;
        scrollPaneHeight = height;
        map.setPreferredSize(new Dimension(scrollPaneWidth, scrollPaneHeight));
    }

    public static int getScrollPaneWidth() {
        return scrollPaneWidth;
    }

    public static int getScrollPaneHeight() {
        return scrollPaneHeight;
    }

}

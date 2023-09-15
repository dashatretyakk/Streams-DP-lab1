import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThreadExampleB {
    private static Thread thread1; 
    private static Thread thread2; 
    private static JSlider slider; 
    private static JButton stopButton1; 
    private static JButton stopButton2; 
    private static JSpinner prioritySpinner1; 
    private static JSpinner prioritySpinner2; 
    private static int semaphore = 1; 

    public static void main(String[] args) {
        JFrame frame = new JFrame("Процеси, потоки"); // Створення вікна програми
        frame.setLayout(new FlowLayout()); // Встановлення менеджера розміщення FlowLayout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Дія при закритті вікна

        slider = new JSlider(0, 100); 
        slider.setMajorTickSpacing(10); 
        slider.setMinorTickSpacing(10); 
        slider.setPaintTicks(true); 
        slider.setPaintLabels(true); 
        slider.setPreferredSize(new Dimension(250, slider.getPreferredSize().height)); 

        JPanel sliderPanel = new JPanel(); 
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS)); // Встановлення менеджера розміщення BoxLayout
        sliderPanel.add(slider); 

        prioritySpinner1 = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); 
        prioritySpinner2 = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); 

        JPanel spinnerPanel = new JPanel(); 
        spinnerPanel.setLayout(new FlowLayout()); 
        spinnerPanel.add(new JLabel("Пріоритет 1:")); 
        spinnerPanel.add(prioritySpinner1); 
        spinnerPanel.add(new JLabel("Пріоритет 2:")); 
        spinnerPanel.add(prioritySpinner2); 

        JButton startButton1 = new JButton("ПУСК 1"); 
        JButton startButton2 = new JButton("ПУСК 2"); 
        stopButton1 = new JButton("СТОП 1"); 
        stopButton2 = new JButton("СТОП 2"); 

        Dimension buttonSize = new Dimension(120, 30); 
        startButton1.setPreferredSize(buttonSize);
        startButton2.setPreferredSize(buttonSize);
        stopButton1.setPreferredSize(buttonSize);
        stopButton2.setPreferredSize(buttonSize);

        JPanel startButtonsPanel = new JPanel(); // Панель для кнопок "ПУСК"
        startButtonsPanel.setLayout(new FlowLayout()); // Встановлення менеджера розміщення FlowLayout
        startButtonsPanel.add(startButton1); 
        startButtonsPanel.add(startButton2); 

        JPanel stopButtonsPanel = new JPanel(); // Панель для кнопок "СТОП"
        stopButtonsPanel.setLayout(new FlowLayout()); // Встановлення менеджера розміщення FlowLayout
        stopButtonsPanel.add(stopButton1); 
        stopButtonsPanel.add(stopButton2); 

        JLabel semaphoreStatusLabel = new JLabel("Статус семафора:                       вільно"); 

        startButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (semaphore == 1) { // Перевірка статусу семафора
                    semaphore = 0; // Захоплення семафора потоком 1
                    semaphoreStatusLabel.setText("Статус семафора: зайнято потоком 1"); 
                    int priority1 = 1; 
                    prioritySpinner1.setValue(priority1); 
                    stopButton2.setEnabled(false); 
                    slider.setValue(10); 

                    thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Thread.currentThread().setPriority(priority1); 
                            while (!Thread.interrupted()) {
                                slider.setValue(10); 
                            }
                        }
                    });
                    thread1.start(); // Запуск потоку 1
                }
            }
        });

        startButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (semaphore == 1) { // Перевірка статусу семафора
                    semaphore = 0; // Захоплення семафора потоком 2
                    semaphoreStatusLabel.setText("Статус семафора: зайнято потоком 2"); // Зміна статусу семафора
                    int priority2 = 10; // Пріоритет для потоку 2
                    prioritySpinner2.setValue(priority2); 
                    stopButton1.setEnabled(false); // Заборона кнопки "СТОП 1"
                    slider.setValue(90); // Зміна значення ползунка

                    thread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Thread.currentThread().setPriority(priority2); // Встановлення пріоритету потоку 2
                            while (!Thread.interrupted()) {
                                slider.setValue(90); // Зміна значення ползунка в потоці 2
                            }
                        }
                    });
                    thread2.start(); // Запуск потоку 2
                }
            }
        });

        stopButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (thread1 != null) {
                    thread1.interrupt(); // Зупинка потоку 1
                    semaphore = 1; // Звільнення семафора
                    stopButton2.setEnabled(true); // Дозвіл кнопки "СТОП 2"
                    semaphoreStatusLabel.setText("Статус семафора:                       вільно"); 
                }
            }
        });

        stopButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (thread2 != null) {
                    thread2.interrupt(); // Зупинка потоку 2
                    semaphore = 1; // Звільнення семафора
                    stopButton1.setEnabled(true); // Дозвіл кнопки "СТОП 1"
                    semaphoreStatusLabel.setText("Статус семафора:                       вільно"); 
                }
            }
        });

        frame.add(sliderPanel); 
        frame.add(spinnerPanel); 
        frame.add(startButtonsPanel); 
        frame.add(stopButtonsPanel); 
        frame.add(semaphoreStatusLabel); 

        frame.setSize(300, 250); 
        frame.setLocationRelativeTo(null); 
        frame.setResizable(false); 
        frame.setVisible(true); 
    }
}

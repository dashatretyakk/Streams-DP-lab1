import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThreadExampleB {
    private static Thread thread1; // Перший потік
    private static Thread thread2; // Другий потік
    private static JSlider slider; // Повзунок для візуалізації потоків
    private static JButton stopButton1; 
    private static JButton stopButton2; 
    private static JSpinner prioritySpinner1; // Спіннер для вибору пріоритету першого потоку
    private static JSpinner prioritySpinner2; // Спіннер для вибору пріоритету другого потоку
    private static int semaphore = 1; // Семафор для управління потоками

    public static void main(String[] args) {
        JFrame frame = new JFrame("Процеси, потоки"); // Створення вікна програми
        frame.setLayout(new FlowLayout()); // Встановлення менеджера розміщення FlowLayout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Дія при закритті вікна

        slider = new JSlider(0, 100); 
        slider.setMajorTickSpacing(10); 
        slider.setMinorTickSpacing(10); 
        slider.setPaintTicks(true); // Відображення поділок на ползунку
        slider.setPaintLabels(true); // Відображення значень на ползунку
        slider.setPreferredSize(new Dimension(250, slider.getPreferredSize().height)); // Встановлення розмірів ползунка

        JPanel sliderPanel = new JPanel(); 
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS)); // Встановлення менеджера розміщення BoxLayout
        sliderPanel.add(slider); // Додавання ползунка на панель

        prioritySpinner1 = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); // Спіннер для пріоритету першого потоку
        prioritySpinner2 = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1)); // Спіннер для пріоритету другого потоку

        JPanel spinnerPanel = new JPanel(); 
        spinnerPanel.setLayout(new FlowLayout()); // Встановлення менеджера розміщення FlowLayout
        spinnerPanel.add(new JLabel("Пріоритет 1:")); 
        spinnerPanel.add(prioritySpinner1); // Додавання спіннера для пріоритету першого потоку на панель
        spinnerPanel.add(new JLabel("Пріоритет 2:")); 
        spinnerPanel.add(prioritySpinner2); // Додавання спіннера для пріоритету другого потоку на панель

        JButton startButton1 = new JButton("ПУСК 1"); 
        JButton startButton2 = new JButton("ПУСК 2"); 
        stopButton1 = new JButton("СТОП 1"); 
        stopButton2 = new JButton("СТОП 2"); 

        Dimension buttonSize = new Dimension(120, 30); // Розміри кнопок
        startButton1.setPreferredSize(buttonSize);
        startButton2.setPreferredSize(buttonSize);
        stopButton1.setPreferredSize(buttonSize);
        stopButton2.setPreferredSize(buttonSize);

        JPanel startButtonsPanel = new JPanel(); 
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
                    semaphoreStatusLabel.setText("Статус семафора: зайнято потоком 1"); // Зміна статусу семафора
                    int priority1 = 1; // Пріоритет для потоку 1
                    prioritySpinner1.setValue(priority1); // Встановлення пріоритету в спіннері
                    stopButton2.setEnabled(false); // Заборона кнопки "СТОП 2"
                    slider.setValue(10); // Зміна значення ползунка

                    thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Thread.currentThread().setPriority(priority1); // Встановлення пріоритету потоку 1
                            while (!Thread.interrupted()) {
                                slider.setValue(10); // Зміна значення ползунка в потоці 1
                            }
                        }
                    });
                    thread1.start(); 
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
                    prioritySpinner2.setValue(priority2); // Встановлення пріоритету в спіннері
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
                    thread2.start(); 
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
                    semaphoreStatusLabel.setText("Статус семафора:                       вільно"); // Зміна статусу семафора
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
                    semaphoreStatusLabel.setText("Статус семафора:                       вільно"); // Зміна статусу семафора
                }
            }
        });

        frame.add(sliderPanel); // Додавання панелі з ползунком на вікно
        frame.add(spinnerPanel); // Додавання панелі зі спіннерами на вікно
        frame.add(startButtonsPanel); // Додавання панелі з кнопками "ПУСК" на вікно
        frame.add(stopButtonsPanel); // Додавання панелі з кнопками "СТОП" на вікно
        frame.add(semaphoreStatusLabel); // Додавання статусу семафора на вікно

        frame.setSize(300, 250); // Встановлення розмірів вікна
        frame.setLocationRelativeTo(null); // Встановлення положення вікна по центру екрана
        frame.setResizable(false); // Заборона зміни розміру вікна
        frame.setVisible(true); // Відображення вікна
    }
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThreadExampleA {
    private static Thread thread1; 
    private static Thread thread2; 
    private static JSlider slider; 
    private static JSpinner prioritySpinner1; // Спіннер для вибору пріоритету першого потоку
    private static JSpinner prioritySpinner2; // Спіннер для вибору пріоритету другого потоку

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

        JPanel sliderPanel = new JPanel(); // Панель для ползунка
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS)); // Встановлення менеджера розміщення BoxLayout
        sliderPanel.add(slider); 

        prioritySpinner1 = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1)); 
        prioritySpinner2 = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1)); 

        JPanel spinnerPanel = new JPanel(); // Панель для спіннерів
        spinnerPanel.setLayout(new FlowLayout()); // Встановлення менеджера розміщення FlowLayout
        spinnerPanel.add(new JLabel("Пріоритет 1:")); 
        spinnerPanel.add(prioritySpinner1); 
        spinnerPanel.add(new JLabel("Пріоритет 2:")); 
        spinnerPanel.add(prioritySpinner2); 

        JButton startButton = new JButton("Пуск"); 

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int priority1 = (int) prioritySpinner1.getValue(); 
                int priority2 = (int) prioritySpinner2.getValue(); 

                if (thread1 != null && thread1.isAlive()) { // Перевірка чи існує і живий перший потік
                    thread1.setPriority(priority1); 
                } else {
                    thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                slider.setValue(10); // Зміна значення ползунка в першому потоці
                            }
                        }
                    });
                    thread1.setPriority(priority1); 
                    thread1.start(); 
                }

                if (thread2 != null && thread2.isAlive()) { // Перевірка чи існує і живий другий потік
                    thread2.setPriority(priority2); // Встановлення пріоритету другого потоку
                } else {
                    thread2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                slider.setValue(90); // Зміна значення ползунка в другому потоці
                            }
                        }
                    });
                    thread2.setPriority(priority2); // Встановлення пріоритету другого потоку
                    thread2.start(); // Запуск другого потоку
                }
            }
        });

        frame.add(sliderPanel); // Додавання панелі з ползунком на вікно
        frame.add(spinnerPanel); // Додавання панелі зі спіннерами на вікно
        frame.add(startButton); // Додавання кнопки "Пуск" на вікно

        frame.setSize(300, 180); // Встановлення розмірів вікна
        frame.setLocationRelativeTo(null); // Встановлення положення вікна по центру екрана
        frame.setResizable(false); // Заборона зміни розміру вікна
        frame.setVisible(true); // Відображення вікна
    }
}

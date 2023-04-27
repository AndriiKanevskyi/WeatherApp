import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAppGUI {
    private JFrame mainFrame;
    private JTextField cityField;
    private JButton submitButton;
    private JTextArea weatherArea;

    public WeatherAppGUI() {
        // Создаем главный фрейм
        mainFrame = new JFrame("Погода от Андрея");
        mainFrame.setSize(350, 350);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создаем панель для компонентов интерфейса
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        // Создаем текстовое поле для ввода названия города
        cityField = new JTextField(50);
        panel.add(cityField);

        // Создаем кнопку для отправки запроса на получение данных
        submitButton = new JButton("Get Weather");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Отправляем запрос на получение данных о погоде
                String cityName = cityField.getText();
                String apiKey = "e0fdbd815aec567cc675e724493834dd";
                String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey + "&units=metric";

                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Парсим JSON-ответ и выводим данные о погоде в текстовой области
                    JSONObject json = new JSONObject(response.toString());
                    String weather = json.getJSONArray("weather").getJSONObject(0).getString("description");
                    String temperature = String.valueOf(json.getJSONObject("main").getDouble("temp"));
                    String humidity = String.valueOf(json.getJSONObject("main").getInt("humidity"));
                    weatherArea.setText("Weather: " + weather + "\nTemperature: " + temperature + "°C\nHumidity: " + humidity + "%");
                } catch (Exception ex) {
                    weatherArea.setText("Error: " + ex.getMessage());
                }
            }
        });
        panel.add(submitButton);

        // Создаем текстовую область для вывода полученных данных
        weatherArea = new JTextArea(10, 30);
        panel.add(weatherArea);

        // Добавляем панель на главный фрейм
        mainFrame.getContentPane().add(panel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new WeatherAppGUI();
    }
}

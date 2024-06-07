package htmake.jtbot;

import htmake.jtbot.discord.bot.JtBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class JtbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(JtbotApplication.class, args);
		new JtBot();
	}
}

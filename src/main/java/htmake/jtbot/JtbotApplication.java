package htmake.jtbot;

import htmake.jtbot.discord.bot.JtBot;
import htmake.jtbot.discord.bot.properties.DiscordProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.security.auth.login.LoginException;

@SpringBootApplication
@EnableConfigurationProperties(DiscordProperties.class)
public class JtbotApplication {

	@Autowired
	private DiscordProperties discordProperties;

	public static void main(String[] args) {
		SpringApplication.run(JtbotApplication.class, args);
	}

	@Autowired
	public void startDiscordBot() {
		try {
			JtBot jtBot = new JtBot(discordProperties);
		} catch (LoginException e) {
			throw new RuntimeException(e);
		}
	}
}

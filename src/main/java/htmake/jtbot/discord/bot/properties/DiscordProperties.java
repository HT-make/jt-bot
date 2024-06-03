package htmake.jtbot.discord.bot.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "discord")
public class DiscordProperties {

    private String key;
}

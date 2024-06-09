package htmake.jtbot.discord.bot;

import htmake.jtbot.discord.commands.gemini.GeminiCommand;
import htmake.jtbot.discord.commands.global.GlobalCommand;
import htmake.jtbot.discord.commands.poll.PollCommand;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

@Getter
public class JtBot {

    private final ShardManager shardManager;
    private final Dotenv config;

    public JtBot(){
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");

        // Build shard manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.listening("이야기"));
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(
                new GlobalCommand(),
                new GeminiCommand(),
                new PollCommand()
        );
    }
}

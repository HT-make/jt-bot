package htmake.jtbot.discord.commands.message.event;

import htmake.jtbot.discord.util.ErrorUtil;
import htmake.jtbot.discord.util.ObjectMapperUtil;
import htmake.jtbot.global.unirest.HttpClient;
import htmake.jtbot.global.unirest.impl.HttpClientImpl;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class MessageScheduleSlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;
    private final ObjectMapperUtil objectMapperUtil;

    public MessageScheduleSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
        this.objectMapperUtil = new ObjectMapperUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        String messageOption = event.getOption("메시지").getAsString();
        String timeOption = event.getOption("예약-시간").getAsString();

        try {
            LocalDateTime scheduledTime = LocalDateTime.parse(timeOption, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime currentTime = LocalDateTime.now();

            if (scheduledTime.isBefore(currentTime)) {
                errorUtil.sendEphemeralError(event, "메시지 예약", "예약 시간이 현재 시간보다 이전입니다.");
                return;
            }
        } catch (DateTimeParseException e) {
            errorUtil.sendEphemeralError(event, "메시지 예약", "올바른 날짜와 시간 형식이 아닙니다. (예: yyyy-MM-dd HH:mm)");
            return;
        }

        HttpResponse<JsonNode> response = request(messageOption, timeOption, event);

        if (response.getStatus() == 200) {
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setTitle("메세지 예약")
                    .setDescription("메세지가 예약되었습니다.")
                    .build();

            event.replyEmbeds(embed).setEphemeral(true).queue();
        } else if (response.getStatus() == 403) {
            errorUtil.sendEphemeralError(event, "메시지 예약", "최대 예약 가능한 메시지 수를 초과했습니다. (5개)");
        } else {
            errorUtil.sendEphemeralError(event, "메시지 예약", "메시지 예약에 실패했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String message, String time, SlashCommandInteractionEvent event){
        Map<String, Object> requestData = new HashMap<>();

        if (event.getGuild() != null) {
            requestData.put("guildId", event.getGuild().getId());
        }

        requestData.put("userName", event.getUser().getName());
        requestData.put("userAvatar", event.getUser().getAvatarUrl());
        requestData.put("channelId", event.getChannelId());
        requestData.put("message", message);
        requestData.put("scheduledTime", time);

        String endPoint = "/message/schedule/{user_id}";
        Pair<String, String> routeParam = new Pair<>("user_id", event.getUser().getId());
        String jsonBody = objectMapperUtil.mapper(requestData);
        return httpClient.sendPostRequest(endPoint, routeParam, jsonBody);
    }
}

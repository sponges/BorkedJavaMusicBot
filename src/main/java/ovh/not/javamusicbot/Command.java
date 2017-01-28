package ovh.not.javamusicbot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import ovh.not.javamusicbot.lib.AlreadyConnectedException;
import ovh.not.javamusicbot.lib.server.Server;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command {
    private static final Pattern FLAG_PATTERN = Pattern.compile("\\s+-([a-zA-Z]+)");
    public final String[] names;
    public boolean hide = false;

    protected Command(String name, String... names) {
        this.names = new String[names.length + 1];
        this.names[0] = name;
        System.arraycopy(names, 0, this.names, 1, names.length);
    }

    public abstract void on(Context context);

    protected class Context {
        public MessageReceivedEvent event;
        public Server server;
        public String[] args;

        public Message reply(String message) {
            try {
                return event.getChannel().sendMessage(message).complete();
            } catch (PermissionException e) {
                event.getAuthor().getPrivateChannel().sendMessage("**dabBot does not have permission to talk in the #"
                        + event.getTextChannel().getName() + " text channel.**\nTo fix this, allow dabBot to " +
                        "`Read Messages` and `Send Messages` in that text channel.\nIf you are not the guild " +
                        "owner, please send this to them.").complete();
                return null;
            }
        }

        public Set<String> parseFlags() {
            String content = String.join(" ", args);
            Matcher matcher = FLAG_PATTERN.matcher(content);
            Set<String> matches = new HashSet<>();
            while (matcher.find()) {
                matches.add(matcher.group().replaceFirst("\\s+-", ""));
            }
            content = content.replaceAll("\\s+-([a-zA-Z]+)", "");
            args = content.split("\\s+");
            return matches;
        }

        public VoiceChannel getVoiceChannel() {
            return event.getMember().getVoiceState().getChannel();
        }

        public boolean inVoiceChannel() {
            return getVoiceChannel() != null;
        }

        @SuppressWarnings("StatementWithEmptyBody")
        public void handleException(Exception e) {
            if (e instanceof PermissionException) {
                event.getAuthor().getPrivateChannel().sendMessage("**dabBot does not have permission to connect to the "
                        + getVoiceChannel().getName() + " voice channel.**\nTo fix this, allow dabBot to `Connect` " +
                        "and `Speak` in that voice channel.\nIf you are not the guild owner, please send " +
                        "this to them.").complete();
            } else if (e instanceof AlreadyConnectedException) {
            } else {
                reply("An error occurred!");
                e.printStackTrace();
            }
        }
    }
}

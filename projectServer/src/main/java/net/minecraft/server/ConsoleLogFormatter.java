package net.minecraft.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

final class ConsoleLogFormatter extends Formatter {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append(this.dateFormat.format(record.getMillis()));
        Level level = record.getLevel();
        if (level == Level.FINEST) {
            builder.append(" [FINEST] ");
        } else if (level == Level.FINER) {
            builder.append(" [FINER] ");
        } else if (level == Level.FINE) {
            builder.append(" [FINE] ");
        } else if (level == Level.INFO) {
            builder.append(" [INFO] ");
        } else if (level == Level.WARNING) {
            builder.append(" [WARNING] ");
        } else if (level == Level.SEVERE) {
            builder.append(" [SEVERE] ");
        } else if (level == Level.SEVERE) {
            builder.append(" [").append(level.getLocalizedName()).append("] ");
        }

        builder.append(record.getMessage());
        builder.append('\n');
        Throwable thrown = record.getThrown();
        if (thrown != null) {
            StringWriter var5 = new StringWriter();
            thrown.printStackTrace(new PrintWriter(var5));
            builder.append(var5.toString());
        }

        return builder.toString();
    }
}

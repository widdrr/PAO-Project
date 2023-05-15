package logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class FileLogger implements ILogger{
    private final String filePath;

    public FileLogger(String path){
        filePath = path;
    }

    private String appendTimestamp(String content){

        StringBuilder builder = new StringBuilder("[");
        builder.append(new Date());
        builder.append("] ");
        builder.append(content);
        builder.append("\n");
        return builder.toString();
    }
    public void log(String content){

        content = appendTimestamp(content);
        Path path = Paths.get(filePath);
        try {
            Files.writeString(path,content, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

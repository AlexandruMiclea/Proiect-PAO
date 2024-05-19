package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ActionService {
    private static ActionService actionService;
    private final String pathName;
    private static FileWriter fileWriter;

    private ActionService() {
        pathName = new File("").getAbsolutePath() + "/logs/";
    }

    public void closeFileWriter() {
        if (fileWriter != null){
            try {
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("eroare la inchiderea lui fileWriter");
            }
            fileWriter = null;
        }
    }

    public boolean getWriteStatus() {
        return fileWriter == null;
    }

    public void toggleFileWriter() {
        if (fileWriter == null) {
            fileWriter = createLogFile();
        } else {
            closeFileWriter();
        }
    }

    public static ActionService getActionService(){
        if (ActionService.actionService == null) {
            ActionService.actionService = new ActionService();
        }
        return ActionService.actionService;
    }

    private FileWriter createLogFile() {
        String fileName = LocalDateTime.now().toString() + ".csv";
        try {
            File trackerFile = new File(pathName + fileName);
            trackerFile.createNewFile();
            FileWriter fw = new FileWriter(trackerFile.getAbsolutePath());
            return fw;
        } catch (IOException e) {
            System.out.println("Tracker Service Error");
        }
        return null;
    }

    public void writeToLogFile(String action){
        if (fileWriter != null){
            try{
                fileWriter.write(action + "," + LocalDateTime.now().toString() + '\n');
            } catch (IOException e) {
                System.out.println("writeToLogFile Error");
            }
        }
    }
}

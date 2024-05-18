package service;

public class AppService {
    private static AppService service;
    private static ActionService actionService = ActionService.getActionService();
    private AppService() {

    }
    public static AppService getService() {
        if (AppService.service == null){
            AppService.service = new AppService();
        }
        return AppService.service;
    }

}
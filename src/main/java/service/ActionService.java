package service;

import javax.swing.*;

public class ActionService {
    private static ActionService actionService;

    private ActionService() {}

    public static ActionService getActionService(){
        if (ActionService.actionService == null) {
            ActionService.actionService = new ActionService();
        }
        return ActionService.actionService;
    }
}

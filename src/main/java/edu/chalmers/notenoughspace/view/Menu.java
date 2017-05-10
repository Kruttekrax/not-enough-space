package edu.chalmers.notenoughspace.view;


import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;


public class Menu extends AbstractAppState {
    private ActionListener actionListener;
    private SimpleApplication app;
    private Round round;


    private BitmapText startRoundText;
    private BitmapText optionsText;
    private BitmapText exitRoundText;


    public Menu() {
        actionListener = new ActionListener() {

            public void onAction(String name, boolean value, float tpf) {
                if (name.equals("startRound") || (name.equals("options") || (name.equals("exit"))) && !value) {
                    startRound();

                }
            };
        };
    }

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, app);
        app = (SimpleApplication) application;

        round = new Round();
        //round.setEnabled(false);

        app.getInputManager().addMapping("startRound", new KeyTrigger(KeyInput.KEY_RETURN));
        app.getInputManager().addListener(actionListener, "startRound");

        app.getInputManager().addMapping("options", new KeyTrigger(KeyInput.KEY_SPACE));
        app.getInputManager().addListener(actionListener, "options");

        app.getInputManager().addMapping("exit", new MouseButtonTrigger(1));
        app.getInputManager().addListener(actionListener, "exit");


        initStartRoundText();
        initOptionsText();
        initExitText();
    }

    private void startRound() {
        app.getStateManager().detach(this);
        app.getStateManager().attach(round);
        round.setEnabled(true);
    }

    private void initStartRoundText() {
        BitmapFont font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        startRoundText = new BitmapText(font);
        startRoundText.setSize(30);
        startRoundText.move(app.getContext().getSettings().getWidth()/4,
                app.getContext().getSettings().getHeight()/2,
                0);
        startRoundText.setText("PRESS ENTER TO START A NEW ROUND!");
        app.getGuiNode().attachChild(startRoundText);
    }

    private void initOptionsText() {
        BitmapFont font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        optionsText = new BitmapText(font);
        optionsText.setSize(30);
        optionsText.move(app.getContext().getSettings().getWidth()/4,
                app.getContext().getSettings().getHeight()/3,
                0);
        optionsText.setText("OPTIONS");
        app.getGuiNode().attachChild(optionsText);
    }

    private void initExitText() {
        BitmapFont font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        exitRoundText = new BitmapText(font);
        exitRoundText.setSize(30);
        exitRoundText.move(app.getContext().getSettings().getWidth()/4,
                app.getContext().getSettings().getHeight()/5,
                0);
        exitRoundText.setText("EXIT");
        app.getGuiNode().attachChild(exitRoundText);

    }

    @Override
    public void cleanup() {
        super.cleanup();
        app.getGuiNode().detachAllChildren();
        app.getInputManager().deleteMapping("startRound");
        app.getInputManager().deleteMapping("options");
        app.getInputManager().deleteMapping("exit");
        app.getInputManager().removeListener(actionListener);

    }



}

package edu.chalmers.notenoughspace;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import edu.chalmers.notenoughspace.core.Camera;
import edu.chalmers.notenoughspace.core.Planet;
import edu.chalmers.notenoughspace.core.Ship;
import edu.chalmers.notenoughspace.ctrl.SpatialHandler;
import edu.chalmers.notenoughspace.view.HUDNode;

public class Round extends AbstractAppState {

    /**
     * The distance from the ship to the planet's surface.
     */
    private final float SHIP_ALTITUDE = 1.8f;
    private final int ROUND_TIME = 120; //seconds

    SimpleApplication app;

    private Ship ship;
    private Planet planet;
    private Geometry sun;
    private Camera camera;
    private DirectionalLight sunLight;
    private AmbientLight ambientLight;
    private AudioNode happy;
    private HUDNode hud;

    /**
     * The total time the round has been active, in seconds.
     */
    private CountDownTimer timer;
    private ActionListener actionListener;


    public Round(AssetManager assetManager, InputManager inputManager) {
        //Sun:
        Sphere sunMesh = new Sphere(100, 100, 10f);
        sunMesh.setTextureMode(Sphere.TextureMode.Projected);
        sun = new Geometry("sun", sunMesh);
        sun.setMaterial(assetManager.loadMaterial("Materials/SunMaterial.j3m"));
        sun.move(-20, 0, 10);
        sun.setLocalTranslation(-100, 0, 0);
        sun.rotate(0, 0, FastMath.HALF_PI); //It has an ugly line at the equator,
        // that's why the rotation is currently needed...

        //Sunlight:
        sunLight = new DirectionalLight();
        sunLight.setDirection(new Vector3f(2, 0, -1).normalizeLocal());
        sunLight.setColor(ColorRGBA.White);

        //AmbientLight:
        ambientLight = new AmbientLight(ColorRGBA.White.mult(0.3f));
        ambientLight.setEnabled(true);

        //Happy :)
        happy = new AudioNode(assetManager, "Sounds/happy_1.WAV", AudioData.DataType.Buffer);
        happy.setLooping(true);  // activate continuous playing
        happy.setPositional(true);
        happy.setVolume(1);
        happy.play(); // play continuously!

        actionListener = new ActionListener() {

            public void onAction(String name, boolean value, float tpf) {
                Round round = getMe();
                if (name.equals("pause") && !value) {
                    if (round.isEnabled())
                        round.setEnabled(false);
                    else
                        round.setEnabled(true);
                }
                /*if (name.equals("cameraMode") && !value) {
                    if (ship.getShipControl().hasThirdPersonViewAttached()) {
                        getShipControl().detachThirdPersonView();
                    } else {
                        getShipControl().attachThirdPersonView(
                                app.getCamera(), PLANET_RADIUS, SHIP_ALTITUDE);
                    }
                }*/
            }
        };
    }

    private Round getMe() {
        return this;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, app);
        app = (SimpleApplication) application;


        new SpatialHandler(app.getRootNode(), app.getInputManager());

        //ShipNode:
        ship = new Ship(null);
        //Moved initialization of shipControl and beam to initialize(), otherwise
        //the restart didn't work.

        //PlanetNode:
        planet = new Planet(null);

        //app.getCamera();
        camera = new Camera(ship);

        //app.getRootNode().attachChild(planet);
        //Test population
        planet.populate(10, 10, 1);

        app.getRootNode().attachChild(sun);
        app.getRootNode().addLight(sunLight);
        app.getRootNode().addLight(ambientLight);
        app.getRootNode().attachChild(happy);

        app.getInputManager().addMapping("pause", new KeyTrigger(KeyInput.KEY_P));
        app.getInputManager().addListener(actionListener, "pause");

        //Adds option to change camera view:
        app.getInputManager().addMapping("cameraMode", new KeyTrigger(KeyInput.KEY_T));
        app.getInputManager().addListener(actionListener, "cameraMode");

        timer = new CountDownTimer(ROUND_TIME) {
            @Override
            public void onTimeOut() {
                returnToMenu();
            }
        };    //Init timer.

        //Init HUD
        hud = new HUDNode(app.getContext().getSettings().getHeight(), app.getContext().getSettings().getWidth());
        app.getGuiNode().attachChild(hud);
    }

    @Override
    public void cleanup() {
        super.cleanup();

        //getShipControl().detachThirdPersonView();
        //app.getRootNode().detachChild(ship);
        //app.getRootNode().removeLight(ship.getSpotLight());

        //app.getRootNode().detachChild(planet);
        app.getRootNode().detachChild(sun);
        app.getRootNode().removeLight(sunLight);
        app.getRootNode().removeLight(ambientLight);
        app.getRootNode().detachChild(happy);
        happy.stop();   //Why is this needed? (Without it the music keeps playing!)

        app.getInputManager().deleteMapping("pause");
        app.getInputManager().removeListener(actionListener);

        app.getGuiNode().detachChild(hud);
    }

    @Override
    public void setEnabled(boolean enabled) {
        // Pause and unpause
        super.setEnabled(enabled);
        if (enabled) {
            //Restore control
            happy.play();
        } else {
            //Remove control
            happy.pause();
        }
    }

    // Note that update is only called while the state is both attached and enabled.
    @Override
    public void update(float tpf) {
        //Update cow controls? Tick time?
        updateTimer(tpf);
    }

    //Helper method for getting the ship control.
    /*private ShipControl getShipControl() {
        return (ShipControl) ship.getControl(ShipControl.class);
    }*/


    private void updateTimer(float tpf) {
        timer.tick(tpf);
        hud.updateTimer(timer.getTimeLeft());
    }

    private void restartRound() {
        app.getStateManager().detach(this);
        app.getStateManager().attach(new Round(app.getAssetManager(), app.getInputManager()));
    }

    private void returnToMenu() {
        app.getStateManager().detach(this);
        app.getStateManager().attach(new Menu());
    }
}

package edu.chalmers.notenoughspace;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class ShipOverPlanetControl extends AbstractControl {

    private final String THIRD_PERSON_CAMERA = "followShipCamera";


    public ShipOverPlanetControl() {
    }

    protected void controlUpdate(float v) {

    }

    protected void controlRender(RenderManager renderManager, ViewPort viewPort) {

    }

    /**
     * Moves the ship model from its original position at the center of the Ship node
     * to it's correct starting position over the planet's surface.
     *
     * @param planetRadius The radius of the planet that the ship is hovering over.
     */
    public void moveShipModelToStartPosition(float planetRadius, float shipAltitude) {
        Ship shipNode = (Ship) spatial;
        Spatial shipModel = shipNode.getChild("ship");
        shipModel.move(0, planetRadius + shipAltitude, 0);

        shipNode.rotate(FastMath.PI/2, 0, 0);   // Rotates the whole node and therefore
        // also the ship model.
    }


    /////////// CAMERA STUFF /////////////

    /**
     * Attaches the given camera to a position a bit behind and above
     * the ship, looking at the ship with UP in the ship's direction.
     * @param cam The camera to be used as third person camera.
     */
    public void attachThirdPersonView(Camera cam, float planetRadius, float shipAltitude) {
        CameraNode followShipCamera = new CameraNode(THIRD_PERSON_CAMERA, cam);
        followShipCamera.setLocalTranslation( 0
                , 4f, -(planetRadius + shipAltitude + 3));

        Node followShipCameraPivotNode = new Node();    //Helper node to set the default position
        //of the camera.
        followShipCameraPivotNode.attachChild(followShipCamera);
        followShipCameraPivotNode.rotate(FastMath.HALF_PI + -8*FastMath.DEG_TO_RAD,
                FastMath.PI,0);


        //PRESS C TO GET CAMERA INFO FOR SETTING CHASECAM!
        ((Ship) spatial).attachChild(followShipCameraPivotNode);

        //use these to change view of the 3rd person camera
        cam.setLocation(new Vector3f(0.09670155f, -0.5602153f, 11.6101885f));
        cam.setRotation(new Quaternion(-4.8353246E-5f, 0.9718176f, 0.23573402f, 1.991157E-4f));
    }

    /**
     * Removes the ship's third person camera (if attached) which restores
     * the camera to the original one.
     */
    public void detachThirdPersonView() {
        Ship ship = (Ship) spatial;
        if (ship.getChild(THIRD_PERSON_CAMERA) != null) {
            CameraNode followShipCamera = (CameraNode) ship.getChild(THIRD_PERSON_CAMERA);

            ship.detachChild(followShipCamera.getParent());    // Removes the camera
            // getParent() part needed since the CameraNode
            // actually is nested inside a "camera pivot node"
            // which in turn is a child of the shipPivotNode.

            //Restores the original settings of the camera:
            Camera gameCamera = followShipCamera.getCamera();
            gameCamera.setFrame(
                    new Vector3f(0, 0, 10f), // Location
                    new Vector3f(-1f, 0, 0), // Left
                    new Vector3f(0, 1f, 0), // Up
                    new Vector3f(0, 0, -1f)); // Direction
        }
    }

    public boolean hasThirdPersonViewAttached() {
        Node shipPivotNode = (Node) spatial;
        return shipPivotNode.getChild(THIRD_PERSON_CAMERA) != null;
    }


}

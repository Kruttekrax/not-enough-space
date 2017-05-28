package edu.chalmers.notenoughspace.ctrl;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import edu.chalmers.notenoughspace.core.entity.beamable.Junk;

/**
 * Control responsible for telling the junk when to update and
 * for notifying it when it is colliding with the beam.
 */
public class JunkControl extends DetachableControl {

    private final Junk junk;

    public JunkControl(Junk junk){
        this.junk = junk;
    }


    protected void controlUpdate(float tpf) {
        checkCollisionWithBeam(tpf);
    }


    private void checkCollisionWithBeam(float tpf) {
        Spatial beamModel = ControlUtil.getRoot(spatial).getChild("beamModel");

        boolean colliding = ControlUtil.checkCollision(getModel(), beamModel);
        boolean beamVisible = beamModel.getCullHint() == Spatial.CullHint.Never; //TODO: Should we really check the view for game logic?

        if (colliding && beamVisible) {
            if(!junk.isInBeam()){
                junk.enterBeam();
            }
        } else {
            if (junk.isInBeam()) {
                junk.exitBeam();
            }

            junk.update(tpf); //Gravitates the junk.
        }
    }

    private Spatial getModel() {
        return ((Node) spatial).getChild(0);
    }

}

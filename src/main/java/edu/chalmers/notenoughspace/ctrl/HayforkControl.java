package edu.chalmers.notenoughspace.ctrl;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import edu.chalmers.notenoughspace.assets.SoundPlayer;
import edu.chalmers.notenoughspace.core.Hayfork;
import edu.chalmers.notenoughspace.core.Ship;

/**
 * Created by Sparven on 2017-05-15.
 */
public class HayforkControl extends AbstractControl {
    private Hayfork hayfork;

    public HayforkControl(Hayfork hayfork) {
        this.hayfork = hayfork;
    }

    protected void controlUpdate(float tpf) {
        hayfork.update(new JMEInhabitant(ControlUtil.getRoot(spatial).getChild("ship")), tpf);

        //Collision
        boolean colliding = ControlUtil.checkCollision(((Node) spatial).getChild(0),
                (ControlUtil.getRoot(spatial).getChild("shipModel")));

        if (colliding &&
                hayfork.getPlanetaryInhabitant().getLocalTranslation().y <=
                Ship.ALTITUDE * 1.25f /*To prevent it from being "hit" by the
                    stick of the spear. TODO: More stable solution.*/) {
            SoundPlayer.getInstance().play("hayforkHit");
            hayfork.hitSomething();
        }
    }

    protected void controlRender(RenderManager renderManager, ViewPort viewPort) {

    }
}

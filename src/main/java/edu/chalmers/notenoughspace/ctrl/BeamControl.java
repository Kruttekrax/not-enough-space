package edu.chalmers.notenoughspace.ctrl;

import com.google.common.eventbus.Subscribe;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import edu.chalmers.notenoughspace.event.BeamCollisionEvent;
import edu.chalmers.notenoughspace.event.Bus;

/**
 * Created by Vibergf on 03/04/2017.
 */
class BeamControl extends AbstractControl{

    public BeamControl(){
        Bus.getInstance().register(this);
    }

    protected void controlUpdate(float v) {
        spatial.rotate(0f, 0.01f * v, 0f);
    }

    protected void controlRender(RenderManager renderManager, ViewPort viewPort) {

    }

    @Subscribe
    public void onCollisionEvent(BeamCollisionEvent event){

    }
}

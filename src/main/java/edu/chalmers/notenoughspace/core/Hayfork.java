package edu.chalmers.notenoughspace.core;

import edu.chalmers.notenoughspace.ctrl.JMEInhabitant;
import edu.chalmers.notenoughspace.event.Bus;
import edu.chalmers.notenoughspace.event.EntityCreatedEvent;

import javax.vecmath.Vector3f;

/**
 * Created by Sparven on 2017-05-15.
 */
public class Hayfork implements Entity {

    private final static float THROW_SPEED = 10f;

    private PlanetaryInhabitant body;
    private Entity thrower;
    private Vector3f direction;

    public Hayfork(Entity thrower){
        this.thrower = thrower;
        Bus.getInstance().post(new EntityCreatedEvent(this));

    }

    public void update(JMEInhabitant ship, float tpf) {
        if (direction == null) {
            Vector3f myPosition = body.getWorldTranslation();
            Vector3f shipPosition = ship.getWorldTranslation();
            shipPosition.sub(myPosition);
            shipPosition.normalize();
            shipPosition.scale(0.1f);
            direction = shipPosition;
        }

        body.move(direction);
    }

    public PlanetaryInhabitant getPlanetaryInhabitant() {
        return body;
    }

    public void setPlanetaryInhabitant(PlanetaryInhabitant body) {
        this.body = body;
    }

    public Entity getThrower() {
        return thrower;
    }

}
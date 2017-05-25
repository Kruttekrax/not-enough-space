package edu.chalmers.notenoughspace.core;

import com.google.common.eventbus.Subscribe;
import com.sun.javaws.exceptions.InvalidArgumentException;
import edu.chalmers.notenoughspace.core.CountDownTimer;
import edu.chalmers.notenoughspace.core.entity.Entity;
import edu.chalmers.notenoughspace.core.entity.Planet;
import edu.chalmers.notenoughspace.core.entity.beamable.Cow;
import edu.chalmers.notenoughspace.core.entity.beamable.Junk;
import edu.chalmers.notenoughspace.core.entity.enemy.Farmer;
import edu.chalmers.notenoughspace.core.entity.enemy.Satellite;
import edu.chalmers.notenoughspace.core.entity.powerup.EnergyPowerup;
import edu.chalmers.notenoughspace.core.entity.powerup.HealthPowerup;
import edu.chalmers.notenoughspace.core.entity.powerup.PowerupFactory;
import edu.chalmers.notenoughspace.event.Bus;
import edu.chalmers.notenoughspace.event.EntityRemovedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Phnor on 2017-05-23.
 */
public class EntitySpawner {

    private Planet planet;
    private List<SpawnTimer> timerList;

    public EntitySpawner(Planet planet) {
        this.planet = planet;
        timerList = new ArrayList<SpawnTimer>();
        Bus.getInstance().register(this);
    }

    /**
     * Updates the state by ticking all stored timers.
     * @param tpf
     * Time elapsed
     */
    public void update(float tpf){
        for (CountDownTimer timer : timerList) {
            timer.tick(tpf);
        }
    }

    /**
     * Called to replace entities removed.
     * Will cause an increase in population if frequency > 1;
     * @param event
     * The EntityRemovedEvent containing the entity removed.
     */
    @Subscribe
    public void entityRemoved(EntityRemovedEvent event){
        spawn(event.getEntity().getClass());
    }

    public void spawn(Class<? extends Entity> entityClass) { spawn(entityClass, 1); }

    public void spawn(Class<? extends Entity> entityClass, int n) {
        spawn(entityClass, n, false, false);
    }

    /**
     * Calls newInstance() on the entityClass and puts the entity in the planet population.
     * @param entityClass
     * The implementation class of entity to spawn on the planet
     * @param n
     * The amount of entities to spawn
     * @param randomPlacing
     * Uses default placing (other side of planet) if false;
     */
    public void spawn(Class<? extends Entity> entityClass, int n, boolean randomPlacing, boolean randomDirection){
        for (int i = 0; i < n; i++) {
            Entity e = getNewInstanceUtil(entityClass);

            if (randomPlacing){
                e.getPlanetaryInhabitant().rotateForward((float)Math.PI*2* new Random().nextFloat());
                e.getPlanetaryInhabitant().rotateSideways((float)Math.PI*2* new Random().nextFloat());
            }
            if(randomDirection){
                e.getPlanetaryInhabitant().rotateModel((float)Math.PI*2 * new Random().nextFloat());
            }
            planet.populate(getNewInstanceUtil(entityClass));
        }
    }

    private Entity getNewInstanceUtil(Class<? extends Entity> entityClass) {
        Entity e;
        if (entityClass.equals(Cow.class)){
            return new Cow();
        } else if (entityClass.equals(Junk.class)) {
            return new Junk();
        } else if (entityClass.equals(Satellite.class)){
            return new Satellite();
        } else if (entityClass.equals(Farmer.class)){
            return new Farmer();
        } else if (entityClass.equals(HealthPowerup.class) || entityClass.equals(EnergyPowerup.class)){
            return PowerupFactory.createRandomPowerup();
        } else {
            throw new IllegalArgumentException("Not a legal spawnable entity");
        }
    }

    /**
     * Adds a new spawn timer which will continously spawn entity of entityClass each spawnInterval seconds.
     * Amount of entities follow the frequency field
     * @param entityClass
     * The implementation class of entity to spawn on the planet
     * @param spawnInterval
     * The interval between spawned entities
     */
    public void addSpawnTimer(Class<? extends Entity> entityClass, int spawnInterval) {
        timerList.add(new SpawnTimer(entityClass, spawnInterval));
    }

    //Private timer class
    private class SpawnTimer extends CountDownTimer {
        private Class<? extends Entity> entityClass;
        private final float spawnInterval;

        public SpawnTimer(final Class<? extends Entity> entityClass, final float spawnInterval) {
            super(spawnInterval);
            this.entityClass = entityClass;
            this.spawnInterval = spawnInterval;
        }

        public void onTimeOut() {
            spawn(entityClass);
            timeLeft = spawnInterval;
            running = true;
        }
    }
}



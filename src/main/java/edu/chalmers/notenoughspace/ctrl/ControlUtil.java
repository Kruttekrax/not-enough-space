package edu.chalmers.notenoughspace.ctrl;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import edu.chalmers.notenoughspace.core.BeamState;

/**
 * Utility class for jme-nodes
 *
 * Created by Phnor on 2017-04-26.
 */
public class ControlUtil {
    private ControlUtil(){}

    public static Node getRoot(Spatial spatial){
        if (spatial == null)
            throw new IllegalArgumentException("Spatial == Null");
        while (spatial.getParent() != null){
            spatial = spatial.getParent();
        }
        return (Node) spatial;
    }

    public static boolean checkCollision(Spatial first, Spatial second){
        CollisionResults results = new CollisionResults();
        BoundingVolume bv = first.getWorldBound();
        second.collideWith(bv, results);

        if (results.size() > 0) {
            return true;
        }else{
            return false;
        }
    }
}
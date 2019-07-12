package capitalvices;

import sun.reflect.generics.scope.Scope;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Coffer {
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);

    private int score = 0;
    private boolean firstPlayer = false;
    private Color color;
    private List<Resource> resources = new ArrayList<Resource>();

    public Coffer(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void moveResourceHere(Resource resource) {
        if(resource.getCoffer() != null) {
            resource.getCoffer().resources.remove(resource);
        }
        this.resources.add(resource);
        resource.setCoffer(this);
    }

    public void trashResource(Resource resource) {
        this.resources.remove(resource);
        resource.setCoffer(null);
    }

    public List<Resource> getResources() {
        return resources;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int points) {
        score += points;
        if(score > 7) {
            score = 7;
        }
    }
}

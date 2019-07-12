package capitalvices;

import java.awt.*;

public class Resource {
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);
    private ResourceType resourceType;
    private Color color;
    private Coffer coffer;

    public Resource(Color color, Coffer initialCoffer, ResourceType resourceType) {
        this.color = color;
        this.coffer = initialCoffer;
        this.resourceType = resourceType;
    }

    public Color getColor() {
        return color;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Coffer getCoffer() {
        return coffer;
    }

    public void setCoffer(Coffer coffer) {
        this.coffer = coffer;
    }
}

package mino;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class BlockGroup implements Component {
    private List<Component> components = new ArrayList<>();
    private int x, y;

    public void addComponent(Component component) {
        components.add(component);
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    @Override
    public void draw(Graphics2D g2) {
        for (Component component : components) {
            component.draw(g2);
        }
    }

    @Override
    public void update() {
        for (Component component : components) {
            component.update();
        }
    }

    @Override
    public void setPosition(int x, int y) {
        int deltaX = x - this.x;
        int deltaY = y - this.y;

        this.x = x;
        this.y = y;

        // Adjust position of all contained components
        for (Component component : components) {
            if (component instanceof Block) {
                Block block = (Block) component;
                block.setPosition(block.x + deltaX, block.y + deltaY);
            }
        }
    }
}
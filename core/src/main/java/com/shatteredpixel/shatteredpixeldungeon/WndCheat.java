
package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;

public class WndCheat extends Window {

    private static final int WIDTH = 130;
    private static final int BTN_HEIGHT = 18;

    public WndCheat() {
        super();

        int y = 0;

        // Button: Go Down One Floor
        y += addButton("Floor Down (+1)", y, () -> {
            Dungeon.depth++;
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene(InterlevelScene.class);
        });

        // Button: Go Up One Floor
        y += addButton("Floor Up (-1)", y, () -> {
            if (Dungeon.depth > 1) {
                Dungeon.depth--;
                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                Game.switchScene(InterlevelScene.class);
            }
        });

        // Button: Full Map Vision & Identify All Items
        y += addButton("Reveal & Identify Level", y, () -> {
            // Reveal map and remove fog of war
            for (int i = 0; i < Dungeon.level.length(); i++) {
                Dungeon.level.visited[i] = true;
                Dungeon.level.mapped[i] = true;
            }
            Dungeon.observe();
            GameScene.updateFog();

            // Identify items on floor / in chests
            for (Heap heap : Dungeon.level.heaps.valueList()) {
                for (Item item : heap.items) {
                    item.identify();
                }
            }

            // Identify backpack inventory
            Dungeon.hero.belongings.identify();

            hide();
        });

        resize(WIDTH, y);
    }

    private int addButton(String text, int y, Runnable action) {
        RedButton btn = new RedButton(text) {
            @Override
            protected void onClick() {
                action.run();
            }
        };
        btn.setRect(0, y, WIDTH, BTN_HEIGHT);
        add(btn);
        return BTN_HEIGHT + 2;
    }
}

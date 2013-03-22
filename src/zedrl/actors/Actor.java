/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zedrl.actors;

import java.awt.Color;
import zedrl.dungeon.Dungeon;
import zedrl.dungeon.Location;
import zedrl.dungeon.Tile;

/**
 *
 * @author Boxx
 */
public class Actor {

    private Dungeon dungeon;
    private ActorAI AI;
    private int posX;
    private int posY;
    private int posZ;
    private char glyph;
    private Color color;
    private int visionRad;
    public String name;
    public int totalHP;
    public int curHP;
    public int attackVal;
    public int defenseVal;

    public Actor(Dungeon dungeon, char glyph, Color color) {
        this.dungeon = dungeon;
        this.glyph = glyph;
        this.color = color;
    }

    public Actor(Dungeon dungeon, char glyph, Color color, String name, int totalHP, int atkVal, int defVal, int visionRad) {
        this.dungeon = dungeon;
        this.glyph = glyph;
        this.color = color;
        this.name = name;
        this.totalHP = totalHP;
        this.attackVal = atkVal;
        this.defenseVal = defVal;
        this.curHP = totalHP;
        this.visionRad = visionRad;
    }

    public void moveBy(int mx, int my, int mz) {
        Tile targetTile;
        Tile thisTile = dungeon.tile(posX, posY, posZ);
        if (mz == -1){
            if (thisTile.isUpStair()) {
                System.out.println("actor: " + posX + posY + posZ);
                System.out.println("tile: " + thisTile.toString());
                Location target = thisTile.getConnection();
                targetTile = dungeon.tile(target.getX(),target.getY(),target.getZ());
                System.out.println("target: " + target.getX() + "," + target.getY() + "," + target.getZ());
                doAction("walk up the stairs to level %d", posZ+mz+1);
                AI.enterTile(target.getX(),target.getY(),target.getZ(),targetTile);
            } else {
                doAction("try to go up but are stopped by the ceiling");
                return;
            }
        } else if (mz == 1){
            if (thisTile.isDownStair()) {
                System.out.println("actor: " + posX + posY + posZ);
                System.out.println("tile: " + thisTile.toString());
                System.out.println(thisTile.getConnection());
                Location target = thisTile.getConnection();
                targetTile = dungeon.tile(target.getX(),target.getY(),target.getZ());
                System.out.println("target: " + target.getX() + "," + target.getY() + "," + target.getZ());
                doAction("walk down the stairs to level %d",posZ+mz+1);
                AI.enterTile(target.getX(),target.getY(),target.getZ(),targetTile);
            } else {
                doAction("try to go down but are stopped by the floor");
                return;
            }
        }
        Actor occupant = dungeon.getActor(posX + mx, posY + my, posZ + mz);

        if (mz == 0 && occupant == null) {
            targetTile = dungeon.tile(posX + mx, posY + my, posZ + mz);
            AI.enterTile(posX + mx, posY + my, posZ + mz, targetTile);
        } else if (mz == 0 && occupant != null){
            attack(occupant);
        }

    }


    public void attack(Actor occupant) {

        int dmg = Math.max(0, getAtkVal() - occupant.getDefVal());
        dmg = (int) (Math.random() * dmg) + 1;
        occupant.setHP(-dmg);

        sendMessage("You hit the %s for %d damage", occupant.name, dmg);
        if (occupant.getCurHP() <= 0) {
            sendMessage("You killed the %s!", occupant.name);
        }
        occupant.sendMessage("%s hits you!  It strikes for %d damage.", name, dmg);

    }

    public void update() {
        AI.update();
    }

    public boolean canEnter(int wx, int wy, int wz) {
        return dungeon.tile(wx, wy, wz).isPassable() && dungeon.getActor(wx, wy, wz) == null;
    }

    public void setHP(int value) {
        curHP += value;

        if (curHP < 1) {
            dungeon.delete(this);
        }
    }

    public void sendMessage(String message, Object... params) {

        AI.getMessage(String.format(message, params));
    }

    public void doAction(String message, Object... params) {
        int r = 9;
        for (int ox = -r; ox < r + 1; ox++) {
            for (int oy = -r; oy < r + 1; oy++) {
                if (ox * ox + oy * oy > r * r) {
                    continue;
                }

                Actor other = dungeon.getActor(posX + ox, posY + oy, posZ);

                if (other == null) {
                    continue;
                }

                if (other == this) {
                    other.sendMessage("You " + message + ".", params);
                } else {
                    other.sendMessage(String.format("The '%s' %s.", glyph, makeSecondPerson(message)), params);
                }
            }
        }
    }

    private String makeSecondPerson(String text) {
        String[] words = text.split(" ");
        words[0] = words[0] + "s";

        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(" ");
            builder.append(word);
        }

        return builder.toString().trim();
    }

    public int getAtkVal() {
        return attackVal;
    }

    public int getCurHP() {
        return curHP;
    }

    public int getDefVal() {
        return defenseVal;
    }

    public int getTotalHP() {
        return totalHP;
    }

    public char getGlyph() {
        return glyph;
    }

    public Color getColor() {
        return color;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setAI(ActorAI AI) {
        this.AI = AI;
    }

    void setPosZ(int posZ) {
        this.posZ = posZ;
    }

    public int getPosZ() {
        return posZ;
    }

    public int getVisionRad() {
        return visionRad;
    }
    public boolean hasSightOf(int x, int y, int z){
        return AI.hasSightOf(x,y,z);
    }
    public Tile lookAt(int x, int y, int z){
        return dungeon.tile(x, y, z);
    }
}

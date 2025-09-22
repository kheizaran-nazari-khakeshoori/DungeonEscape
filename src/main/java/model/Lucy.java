package model;

public class Lucy extends Player {
    public Lucy() {
        // Bean's personal demon. Fragile but packs a punch. Do it, do it, do it!
        super("Lucy", 66);
        
        // Represents his demonic influence, not a physical weapon.
        Weapon demonicPower = new Weapon("Demonic Influence", 20, 999, DamageType.FIRE, "images/weapons/Fire.png");
        this.pickItem(demonicPower);
    }
}
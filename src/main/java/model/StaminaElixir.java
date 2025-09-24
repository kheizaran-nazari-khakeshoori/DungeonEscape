package model;

public class StaminaElixir extends Potion {

    public StaminaElixir(String name, String description, int healAmount, String imagePath) {
        super(name, description, healAmount, 1, imagePath);
    }

    @Override
    public void use(Player player) {
        // Provide the initial burst of healing
        super.use(player);

        // Add a regeneration effect: 5 health per turn for 3 turns
        player.addEffect(new ActiveEffect("Regeneration", 5, 3));
    }
}
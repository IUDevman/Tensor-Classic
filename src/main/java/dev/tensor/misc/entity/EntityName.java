package dev.tensor.misc.entity;

/**
 * @author Hoosiers
 * @since 03-31-2026
 */

public final class EntityName {

    public static String convert(String original) {

        if (original.startsWith("Entity")) {

            String replace = original.replace("Entity", "");

            if (replace.equalsIgnoreCase("PigZombie")) {
                return "Zombie Pigman";
            }

            if (replace.equalsIgnoreCase("TNTPrimed")) {
                return "TNT Primed";
            }

            if (replace.equalsIgnoreCase("FallingSand")) {
                return "Falling Sand";
            }

            return replace;

        } else {
            switch (original) {
                case "class_313":
                    return "Chicken";
                case "class_466":
                    return "Cow";
                case "class_443":
                    return "Pig";
                case "class_613":
                    return "Sheep";
                case "class_151":
                    return "Squid";
                case "class_121":
                    return "Wolf";
                case "class_114":
                    return "Creeper";
                case "class_364":
                    return "Ghast";
                case "class_535":
                    return "Zombie Pigman";
                case "class_90":
                    return "Skeleton";
                case "class_451":
                    return "Slime";
                case "class_558":
                    return "Spider";
                case "class_480":
                    return "Zombie";
                case "class_410":
                    return "Arrow";
                case "class_113":
                    return "Boat";
                case "class_499":
                    return "Egg";
                case "class_226":
                    return "Falling Sand";
                case "class_372":
                    return "Fireball";
                case "class_135":
                    return "Fish";
                case "class_549":
                    return "Minecart";
                case "class_107":
                    return "Painting";
                case "class_484":
                    return "Snowball";
                case "class_365":
                    return "TNT Primed";
            }

            return original;
        }
    }
}

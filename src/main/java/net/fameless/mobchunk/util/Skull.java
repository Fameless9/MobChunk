package net.fameless.mobchunk.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.MalformedURLException;
import java.util.UUID;

public enum Skull {

    NUMBER_0("MGViZTdlNTIxNTE2OWE2OTlhY2M2Y2VmYTdiNzNmZGIxMDhkYjg3YmI2ZGFlMjg0OWZiZTI0NzE0YjI3In19fQ=="),
    NUMBER_1("NzFiYzJiY2ZiMmJkMzc1OWU2YjFlODZmYzdhNzk1ODVlMTEyN2RkMzU3ZmMyMDI4OTNmOWRlMjQxYmM5ZTUzMCJ9fX0="),
    NUMBER_2("NGNkOWVlZWU4ODM0Njg4ODFkODM4NDhhNDZiZjMwMTI0ODVjMjNmNzU3NTNiOGZiZTg0ODczNDE0MTk4NDcifX19"),
    NUMBER_3("MWQ0ZWFlMTM5MzM4NjBhNmRmNWU4ZTk1NTY5M2I5NWE4YzNiMTVjMzZiOGI1ODc1MzJhYzA5OTZiYzM3ZTUifX19"),
    NUMBER_4("ZDJlNzhmYjIyNDI0MjMyZGMyN2I4MWZiY2I0N2ZkMjRjMWFjZjc2MDk4NzUzZjJkOWMyODU5ODI4N2RiNSJ9fX0="),
    NUMBER_5("NmQ1N2UzYmM4OGE2NTczMGUzMWExNGUzZjQxZTAzOGE1ZWNmMDg5MWE2YzI0MzY0M2I4ZTU0NzZhZTIifX19"),
    NUMBER_6("MzM0YjM2ZGU3ZDY3OWI4YmJjNzI1NDk5YWRhZWYyNGRjNTE4ZjVhZTIzZTcxNjk4MWUxZGNjNmIyNzIwYWIifX19"),
    NUMBER_7("NmRiNmViMjVkMWZhYWJlMzBjZjQ0NGRjNjMzYjU4MzI0NzVlMzgwOTZiN2UyNDAyYTNlYzQ3NmRkN2I5In19fQ=="),
    NUMBER_8("NTkxOTQ5NzNhM2YxN2JkYTk5NzhlZDYyNzMzODM5OTcyMjI3NzRiNDU0Mzg2YzgzMTljMDRmMWY0Zjc0YzJiNSJ9fX0="),
    NUMBER_9("ZTY3Y2FmNzU5MWIzOGUxMjVhODAxN2Q1OGNmYzY0MzNiZmFmODRjZDQ5OWQ3OTRmNDFkMTBiZmYyZTViODQwIn19fQ=="),
    BLANK("NWRiNTMyYjVjY2VkNDZiNGI1MzVlY2UxNmVjZWQ3YmJjNWNhYzU1NTk0ZDYxZThiOGY4ZWFjNDI5OWM5ZmMifX19"),
    COLON("Y2NiZWUyOGUyYzc5ZGIxMzhmMzk3N2JhNDcyZGZhZTZiMTFhOWJiODJkNWIzZDdmMjU0NzkzMzhmZmYxZmU5MiJ9fX0="),
    SLASH("N2Y5NWQ3YzFiYmYzYWZhMjg1ZDhkOTY3NTdiYjU1NzIyNTlhM2FlODU0ZjUzODlkYzUzMjA3Njk5ZDk0ZmQ4In19fQ=="),
    INFO("ZDAxYWZlOTczYzU0ODJmZGM3MWU2YWExMDY5ODgzM2M3OWM0MzdmMjEzMDhlYTlhMWEwOTU3NDZlYzI3NGEwZiJ9fX0="),
    QUESTION_MARK("M2ZkYWI0MDQzNGVkNWQwMWY1OGM0NWNhMGM5ZmFkYTQ2NjJlMTc3MmZmNDNlMjk3NDk3OTQ0MGE1Y2ZlMTVjOSJ9fX0="),
    HEART("OWNhYTc0NDZiZTg3NGJmMWExNGVkMWZjZWZjYzBhMDkyMzM2YzFiZjViZjQ2NWU5ZDI1NGRlZTM0YWZlYTc5In19fQ=="),
    ARROW_UP("NDVjNTg4YjllYzBhMDhhMzdlMDFhODA5ZWQwOTAzY2MzNGMzZTNmMTc2ZGM5MjIzMDQxN2RhOTNiOTQ4ZjE0OCJ9fX0="),
    ARROW_DOWN("MWNiOGJlMTZkNDBjMjVhY2U2NGUwOWY2MDg2ZDQwOGViYzNkNTQ1Y2ZiMjk5MGM1YjZjMjVkYWJjZWRlYWNjIn19fQ=="),
    TRAFFIC_LIGHT("YjBiZjgwYjcxNDJhMmQwNzZlNGMxMzM3N2JhZDk3NzhlMjJhNzRjMzA5ZjE1M2MwMzI0ZTIxYTRiNWI5NjVhIn19fQ=="),
    FLAG_UK("YzQzOWQ3ZjljNjdmMzJkY2JiODZiNzAxMGIxZTE0YjYwZGU5Njc3NmEzNWY2MWNlZTk4MjY2MGFhY2Y1MjY0YiJ9fX0="),
    FLAG_GERMANY("NWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ==");

    private final ItemStack itemStack;

    Skull(String texture) {
        try {
            itemStack = getSkull(texture);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemStack getAsItemStack() {
        return itemStack;
    }

    private ItemStack getSkull(String texture) throws MalformedURLException {
        String prefix = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", prefix + texture));
        meta.setPlayerProfile(profile);

        head.setItemMeta(meta);
        return head;
    }
}

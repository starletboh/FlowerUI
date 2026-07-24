package me.starletboh.flowerui.fabric.ref


import me.starletboh.flowerui.ref.UtilRef
import net.minecraft.util.Util
import java.net.URI

class MinecraftUtil : UtilRef{
    override fun getOperatingSystem() : Any{
        return Util.getPlatform()
    }

    override fun getOperatingSystemOpenURL(url: String): Any {
        return Util.getPlatform().openUri(URI.create(url))
    }

}
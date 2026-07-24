package me.starletboh.flowerui.fabric.ref


import me.starletboh.flowerui.ref.UtilRef
import net.minecraft.util.Util
import java.net.URI

class MinecraftUtil : UtilRef{
    override fun getOperatingSystem() : Util.OperatingSystem? {
        return Util.getOperatingSystem()
    }

    override fun getOperatingSystemOpenURL(url: String): Any {
        return Util.getOperatingSystem().open(URI.create(url))
    }

}
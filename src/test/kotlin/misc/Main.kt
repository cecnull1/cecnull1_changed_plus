package misc

fun main(args: Array<String>) {
    cpw.mods.bootstraplauncher.BootstrapLauncher.main(arrayOf(
        "--launchTarget", "forgeserver",
        "--version", "1.18.2",
        "--gameDir", ".",
        "--fml.forgeVersion", "40.2.0", // 替换为你的Forge版本
        "--fml.mcVersion", "1.18.2",
        "--fml.forgeGroup", "net.minecraftforge",
        "--accessToken", "0"
    ))
}
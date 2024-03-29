![Simple Museum](./src/main/resources/simplemuseum.png)

[![CurseForge](https://img.shields.io/badge/CurseForge-1.4.0-E04E14?logo=CurseForge)](https://www.curseforge.com/minecraft/mc-mods/simple-museum) [![Minecraft Version](https://img.shields.io/badge/Minecraft-1.16-blue)](https://www.minecraft.net) [![Forge Version](https://img.shields.io/badge/Forge-36.1.0-blue)](https://files.minecraftforge.net) [![GeckoLib Version](https://img.shields.io/badge/GeckoLib-3.0.31-blue)](https://www.curseforge.com/minecraft/mc-mods/geckolib)

Allows you to load and display static arbitrary GeckoLib models via resource packs. Primarily useful for testing things
in-game or creating displays/showcases.

Use the "Curator's Cane" (found in the "tools" creative tab) to create a puppet. Use the cane on the puppet to open its
settings, or hit the puppet with the cane to remove the puppet.

Custom models can be loaded via resource packs (server packs are recommended). Use whatever you like for the namespace
(the `/assets/<namespace-here>/`folder). I recommend either using this mod's ID (`simplemuseum`) or coming up with
something else. Note that using an existing namespace (like `minecraft`) could result in overriding textures/models if a
given namespace is already using the defined paths.

In the future, I would like servers to be able to send custom resources to clients without worrying about the 50mb
limit, but we'll cross that bridge when we come to it.

---

Feel free to use this in modpacks as desired. Check the license for details if you want to modify or add to this
project.

This project is still rough around the edges, please report issues as you find them or request features/polish if
desired.

---

Thanks to @jordanimating for requesting this project, and thanks to @TofuToons for giving me ideas and helping me test.

package dev.zihasz.zware;

import dev.xdark.ssbus.Bus;
import dev.zihasz.zware.event.Event;
import dev.zihasz.zware.event.EventProcessor;
import dev.zihasz.zware.manager.*;
import dev.zihasz.zware.mixin.MixinLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = ZWare.MOD_ID, name = ZWare.MOD_NAME, version = ZWare.MOD_VERSION)
public class ZWare {

	public static final String MOD_ID = "zware";
	public static final String MOD_NAME = "ZWare.cc";
	public static final String MOD_VERSION = "1.0.0";
	public static final String MOD_VERSION_PREFIX = "v";

	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	public static final Bus<Event> BUS = new Bus<>(Event.class);

	public static MixinLoader mixinLoader;
	public static EventProcessor eventProcessor;

	public static ClientManager clientManager;
	public static ConfigManager configManager;
	public static CapeManager capeManager;
	public static CommandManager commandManager;
	public static ModuleManager moduleManager;

	/**
	 * Load mixins, security here.
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		mixinLoader = new MixinLoader();
		eventProcessor = new EventProcessor();
	}

	/**
	 * Load features here. Also if you are not sure just load things here.
	 */
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		// Tracking.onStarted();

		clientManager = new ClientManager();
		commandManager = new CommandManager();
		moduleManager = new ModuleManager();
		configManager = new ConfigManager();

		ConfigManager.load();

		Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::save));
		// Runtime.getRuntime().addShutdownHook(new Thread(Tracking::onStopped));
	}

	/**
	 * Load cosmetics and things like that here.
	 */
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Display.setTitle(MOD_NAME + " " + MOD_VERSION_PREFIX + MOD_VERSION);
		capeManager = new CapeManager();
	}

}

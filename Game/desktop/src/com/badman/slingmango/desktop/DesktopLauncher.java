package com.badman.slingmango.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badman.slingmango.main.SlingMango;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
<<<<<<< HEAD
		new LwjglApplication(new SlingMango(), config);
=======
		config.title = "SlingMango";
		new LwjglApplication(new SlingName(), config);
>>>>>>> Badman
	}
}

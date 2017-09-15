package controller;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import javax.sound.sampled.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import gui.sprites.Textures;
import model.*;

/**
 * Bietet statische Methoden zum Laden und Speichern von Spielständen, und der
 * Konfiguration des Programms. </br>
 * Zudem bietet es Online-Konnektivität Dateienpfade sind relativ
 * 
 * @author Miran Shefketi
 */
public class IOController {
	/**
	 * java.util.Properties Objekt, welches die Eigenschaften des Programms
	 * enthält
	 */
	private static Properties mpcProperties = defaultProperties();

	/**
	 * Initialisiert die Eigenschaften des Programms auf die gewünschten
	 * Standardwerte
	 * 
	 * @return Ein aus Standardwerte initialisiertes Properties-Objekt
	 */
	private static Properties defaultProperties() {
		Properties defaultProps = new Properties();
		defaultProps.setProperty("PROPERTIES_PATH", "./MPC-Application.properties");
		defaultProps.setProperty("STATS_PATH", "./myStatistics.stats");
		defaultProps.setProperty("ZANK_VS_AI_PATH", "rsc/gamesaves/AGGRO_AI_PATIENCE.mpcs");
		defaultProps.setProperty("ZANK_VS_HUMAN_PATH", "rsc/gamesaves/AGGRO_ONLINE_PATIENCE.mpcs");
		defaultProps.setProperty("ZANK_FREE_PATH", "rsc/gamesaves/AGGRO_FREE_PATIENCE.mpcs");

		defaultProps.setProperty("IDIOT_PATH", "rsc/gamesaves/IDIOT_PATIENCE.mpcs");
		defaultProps.setProperty("FREECELL_PATH", "rsc/gamesaves/FREECELL.mpcs");
		defaultProps.setProperty("cardSkin", Textures.CardSkin.STANDARD.toString());
		defaultProps.setProperty("coins", "1000");
		defaultProps.setProperty("masterVolume", "75");
		defaultProps.setProperty("lastSound", "");
		return defaultProps;
	}

	/**
	 * Speichert die momentanen Einstellungen des Programms im PROPERTIES_PATH
	 * als "MPC-Application.properties"
	 */
	public static void saveProperties() {
		File propertiesFile = new File(mpcProperties.getProperty("PROPERTIES_PATH"));
		try {
			if (!propertiesFile.exists()) {
				propertiesFile.createNewFile();
			}
			FileWriter propertiesWriter = new FileWriter(propertiesFile);
			mpcProperties.store(propertiesWriter, "Konfigurationsdatei für die Macrohard Patience Collection 2017");
			propertiesWriter.close();
		} catch (IOException ioe) {
			System.err.println("IOException bei saveProperties!");
			ioe.printStackTrace();
		}
	}

	/**
	 * Lädt die momentanen Einstellungen des Programms aus
	 * "MPC-Application.properties" in PROPERTIES_PATH
	 */
	public static void loadProperties() {
		File propertiesFile = new File(mpcProperties.getProperty("PROPERTIES_PATH"));
		if (propertiesFile.exists()) {
			try (BufferedReader propertiesReader = new BufferedReader(new FileReader(propertiesFile))) {
				mpcProperties.load(propertiesReader);
			} catch (IOException ioe) {
				System.err.println("IOException bei loadProperties!");
			}
		} else {
			saveProperties();
		}
	}

	/**
	 * Wrapper Methode für Properties als anti-Spaghetti Maßnahme
	 * 
	 * @return Der angefragte String-Wert unter dem angegebenen Key
	 */
	public static String getProperty(String key) {
		return mpcProperties.getProperty(key);
	}

	/**
	 * Wrapper Methode für Properties als anti-Spaghetti Maßnahme
	 * 
	 * @param key
	 *            der Schlüssel unter welchem der String-Wert eingetragen werden
	 *            soll
	 * @param value
	 *            Der String-Wert der unter dem key als Property eingetragen
	 *            werden soll
	 */
	public static void setProperty(String key, String value) {
		mpcProperties.setProperty(key, value);
	}

	/**
	 * Speichert Das gegebene Battlefield-Objekt in eine Datei mit .json-Format
	 * im angegebenen Pfad
	 * 
	 * @param battlefield
	 *            Das zu speichernde Battlefield-Objekt
	 * @param path
	 *            Der (relative?) Pfad, in dem der Spielstand gespeichert werden
	 *            soll
	 * @throws IOException,
	 *             falls die Datei nicht erstellt werden kann. (Zugriffsrecht
	 *             oder Festplattenplatz)
	 */
	public static void saveGame(Battlefield battleField, String path) throws IOException {
		saveJson(battleField, Battlefield.class, path);
	}

	/**
	 * Speichert Das gegebene Statistik-Objekt in eine Datei mit .json-Format im
	 * angegebenen Pfad
	 * 
	 * @param src
	 *            Das zu speichernde Statistik-Objekt
	 * @param path
	 *            Der (relative?) Pfad, in dem die Statistik gespeichert werden
	 *            soll
	 * @throws IOException,
	 *             falls die Datei nicht erstellt werden kann. (Zugriffsrecht
	 *             oder Festplattenplatz)
	 */
	public static void saveStats(GameStatistics stats, String path) throws IOException {
		saveJson(stats, GameStatistics.class, path);
	}

	/**
	 * Speichert Das gegebene Objekt in eine Datei mit .json-Format im
	 * angegebenen Pfad
	 * 
	 * @param src
	 *            Das zu speichernde Objekt
	 * @param typeOfSrc
	 *            Der Klassentyp des src-Objekts
	 * @param path
	 *            Der (relative?) Pfad, in dem das Objekt gespeichert werden
	 *            soll
	 * @throws IOException,
	 *             falls die Datei nicht erstellt werden kann. (Zugriffsrecht
	 *             oder Festplattenplatz)
	 */
	private static void saveJson(Object src, Type typeOfSrc, String path) throws IOException {
		Gson gson = new Gson();
		File saveFile = new File(path);
		String jsonString = gson.toJson(src, typeOfSrc);
		try (FileWriter jsonWriter = new FileWriter(saveFile)) {
			if (!saveFile.exists()) {
				saveFile.mkdirs();
				saveFile.createNewFile();
			}
			jsonWriter.write(jsonString);
			jsonWriter.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Lädt ein Battlefield-Objekt aus einer entsprechenden Datei im .json
	 * Format
	 * 
	 * @param path
	 *            Pfad der Datei, aus welcher ein Battlefield-Objekt zu lesen
	 *            ist.
	 * @return Ein gültiges Battlefield-Objekt, welches aus der angegebenen
	 *         Datei geladen wurde.
	 * @throws IOException
	 *             falls die zu ladende Datei nicht gefunden wurde oder keinen
	 *             gültigen Spielstand enthält
	 */
	public static Battlefield loadGame(String path) throws IOException {
		Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<EnumMap<CardStackType, CardStack>>() {
		}.getType(), new EnumMapInstanceCreator<CardStackType, CardStack>(CardStackType.class)).create();
		Battlefield loadedGame;
		try {
			loadedGame = gson.fromJson(loadString(path), Battlefield.class);
			return loadedGame;
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (JsonSyntaxException jse) {
			throw new IOException("Spielstand konnte nicht gelesen werden. \nDie Datei könnte korrupt sein.");
		}
		return null;
	}

	/**
	 * Lädt ein Statistik-Objekt aus einer entsprechenden Datei im .json Format
	 * 
	 * @param path
	 *            Pfad der Datei, aus welcher ein Statistik-Objekt zu lesen ist.
	 * @return Ein gültiges Statistik-Objekt, welches aus der angegebenen Datei
	 *         geladen wurde.
	 * @throws IOException
	 *             falls die zu ladende Datei nicht gefunden wurde oder keine
	 *             gültige Statistik enthält
	 */
	public static GameStatistics loadStats(String path) throws IOException {
		Gson gson = new Gson();
		GameStatistics loadedStats;
		loadedStats = gson.fromJson(loadString(path), GameStatistics.class);
		return loadedStats;
	}

	/**
	 * Lädt ein Json-String aus einer entsprechenden Datei im .json Format
	 * 
	 * @param path
	 *            Pfad der Datei, aus welcher ein Json-Objekt zu lesen ist.
	 * @return Ein Json-String, welcher aus der angegebenen Datei geladen wurde.
	 * @throws IOException
	 *             falls die zu ladende Datei nicht gefunden wurde oder kein
	 *             gültiges Json enthält
	 */
	private static String loadString(String path) throws FileNotFoundException {
		File saveFile = new File(path);
		if (!saveFile.exists()) {
			throw new FileNotFoundException(saveFile.toString() + " konnte nicht gefunden werden!");
		}
		StringBuilder jsonStringBuilder = new StringBuilder();
		try (BufferedReader jsonReader = new BufferedReader(new FileReader(saveFile))) {
			String nextLine = jsonReader.readLine();
			while (nextLine != null) {
				jsonStringBuilder.append(nextLine);
				nextLine = jsonReader.readLine();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return jsonStringBuilder.toString();
	}

	public static void playSound(String url) {
		IOController.playSound(url, 0.0f);
	}

	public static void playSound(String url, float gain) {
		// Thread audioThread = new Thread(() -> {
		try (AudioInputStream inputStream = AudioSystem
				.getAudioInputStream(IOController.class.getResourceAsStream("/sounds/" + url))) {
			Clip clip = AudioSystem.getClip();
			clip.addLineListener((event) -> {
				if (event.getType().equals(LineEvent.Type.STOP)) {
					clip.close();
				}
			});
			clip.open(inputStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float volume = Float.parseFloat(IOController.getProperty("masterVolume")) / 100.0f;
			float range = (gainControl.getMaximum() - gainControl.getMinimum());
			float newGain = (Math.max(gainControl.getMinimum(),
					gainControl.getMinimum() + Math.min(range, range * volume + gain)));
			gainControl.setValue(newGain);
			clip.start();
		} catch (IOException ioe) {
			System.err.println("Audiodatei konnte nicht geöffnet werden!");
			// ioe.printStackTrace();
		} catch (UnsupportedAudioFileException uafe) {
			System.err.println("Das Format der Audiodatei ist nicht unterstützt!");
			// uafe.printStackTrace();
		} catch (LineUnavailableException lue) {
			System.err.println("Es ist keine passende Audio Line verfügbar!");
			// lue.printStackTrace();
		}
		// });
		// audioThread.setName("Audio Thread");
		// audioThread.setDaemon(true);
		// audioThread.start();
	}

}

class EnumMapInstanceCreator<K extends Enum<K>, V> implements InstanceCreator<EnumMap<K, V>> {
	private final Class<K> enumClazz;

	public EnumMapInstanceCreator(final Class<K> enumClazz) {
		super();
		this.enumClazz = enumClazz;
	}

	@Override
	public EnumMap<K, V> createInstance(final Type type) {
		return new EnumMap<K, V>(enumClazz);
	}
}
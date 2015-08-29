package ionium.templates;

import ionium.registry.AssetRegistry;
import ionium.registry.ErrorLogRegistry;
import ionium.registry.ScreenRegistry;
import ionium.screen.AssetLoadingScreen;
import ionium.screen.MainMenuScreen;
import ionium.screen.Updateable;
import ionium.transition.TrainDoors;
import ionium.transition.Transition;
import ionium.transition.TransitionScreen;
import ionium.util.AssetMap;
import ionium.util.CaptureStream;
import ionium.util.GameException;
import ionium.util.Logger;
import ionium.util.MathHelper;
import ionium.util.MemoryUtils;
import ionium.util.ScreenshotFactory;
import ionium.util.SpecialCharactersList;
import ionium.util.Splashes;
import ionium.util.Translator;
import ionium.util.Utils;
import ionium.util.CaptureStream.Consumer;
import ionium.util.render.Gears;
import ionium.util.render.Shaders;
import ionium.util.version.VersionGetter;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;

/**
 * 
 * Main class, think of it like slick's Main class
 *
 */
public abstract class Main extends Game implements Consumer {

	public static OrthographicCamera camera;

	public SpriteBatch batch;

	public ShapeRenderer shapes;
	
	public ImmediateModeRenderer20 verticesRenderer;

	public BitmapFont font;
	public BitmapFont arial;

	private static Color rainbow = new Color();
	private static Color inverseRainbow = new Color();

	public static final String version = "v0.1.0-alpha";
	public static String githubVersion = null;

	public static Texture filltex;
	public static TextureRegion filltexRegion;
	public static Pixmap clearPixmap;

	public ShaderProgram maskshader;
	public ShaderProgram greyshader;
	public ShaderProgram warpshader;
	public ShaderProgram blurshader;
	public static ShaderProgram defaultShader;
	public ShaderProgram invertshader;
	public ShaderProgram distanceFieldShader;
	public static ShaderProgram meshShader;
	public ShaderProgram maskNoiseShader;

	private CaptureStream output;
	private PrintStream printstrm;
	private JFrame consolewindow;
	private JTextArea consoletext;
	private JScrollPane conscrollPane;

	private int[] lastFPS = new int[5];
	private long nanoUntilTick = Settings.TICKS_NANO;
	private long lastKnownNano = System.nanoTime();
	public float totalSeconds = 0f;
	private long totalTicksElapsed = 0;
	private long lastTickDurationNano = 0;

	public static Gears gears;

	/**
	 * use this rather than Gdx.app.log
	 */
	public static Logger logger;

	public Main(Logger l) {
		super();
		logger = l;
	}

	@Override
	public void create() {
		Gdx.graphics.setTitle(getTitle() + " - " + Splashes.getRandomSplash());
		redirectSysOut();

		for (int i = 0; i < lastFPS.length; i++) {
			lastFPS[i] = 0;
		}

		ShaderProgram.pedantic = false;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		batch.enableBlending();
		
		verticesRenderer = new ImmediateModeRenderer20(false, true, 0);
		
		defaultShader = SpriteBatch.createDefaultShader();
		AssetRegistry.createMissingTexture();

		loadFont();

		arial = new BitmapFont();
		arial.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Pixmap pix = new Pixmap(1, 1, Format.RGBA8888);
		pix.setColor(Color.WHITE);
		pix.fill();
		filltex = new Texture(pix);
		pix.dispose();
		filltexRegion = new TextureRegion(filltex);
		clearPixmap = new Pixmap(8, 8, Format.RGBA8888);
		clearPixmap.setColor(0, 0, 0, 0);
		clearPixmap.fill();
		
		shapes = new ShapeRenderer();

		maskshader = new ShaderProgram(Shaders.VERTDEFAULT, Shaders.FRAGBAKE);
		maskshader.begin();
		maskshader.setUniformi("u_mask", 1);
		maskshader.end();
		
		greyshader = new ShaderProgram(Shaders.VERTGREY, Shaders.FRAGGREY);

		warpshader = new ShaderProgram(Shaders.VERTDEFAULT, Shaders.FRAGWARP);
		warpshader.begin();
		warpshader.setUniformf(warpshader.getUniformLocation("time"), totalSeconds);
		warpshader.setUniformf(warpshader.getUniformLocation("amplitude"), 1.0f, 1.0f);
		warpshader.setUniformf(warpshader.getUniformLocation("frequency"), 1.0f, 1.0f);
		warpshader.setUniformf(warpshader.getUniformLocation("speed"), 1f);
		warpshader.end();

		blurshader = new ShaderProgram(Shaders.VERTBLUR, Shaders.FRAGBLUR);
		blurshader.begin();
		blurshader.setUniformf("dir", 1f, 0f);
		blurshader.setUniformf("resolution", Settings.DEFAULT_WIDTH);
		blurshader.setUniformf("radius", 2f);
		blurshader.end();
		
		maskNoiseShader = new ShaderProgram(Shaders.VERTDEFAULT, Shaders.FRAGBAKENOISE);

		invertshader = new ShaderProgram(Shaders.VERTINVERT, Shaders.FRAGINVERT);
		distanceFieldShader = new ShaderProgram(Shaders.VERTDISTANCEFIELD,
				Shaders.FRAGDISTANCEFIELD);
		meshShader = new ShaderProgram(Shaders.VERTMESH, Shaders.FRAGMESH);

		loadUnmanagedAssets();
		loadAssets();

		Gdx.input.setInputProcessor(getDefaultInput());

		prepareStates();

		this.setScreen(ScreenRegistry.get("assetloading"));

		new Thread("version checker") {

			@Override
			public void run() {
				VersionGetter.instance().getVersionFromServer();
			}
		}.start();

		// set resolution/fullscreen according to settings
		if (Gdx.graphics.getWidth() != Settings.actualWidth
				|| Gdx.graphics.getHeight() != Settings.actualHeight
				|| Gdx.graphics.isFullscreen() != Settings.fullscreen) {
			Gdx.graphics.setDisplayMode(Settings.actualWidth, Settings.actualHeight,
					Settings.fullscreen);
		}
	}

	public void prepareStates() {
		ScreenRegistry reg = ScreenRegistry.instance();
		reg.add("mainmenu", new MainMenuScreen(this));
		reg.add("assetloading", new AssetLoadingScreen(this));
		reg.add("transition", new TransitionScreen(this));
	}

	@Override
	public void dispose() {
		Settings.instance().save();

		batch.dispose();
		verticesRenderer.dispose();
		AssetRegistry.instance().dispose();
		font.dispose();
		arial.dispose();
		maskshader.dispose();
		warpshader.dispose();
		blurshader.dispose();
		invertshader.dispose();
		distanceFieldShader.dispose();
		meshShader.dispose();
		maskNoiseShader.dispose();
		shapes.dispose();
		clearPixmap.dispose();

		// dispose screens
		ScreenRegistry.instance().dispose();
	}

	private void preRender() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearDepthf(1f);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		gears.update(1);
	}

	@Override
	public void render() {
		totalSeconds += Gdx.graphics.getDeltaTime();
		nanoUntilTick += (System.nanoTime() - lastKnownNano);
		lastKnownNano = System.nanoTime();

		try {
			// ticks
			while (nanoUntilTick >= Settings.TICKS_NANO) {
				long nano = System.nanoTime();
				
				if (getScreen() != null) ((Updateable) getScreen()).tickUpdate();
				
				tickUpdate();
				
				lastTickDurationNano = System.nanoTime() - nano;
				
				nanoUntilTick -= Settings.TICKS_NANO;
			}
			
			// render updates
			if (getScreen() != null) {
				((Updateable) getScreen()).renderUpdate();
			}

			preRender();
			super.render();
			postRender();

		} catch (Exception e) {
			e.printStackTrace();

			Gdx.files.local("crash/").file().mkdir();
			FileHandle handle = Gdx.files.local("crash/crash-log_" + new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()).trim() + ".txt");
			
			handle.writeString(ErrorLogRegistry.instance().createErrorLog(output.toString()), false);

			resetSystemOut();
			System.out.println("\n\nThe game crashed. There is an error log at " + handle.path()
					+ " ; please send it to the game developer!\n");

			Gdx.app.exit();
			System.exit(1);
		}

	}

	private void postRender() {
		batch.begin();

		font.setColor(Color.WHITE);

		if (Settings.showFPS || Settings.debug) {
			font.draw(batch, "FPS: "
					+ (Gdx.graphics.getFramesPerSecond() <= (Settings.MAX_FPS / 4f) ? "[RED]"
							: (Gdx.graphics.getFramesPerSecond() <= (Settings.MAX_FPS / 2f) ? "[YELLOW]"
									: "")) + Gdx.graphics.getFramesPerSecond() + "[]", 5,
					Gdx.graphics.getHeight() - 5);
		}
		if (Settings.debug) {
			font.getData().markupEnabled = false;
			font.draw(
					batch,
					"(avg of " + lastFPS.length + " sec: " + String.format("%.1f", getAvgFPS())
							+ ") " + Arrays.toString(lastFPS),
					5 + font.getSpaceWidth()
							+ (Utils.getWidth(font, "FPS: " + Gdx.graphics.getFramesPerSecond())),
							Gdx.graphics.getHeight() - 5);
			font.getData().markupEnabled = true;
		}

		if (this.getScreen() != null) {
			if (Settings.debug) ((Updateable) this.getScreen()).renderDebug(this.renderDebug());
		}
		batch.end();

		fpstimer += Gdx.graphics.getDeltaTime();
		if (fpstimer >= 1) {
			fpstimer--;
			int[] temp = lastFPS.clone();
			for (int i = 1; i < lastFPS.length; i++) {
				lastFPS[i] = temp[i - 1];
			}
			lastFPS[0] = Gdx.graphics.getFramesPerSecond();
		}

		warpshader.begin();
		warpshader.setUniformf(warpshader.getUniformLocation("time"), totalSeconds);
		warpshader.setUniformf(warpshader.getUniformLocation("amplitude"), 0.25f, 0.1f);
		warpshader.setUniformf(warpshader.getUniformLocation("frequency"), 10f, 5f);
		warpshader.setUniformf(warpshader.getUniformLocation("speed"), 2.5f);
		warpshader.end();

		inputUpdate();
	}

	private int renderDebug() {
		int offset = 0;
		if (getScreen() != null) offset = ((Updateable) getScreen()).getDebugOffset();
		if (MemoryUtils.getUsedMemory() > getMostMemory) getMostMemory = MemoryUtils
				.getUsedMemory();
		font.setColor(Color.WHITE);
		font.draw(batch, "version: " + Main.version
				+ (githubVersion == null ? "" : "; latest: " + Main.githubVersion), 5,
				Main.convertY(font.getCapHeight() * 2 + offset));
		font.draw(batch, "memory: "
				+ NumberFormat.getInstance().format(MemoryUtils.getUsedMemory()) + " KB / "
				+ NumberFormat.getInstance().format(MemoryUtils.getMaxMemory()) + " KB (max "
				+ NumberFormat.getInstance().format(getMostMemory) + " KB) ", 5,
				Main.convertY(font.getCapHeight() * 3 + offset));
		font.draw(batch, "OS: " + System.getProperty("os.name") + ", " + MemoryUtils.getCores() + " cores", 5,
				Main.convertY(font.getCapHeight() * 4 + offset));
		font.draw(batch, "tickDuration: " + (lastTickDurationNano / 1000000f) + " ms", 5, Main.convertY(font.getCapHeight() * 5 + offset));
		font.draw(batch, "delta: " + Gdx.graphics.getDeltaTime(), 5, Main.convertY(font.getCapHeight() * 6 + offset));
		if (getScreen() != null) {
			font.draw(batch, "state: " + getScreen().getClass().getSimpleName(), 5,
					Main.convertY(font.getCapHeight() * 7 + offset));
		} else {
			font.draw(batch, "state: null", 5, Main.convertY(font.getCapHeight() * 8 + offset));
		}

		return 30 + offset + 105;
	}

	public void inputUpdate() {
		if (Gdx.input.isKeyJustPressed(Keys.F12)) {
			Settings.debug = !Settings.debug;
		} else if (Gdx.input.isKeyJustPressed(Keys.F1)) {
			ScreenshotFactory.saveScreenshot();
		}
		if (Settings.debug) { // console things -> alt + key
			if (((Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT)))) {
				if (Gdx.input.isKeyJustPressed(Keys.C)) {
					if (consolewindow.isVisible()) {
						consolewindow.setVisible(false);
					} else {
						consolewindow.setVisible(true);
						conscrollPane.getVerticalScrollBar().setValue(
								conscrollPane.getVerticalScrollBar().getMaximum());
					}
				} else if (Gdx.input.isKeyJustPressed(Keys.Q)) {
					throw new GameException(
							"This is a forced crash caused by pressing ALT+Q while in debug mode.");
				} else if (Gdx.input.isKeyJustPressed(Keys.G)) {
					gears.reset();
					
					// FIXME
					this.transition(new TrainDoors(true), new TrainDoors(false), ScreenRegistry.get("mainmenu"));
				}

			}
		}
	}

	public void tickUpdate() {
		
	}
	
	public void loadFont(){
		FreeTypeFontGenerator ttfGenerator = new FreeTypeFontGenerator(
				Gdx.files.internal("fonts/minecraft.ttf"));
		FreeTypeFontParameter ttfParam = new FreeTypeFontParameter();
		ttfParam.magFilter = TextureFilter.Nearest;
		ttfParam.minFilter = TextureFilter.Nearest;
		ttfParam.genMipMaps = true;
		ttfParam.size = 16; // 14 for my font, 16 for Osaka font/minecraft font
		ttfParam.characters += SpecialCharactersList.getJapaneseKana();
		font = ttfGenerator.generateFont(ttfParam);
		font.getData().markupEnabled = true;
		
		ttfGenerator.dispose();
	}

	private void loadAssets() {
		AssetMap.instance(); // load asset map namer thing
		Translator.instance();
		addColors();
		
		// the default assets are already added in StandardAssetLoader
	}

	private void loadUnmanagedAssets() {
		long timeTaken = System.currentTimeMillis();
		
		AssetRegistry.instance().loadUnmanagedTextures();
		
		// load gears instance (used in loading screen)
		gears = new Gears(this);

		Main.logger.info("Finished loading all unmanaged assets, took "
				+ (System.currentTimeMillis() - timeTaken) + " ms");
	}

	private void addColors() {

	}

	public static String getRandomUsername() {
		return "Player" + MathUtils.random(9999);
	}

	public static String getTitle() {
		return (Translator.getMsg("gamename") + " " + Main.version);
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		shapes.setProjectionMatrix(Main.camera.combined);
		
		for(Updateable up : ScreenRegistry.instance().getAll()){
			up.container.onResize();
		}
	}

	public void redirectSysOut() {
		PrintStream ps = System.out;
		output = new CaptureStream(this, ps);
		printstrm = new PrintStream(output);
		resetConsole();
		System.setOut(printstrm);
	}

	public void resetConsole() {
		consolewindow = new JFrame();
		consolewindow.setTitle("Console for " + Translator.getMsg("gamename") + " " + Main.version);
		consolewindow.setVisible(false);
		consoletext = new JTextArea(40, 60);
		consoletext.setEditable(false);
		conscrollPane = new JScrollPane(consoletext);
		consolewindow.add(conscrollPane, null);
		consolewindow.pack();
	}

	public void resetSystemOut() {
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}

	@Override
	public void appendText(final String text) {
		consoletext.append(text);
		consoletext.setCaretPosition(consoletext.getText().length());
	}

	public void transition(Transition from, Transition to, Screen next) {
		TransitionScreen transition = ScreenRegistry.get("transition", TransitionScreen.class);
		
		transition.prepare(this.getScreen(), from, to, next);
		setScreen(transition);
	}

	public static Color getRainbow() {
		return getRainbow(System.currentTimeMillis(), 1, 1);
	}

	public static Color getRainbow(float s) {
		return getRainbow(System.currentTimeMillis(), s, 1);
	}

	public static Color getRainbow(long ms, float s, float saturation) {
		return rainbow.set(
				Utils.HSBtoRGBA8888(
						(s < 0 ? 1.0f : 0) - MathHelper.getNumberFromTime(ms, Math.abs(s)),
						saturation, 0.75f)).clamp();
	}

	public InputMultiplexer getDefaultInput() {
		InputMultiplexer plexer = new InputMultiplexer();
		plexer.addProcessor(new MainInputProcessor(this));
		return plexer;
	}

	private static Random random = new Random();

	public static Random getRandom() {
		return random;
	}

	public static void fillRect(Batch batch, float x, float y, float width, float height) {
		batch.draw(filltex, x, y, width, height);
	}

	private static float[] gradientverts = new float[20];
	private static Color tempGradientColor = new Color();

	public static void drawGradient(SpriteBatch batch, float x, float y, float width, float height,
			Color bl, Color br, Color tr, Color tl) {
		tempGradientColor.set((bl.r + br.r + tr.r + tl.r) / 4f, (bl.g + br.g + tr.g + tl.g) / 4f,
				(bl.b + br.b + tr.b + tl.b) / 4f, (bl.a + br.a + tr.a + tl.a) / 4f);

		int idx = 0;

		// draw bottom face
		idx = 0;
		gradientverts[idx++] = x + (width / 2);
		gradientverts[idx++] = y + (height / 2);
		gradientverts[idx++] = tempGradientColor.toFloatBits(); // middle
		gradientverts[idx++] = 0.5f;
		gradientverts[idx++] = 0.5f;

		gradientverts[idx++] = x;
		gradientverts[idx++] = y;
		gradientverts[idx++] = bl.toFloatBits(); // bottom left
		gradientverts[idx++] = filltexRegion.getU(); //NOTE: texture coords origin is top left
		gradientverts[idx++] = filltexRegion.getV2();

		gradientverts[idx++] = x + width;
		gradientverts[idx++] = y;
		gradientverts[idx++] = br.toFloatBits(); // bottom right
		gradientverts[idx++] = filltexRegion.getU2();
		gradientverts[idx++] = filltexRegion.getV2();

		gradientverts[idx++] = x + (width / 2);
		gradientverts[idx++] = y + (height / 2);
		gradientverts[idx++] = tempGradientColor.toFloatBits(); // middle
		gradientverts[idx++] = 0.5f;
		gradientverts[idx++] = 0.5f;

		batch.draw(filltexRegion.getTexture(), gradientverts, 0, gradientverts.length);

		// draw top face
		idx = 0;
		gradientverts[idx++] = x + (width / 2);
		gradientverts[idx++] = y + (height / 2);
		gradientverts[idx++] = tempGradientColor.toFloatBits(); // middle
		gradientverts[idx++] = 0.5f;
		gradientverts[idx++] = 0.5f;

		gradientverts[idx++] = x;
		gradientverts[idx++] = y + height;
		gradientverts[idx++] = tl.toFloatBits(); // top left
		gradientverts[idx++] = filltexRegion.getU();
		gradientverts[idx++] = filltexRegion.getV();

		gradientverts[idx++] = x + width;
		gradientverts[idx++] = y + height;
		gradientverts[idx++] = tr.toFloatBits(); // top right
		gradientverts[idx++] = filltexRegion.getU2();
		gradientverts[idx++] = filltexRegion.getV();

		gradientverts[idx++] = x + (width / 2);
		gradientverts[idx++] = y + (height / 2);
		gradientverts[idx++] = tempGradientColor.toFloatBits(); // middle
		gradientverts[idx++] = 0.5f;
		gradientverts[idx++] = 0.5f;

		batch.draw(filltexRegion.getTexture(), gradientverts, 0, gradientverts.length);

		// draw left face
		idx = 0;
		gradientverts[idx++] = x + (width / 2);
		gradientverts[idx++] = y + (height / 2);
		gradientverts[idx++] = tempGradientColor.toFloatBits(); // middle
		gradientverts[idx++] = 0.5f;
		gradientverts[idx++] = 0.5f;

		gradientverts[idx++] = x;
		gradientverts[idx++] = y + height;
		gradientverts[idx++] = tl.toFloatBits(); // top left
		gradientverts[idx++] = filltexRegion.getU();
		gradientverts[idx++] = filltexRegion.getV();

		gradientverts[idx++] = x;
		gradientverts[idx++] = y;
		gradientverts[idx++] = bl.toFloatBits(); // bottom left
		gradientverts[idx++] = filltexRegion.getU(); //NOTE: texture coords origin is top left
		gradientverts[idx++] = filltexRegion.getV2();

		gradientverts[idx++] = x + (width / 2);
		gradientverts[idx++] = y + (height / 2);
		gradientverts[idx++] = tempGradientColor.toFloatBits(); // middle
		gradientverts[idx++] = 0.5f;
		gradientverts[idx++] = 0.5f;

		batch.draw(filltexRegion.getTexture(), gradientverts, 0, gradientverts.length);

		// draw right face
		idx = 0;
		gradientverts[idx++] = x + (width / 2);
		gradientverts[idx++] = y + (height / 2);
		gradientverts[idx++] = tempGradientColor.toFloatBits(); // middle
		gradientverts[idx++] = 0.5f;
		gradientverts[idx++] = 0.5f;

		gradientverts[idx++] = x + width;
		gradientverts[idx++] = y + height;
		gradientverts[idx++] = tr.toFloatBits(); // top right
		gradientverts[idx++] = filltexRegion.getU2();
		gradientverts[idx++] = filltexRegion.getV();

		gradientverts[idx++] = x + width;
		gradientverts[idx++] = y;
		gradientverts[idx++] = br.toFloatBits(); // bottom right
		gradientverts[idx++] = filltexRegion.getU2();
		gradientverts[idx++] = filltexRegion.getV2();

		gradientverts[idx++] = x + (width / 2);
		gradientverts[idx++] = y + (height / 2);
		gradientverts[idx++] = tempGradientColor.toFloatBits(); // middle
		gradientverts[idx++] = 0.5f;
		gradientverts[idx++] = 0.5f;

		batch.draw(filltexRegion.getTexture(), gradientverts, 0, gradientverts.length);

	}

	/**
	 * Call after the masking shader is set to mask a texture onto another stencil texture. Use with maskShader.
	 * 
	 * @param mask mask itself (generally base tex as well)
	 */
	public static void useMask(Texture mask) {
		mask.bind(1);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
	}

	/**
	 * converts y-down to y-up
	 * 
	 * @param f
	 *            the num. of px down from the top of the screen
	 * @return the y-down conversion of input
	 */
	public static int convertY(float f) {
		return Math.round(Gdx.graphics.getHeight() - f);
	}

	public void drawTextBg(BitmapFont font, String text, float x, float y, float wrapWidth, int align) {
		batch.setColor(0, 0, 0, batch.getColor().a * 0.6f);
		fillRect(batch, x, y, Utils.getWidth(font, text) + 2, (Utils.getHeight(font, text)) + 2);
		font.draw(batch, text, x + 1, y + font.getCapHeight(), wrapWidth, align, true);
		batch.setColor(1, 1, 1, 1);
	}
	
	public void drawTextBg(BitmapFont font, String text, float x, float y){
		drawTextBg(font, text, x, y, Utils.getWidth(font, text), Align.left);
	}

	private int totalavgFPS = 0;
	private float fpstimer = 0;

	public float getAvgFPS() {
		totalavgFPS = 0;
		for (int i = 0; i < lastFPS.length; i++) {
			totalavgFPS += lastFPS[i];
		}

		return ((totalavgFPS) / (lastFPS.length * 1f));
	}

	public int getMostMemory = MemoryUtils.getUsedMemory();

	/**
	 * basically appends gamename + "-" to the beginning of your preference
	 * 
	 * @param ref
	 * @return preferences
	 */
	public static Preferences getPref(String ref) {
		return Gdx.app.getPreferences("-" + ref);
	}

	public void setClearColor(int r, int g, int b) {
		Gdx.gl20.glClearColor(r / 255f, g / 255f, b / 255f, 1f);
	}
	
	public abstract String getPreferencePrefix();

}

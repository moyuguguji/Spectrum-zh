package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.particle_spawner.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.fabricmc.fabric.mixin.client.particle.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.texture.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import org.joml.*;
import org.lwjgl.glfw.*;

import java.lang.Math;
import java.util.*;
import java.util.function.*;

@Environment(EnvType.CLIENT)
public class ParticleSpawnerScreen extends HandledScreen<ParticleSpawnerScreenHandler> {
	
	protected static final Identifier GUI_TEXTURE = SpectrumCommon.locate("textures/gui/container/particle_spawner.png");
	protected static final int PARTICLES_PER_PAGE = 6;
	protected static final int TEXT_COLOR = 2236962;
	
	protected SpriteAtlasTexture spriteAtlasTexture;
	protected boolean glowing = false;
	protected boolean collisionsEnabled = false;
	protected int activeParticlePage = 0;
	protected int particleSelectionIndex = 0;
	protected boolean selectedParticleSupportsColoring = false;
	
	private final List<ClickableWidget> selectableWidgets = new ArrayList<>();
	private TextFieldWidget cyanField;
	private TextFieldWidget magentaField;
	private TextFieldWidget yellowField;
	private TextFieldWidget amountField;
	private TextFieldWidget positionXField;
	private TextFieldWidget positionYField;
	private TextFieldWidget positionZField;
	private TextFieldWidget positionXVarianceField;
	private TextFieldWidget positionYVarianceField;
	private TextFieldWidget positionZVarianceField;
	private TextFieldWidget velocityXField;
	private TextFieldWidget velocityYField;
	private TextFieldWidget velocityZField;
	private TextFieldWidget velocityXVarianceField;
	private TextFieldWidget velocityYVarianceField;
	private TextFieldWidget velocityZVarianceField;
	private TextFieldWidget scale;
	private TextFieldWidget scaleVariance;
	private TextFieldWidget duration;
	private TextFieldWidget durationVariance;
	private TextFieldWidget gravity;
	private ButtonWidget glowingButton;
	private ButtonWidget collisionsButton;
	private ButtonWidget backButton;
	private ButtonWidget forwardButton;
	private List<ButtonWidget> particleButtons;
	
	private List<ParticleSpawnerParticlesDataLoader.ParticleSpawnerEntry> displayedParticleEntries = new ArrayList<>();
	
	public ParticleSpawnerScreen(ParticleSpawnerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.titleX = 48;
		this.titleY = 7;
		this.backgroundHeight = 243;
	}
	
	@Override
	protected void init() {
		super.init();

		this.spriteAtlasTexture = ((ParticleManagerAccessor) client.particleManager).getParticleAtlasTexture();
		this.displayedParticleEntries = ParticleSpawnerParticlesDataLoader.getAllUnlocked(client.player);
		
		this.selectableWidgets.clear();
		setupInputFields(handler.getBlockEntity());
		setInitialFocus(amountField);
	}
	
	@Override
	public void handledScreenTick() {
		super.handledScreenTick();
		
		for (ClickableWidget widget : selectableWidgets) {
			if (widget instanceof TextFieldWidget textFieldWidget) {
				textFieldWidget.tick();
			}
		}
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			client.player.closeHandledScreen();
		}
		
		Element focusedElement = getFocused();
		if (focusedElement instanceof TextFieldWidget focusedTextFieldWidget) {
			if (keyCode == GLFW.GLFW_KEY_TAB) {
				int currentIndex = selectableWidgets.indexOf(focusedElement);
				focusedTextFieldWidget.setFocused(false);
				
				if (modifiers == 1) {
					setFocused(selectableWidgets.get((selectableWidgets.size() + currentIndex - 1) % selectableWidgets.size()));
				} else {
					setFocused(selectableWidgets.get((currentIndex + 1) % selectableWidgets.size()));
				}
			}
			return focusedElement.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
		}
		
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext);
		super.render(drawContext, mouseX, mouseY, delta);
		
		RenderSystem.disableBlend();
		renderForeground(drawContext, mouseX, mouseY, delta);
		drawMouseoverTooltip(drawContext, mouseX, mouseY);
	}
	
	public void renderForeground(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		for (ClickableWidget widget : selectableWidgets) {
			if (widget instanceof TextFieldWidget) {
				widget.render(drawContext, mouseX, mouseY, delta);
			}
		}
	}
	
	@Override
	protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
		var tr = this.textRenderer;
		drawContext.drawText(tr, this.title, this.titleX, this.titleY, 2236962, false);

		drawContext.drawText(tr, Text.literal("C").formatted(Formatting.AQUA), 7, 54, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.literal("M").formatted(Formatting.LIGHT_PURPLE), 47, 54, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.literal("Y").formatted(Formatting.GOLD), 90, 54, TEXT_COLOR, false);
		/*  this still uses the old color format, since it is easier to read on the background
		drawContext.drawText(tr, Text.literal("C").setStyle(Style.EMPTY.withColor(InkColors.CYAN_COLOR)), 7, 54, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.literal("M").setStyle(Style.EMPTY.withColor(InkColors.MAGENTA_COLOR)), 47, 54, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.literal("Y").setStyle(Style.EMPTY.withColor(InkColors.YELLOW_COLOR)), 90, 54, TEXT_COLOR, false);
		 */
		drawContext.drawText(tr, Text.literal("Glow"), 130, 54, TEXT_COLOR, false);
		
		int offset = 23;
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.particle_count"), 10, 53 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.literal("x"), 66, 64 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.literal("y"), 99, 64 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.literal("z"), 134, 64 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.offset"), 10, 78 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.variance"), 21, 97 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.velocity"), 10, 117 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.variance"), 21, 137 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.scale"), 10, 161 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.variance"), 91, 161 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.duration"), 10, 181 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.variance"), 91, 181 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.gravity"), 10, 201 + offset, TEXT_COLOR, false);
		drawContext.drawText(tr, Text.translatable("block.spectrum.particle_spawner.collisions"), 90, 201 + offset, TEXT_COLOR, false);
	}
	
	@Override
	protected void drawBackground(DrawContext drawContext, float delta, int mouseX, int mouseY) {
		int x = (this.width - this.backgroundWidth) / 2;
		int y = (this.height - this.backgroundHeight) / 2;
		
		// the background
		drawContext.drawTexture(GUI_TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
		
		// disabled coloring text field backgrounds
		if (!selectedParticleSupportsColoring) {
			drawContext.drawTexture(GUI_TEXTURE, x + 15, y + 50, 214, 0, 31, 16);
			drawContext.drawTexture(GUI_TEXTURE, x + 56, y + 50, 214, 0, 31, 16);
			drawContext.drawTexture(GUI_TEXTURE, x + 97, y + 50, 214, 0, 31, 16);
		}
		
		// the checked & collision buttons checkmarks, if enabled
		if (collisionsEnabled) {
			drawContext.drawTexture(GUI_TEXTURE, x + 146, y + 220, 176, 0, 16, 16);
		}
		if (glowing) {
			drawContext.drawTexture(GUI_TEXTURE, x + 153, y + 50, 176, 0, 16, 16);
		}
		
		// particle selection outline
		if (particleSelectionIndex / PARTICLES_PER_PAGE == activeParticlePage) {
			drawContext.drawTexture(GUI_TEXTURE, x + 27 + (20 * (particleSelectionIndex % PARTICLES_PER_PAGE)), y + 19, 192, 0, 22, 22);
		}
		
		RenderSystem.setShaderTexture(0, spriteAtlasTexture.getId());
		int firstDisplayedEntryId = PARTICLES_PER_PAGE * activeParticlePage;
		for (int j = 0; j < PARTICLES_PER_PAGE; j++) {
			int spriteIndex = firstDisplayedEntryId + j;
			if (spriteIndex >= displayedParticleEntries.size()) {
				break;
			}
			Sprite particleSprite = spriteAtlasTexture.getSprite(displayedParticleEntries.get(spriteIndex).textureIdentifier());
			SpriteContents contents = particleSprite.getContents();
			drawContext.drawSprite(x + 38 + j * 20 - contents.getWidth() / 2, y + 31 - contents.getHeight() / 2, 0, contents.getWidth(), contents.getHeight(), particleSprite);
		}
	}
	
	protected void setupInputFields(ParticleSpawnerBlockEntity blockEntity) {
		int startX = (this.width - this.backgroundWidth) / 2 + 3;
		int startY = (this.height - this.backgroundHeight) / 2 + 3;
		
		ParticleSpawnerConfiguration configuration = blockEntity.getConfiguration();
		cyanField = addTextFieldWidget(startX + 16, startY + 51, Text.literal("Cyan"), String.valueOf(configuration.getCmyColor().x()), this::isPositiveDecimalNumber100);
		magentaField = addTextFieldWidget(startX + 57, startY + 51, Text.literal("Magenta"), String.valueOf(configuration.getCmyColor().y()), this::isPositiveDecimalNumber100);
		yellowField = addTextFieldWidget(startX + 97, startY + 51, Text.literal("Yellow"), String.valueOf(configuration.getCmyColor().z()), this::isPositiveDecimalNumber100);
		glowingButton = ButtonWidget.builder(Text.translatable("gui.spectrum.button.glowing"), this::glowingButtonPressed)
				.size(16, 16)
				.position(startX + 153, startY + 50)
				.build();
		addSelectableChild(glowingButton);
		this.glowing = configuration.glows();
		
		int offset = 23;
		amountField = addTextFieldWidget(startX + 110, startY + 50 + offset, Text.literal("Particles per Second"), String.valueOf(configuration.getParticlesPerSecond()), this::isPositiveDecimalNumberUnderThousand);
		positionXField = addTextFieldWidget(startX + 61, startY + 74 + offset, Text.literal("X Position"), String.valueOf(configuration.getSourcePosition().x()), this::isAbsoluteDecimalNumberThousand);
		positionYField = addTextFieldWidget(startX + 96, startY + 74 + offset, Text.literal("Y Position"), String.valueOf(configuration.getSourcePosition().y()), this::isAbsoluteDecimalNumberThousand);
		positionZField = addTextFieldWidget(startX + 131, startY + 74 + offset, Text.literal("Z Position"), String.valueOf(configuration.getSourcePosition().z()), this::isAbsoluteDecimalNumberThousand);
		positionXVarianceField = addTextFieldWidget(startX + 69, startY + 94 + offset, Text.literal("X Position Variance"), String.valueOf(configuration.getSourcePositionVariance().x()), this::isAbsoluteDecimalNumberThousand);
		positionYVarianceField = addTextFieldWidget(startX + 104, startY + 94 + offset, Text.literal("Y Position Variance"), String.valueOf(configuration.getSourcePositionVariance().y()), this::isAbsoluteDecimalNumberThousand);
		positionZVarianceField = addTextFieldWidget(startX + 140, startY + 94 + offset, Text.literal("Z Position Variance"), String.valueOf(configuration.getSourcePositionVariance().z()), this::isAbsoluteDecimalNumberThousand);
		velocityXField = addTextFieldWidget(startX + 61, startY + 114 + offset, Text.literal("X Velocity"), String.valueOf(configuration.getVelocity().x()), this::isAbsoluteDecimalNumberThousand);
		velocityYField = addTextFieldWidget(startX + 96, startY + 114 + offset, Text.literal("Y Velocity"), String.valueOf(configuration.getVelocity().y()), this::isAbsoluteDecimalNumberThousand);
		velocityZField = addTextFieldWidget(startX + 131, startY + 114 + offset, Text.literal("Z Velocity"), String.valueOf(configuration.getVelocity().z()), this::isAbsoluteDecimalNumberThousand);
		velocityXVarianceField = addTextFieldWidget(startX + 69, startY + 134 + offset, Text.literal("X Velocity Variance"), String.valueOf(configuration.getVelocityVariance().x()), this::isAbsoluteDecimalNumberThousand);
		velocityYVarianceField = addTextFieldWidget(startX + 104, startY + 134 + offset, Text.literal("Y Velocity Variance"), String.valueOf(configuration.getVelocityVariance().y()), this::isAbsoluteDecimalNumberThousand);
		velocityZVarianceField = addTextFieldWidget(startX + 140, startY + 134 + offset, Text.literal("Z Velocity Variance"), String.valueOf(configuration.getVelocityVariance().z()), this::isAbsoluteDecimalNumberThousand);
		scale = addTextFieldWidget(startX + 55, startY + 158 + offset, Text.literal("Scale"), String.valueOf(configuration.getScale()), this::isPositiveDecimalNumberUnderTen);
		scaleVariance = addTextFieldWidget(startX + 139, startY + 158 + offset, Text.literal("Scale Variance"), String.valueOf(configuration.getScaleVariance()), this::isPositiveDecimalNumberUnderTen);
		duration = addTextFieldWidget(startX + 55, startY + 178 + offset, Text.literal("Duration"), String.valueOf(configuration.getLifetimeTicks()), this::isPositiveWholeNumberUnderThousand);
		durationVariance = addTextFieldWidget(startX + 139, startY + 178 + offset, Text.literal("Duration Variance"), String.valueOf(configuration.getLifetimeVariance()), this::isPositiveWholeNumberUnderThousand);
		gravity = addTextFieldWidget(startX + 55, startY + 198 + offset, Text.literal("Gravity"), String.valueOf(configuration.getGravity()), this::isBetweenZeroAndOne);
		
		collisionsButton = ButtonWidget.builder(Text.translatable("gui.spectrum.button.collisions"), this::collisionButtonPressed)
				.position(startX + 142, startY + 194 + offset)
				.size(16, 16)
				.build();
		collisionsEnabled = configuration.hasCollisions();
		addSelectableChild(collisionsButton);
		
		selectableWidgets.add(cyanField);
		selectableWidgets.add(magentaField);
		selectableWidgets.add(yellowField);
		selectableWidgets.add(glowingButton);
		selectableWidgets.add(amountField);
		selectableWidgets.add(positionXField);
		selectableWidgets.add(positionYField);
		selectableWidgets.add(positionZField);
		selectableWidgets.add(positionXVarianceField);
		selectableWidgets.add(positionYVarianceField);
		selectableWidgets.add(positionZVarianceField);
		selectableWidgets.add(velocityXField);
		selectableWidgets.add(velocityYField);
		selectableWidgets.add(velocityZField);
		selectableWidgets.add(velocityXVarianceField);
		selectableWidgets.add(velocityYVarianceField);
		selectableWidgets.add(velocityZVarianceField);
		selectableWidgets.add(scale);
		selectableWidgets.add(scaleVariance);
		selectableWidgets.add(duration);
		selectableWidgets.add(durationVariance);
		selectableWidgets.add(gravity);
		selectableWidgets.add(collisionsButton);
		
		backButton = ButtonWidget.builder(Text.translatable("gui.spectrum.button.back"), this::navigationButtonPressed)
				.size(12, 14)
				.position(startX + 11, startY + 19)
				.build();
		addSelectableChild(backButton);
		forwardButton = ButtonWidget.builder(Text.translatable("gui.spectrum.button.forward"), this::navigationButtonPressed)
				.size(12, 14)
				.position(startX + 147, startY + 19)
				.build();
		addSelectableChild(forwardButton);
		
		particleButtons = List.of(
				addParticleButton(startX + 23, startY + 16),
				addParticleButton(startX + 23 + 20, startY + 16),
				addParticleButton(startX + 23 + 40, startY + 16),
				addParticleButton(startX + 23 + 60, startY + 16),
				addParticleButton(startX + 23 + 80, startY + 16),
				addParticleButton(startX + 23 + 100, startY + 16)
		);
		
		this.particleSelectionIndex = 0;
		int particleIndex = 0;
		for (ParticleSpawnerParticlesDataLoader.ParticleSpawnerEntry availableParticle : displayedParticleEntries) {
			if (availableParticle.particleType().equals(configuration.getParticleType())) {
				this.particleSelectionIndex = particleIndex;
				break;
			}
			particleIndex++;
		}
		
		if (displayedParticleEntries.isEmpty()) {
			setColoringEnabled(false);
		}
		
		ParticleSpawnerParticlesDataLoader.ParticleSpawnerEntry entry = displayedParticleEntries.get(this.particleSelectionIndex);
		setColoringEnabled(entry.supportsColoring());
	}
	
	private void navigationButtonPressed(ButtonWidget buttonWidget) {
		int pageCount = displayedParticleEntries.size() / PARTICLES_PER_PAGE;
		if (pageCount == 0) {
			return;
		}
		
		if (buttonWidget == forwardButton) {
			activeParticlePage = (activeParticlePage + 1) % pageCount;
		} else {
			activeParticlePage = (activeParticlePage - 1 + pageCount) % pageCount;
		}
	}
	
	private @NotNull TextFieldWidget addTextFieldWidget(int x, int y, Text text, String defaultText, Predicate<String> textPredicate) {
		TextFieldWidget textFieldWidget = new TextFieldWidget(this.textRenderer, x, y, 31, 16, text);
		
		textFieldWidget.setTextPredicate(textPredicate);
		textFieldWidget.setFocusUnlocked(true);
		textFieldWidget.setEditable(true);
		textFieldWidget.setEditableColor(-1);
		textFieldWidget.setUneditableColor(-1);
		textFieldWidget.setDrawsBackground(false);
		textFieldWidget.setMaxLength(6);
		textFieldWidget.setText(defaultText);
		textFieldWidget.setChangedListener(this::onTextBoxValueChanged);
		addSelectableChild(textFieldWidget);
		
		return textFieldWidget;
	}
	
	private @NotNull ButtonWidget addParticleButton(int x, int y) {
		ButtonWidget button = ButtonWidget.builder(Text.translatable("gui.spectrum.button.particles"), this::particleButtonPressed)
				.size(20, 20)
				.position(x, y)
				.build();
		addSelectableChild(button);
		return button;
	}
	
	private void particleButtonPressed(ButtonWidget buttonWidget) {
		int buttonIndex = particleButtons.indexOf(buttonWidget);
		int newIndex = PARTICLES_PER_PAGE * activeParticlePage + buttonIndex;
		if (newIndex >= displayedParticleEntries.size()) {
			return;
		}

		ParticleSpawnerParticlesDataLoader.ParticleSpawnerEntry entry = displayedParticleEntries.get(newIndex);
		setColoringEnabled(entry.supportsColoring());

		if (newIndex < displayedParticleEntries.size()) {
			particleSelectionIndex = newIndex;
			onValuesChanged();
		}
	}
	
	private void setColoringEnabled(boolean enabled) {
		this.selectedParticleSupportsColoring = enabled;
		
		this.cyanField.setEditable(enabled);
		this.magentaField.setEditable(enabled);
		this.yellowField.setEditable(enabled);
		this.cyanField.setFocusUnlocked(enabled);
		this.magentaField.setFocusUnlocked(enabled);
		this.yellowField.setFocusUnlocked(enabled);
		this.cyanField.setChangedListener(enabled ? this::onTextBoxValueChanged : null);
		this.magentaField.setChangedListener(enabled ? this::onTextBoxValueChanged : null);
		this.yellowField.setChangedListener(enabled ? this::onTextBoxValueChanged : null);
		
		this.setFocused(this.amountField);
	}
	
	private void collisionButtonPressed(ButtonWidget buttonWidget) {
		collisionsEnabled = !collisionsEnabled;
		this.onValuesChanged();
	}
	
	private void glowingButtonPressed(ButtonWidget buttonWidget) {
		glowing = !glowing;
		this.onValuesChanged();
	}
	
	private void onTextBoxValueChanged(@NotNull String newValue) {
		onValuesChanged();
	}
	
	private boolean isPositiveDecimalNumberUnderThousand(String text) {
		try {
			return Double.parseDouble(text) < 1000;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isAbsoluteDecimalNumberThousand(String text) {
		try {
			return Math.abs(Double.parseDouble(text)) < 1000;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isPositiveDecimalNumber100(String text) {
		try {
			int number = Integer.parseInt(text);
			return number >= 0 && number <= 100;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private boolean isPositiveDecimalNumberUnderTen(String text) {
		try {
			return Double.parseDouble(text) < 10;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	protected boolean isPositiveWholeNumberUnderThousand(@NotNull String text) {
		try {
			return Integer.parseInt(text) < 1000;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	protected boolean isBetweenZeroAndOne(@NotNull String text) {
		try {
			float f = Float.parseFloat(text);
			return f >= 0 && f <= 1;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Send these changes to the server to distribute to all clients
	 */
	private void onValuesChanged() {
		try {
			ParticleSpawnerConfiguration configuration = new ParticleSpawnerConfiguration(
					displayedParticleEntries.get(particleSelectionIndex).particleType(),
					selectedParticleSupportsColoring ? new Vector3i(Float.parseFloat(cyanField.getText()), Float.parseFloat(magentaField.getText()), Float.parseFloat(yellowField.getText()), RoundingMode.HALF_DOWN) : new Vector3i(0, 0, 0),
					glowing,
					Float.parseFloat(amountField.getText()),
					new Vector3f(Float.parseFloat(positionXField.getText()), Float.parseFloat(positionYField.getText()), Float.parseFloat(positionZField.getText())),
					new Vector3f(Float.parseFloat(positionXVarianceField.getText()), Float.parseFloat(positionYVarianceField.getText()), Float.parseFloat(positionZVarianceField.getText())),
					new Vector3f(Float.parseFloat(velocityXField.getText()), Float.parseFloat(velocityYField.getText()), Float.parseFloat(velocityZField.getText())),
					new Vector3f(Float.parseFloat(velocityXVarianceField.getText()), Float.parseFloat(velocityYVarianceField.getText()), Float.parseFloat(velocityZVarianceField.getText())),
					Float.parseFloat(scale.getText()),
					Float.parseFloat(scaleVariance.getText()),
					Integer.parseInt(duration.getText()),
					Integer.parseInt(durationVariance.getText()),
					Float.parseFloat(gravity.getText()),
					collisionsEnabled
			);
			
			PacketByteBuf packetByteBuf = PacketByteBufs.create();
			configuration.write(packetByteBuf);
			
			ClientPlayNetworking.send(SpectrumC2SPackets.CHANGE_PARTICLE_SPAWNER_SETTINGS_PACKET_ID, packetByteBuf);
		} catch (Exception e) {
			// the text boxes currently are not able to be parsed yet.
			// wait until everything is set up
		}
	}
	
}
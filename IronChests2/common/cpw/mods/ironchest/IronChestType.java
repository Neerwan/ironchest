package cpw.mods.ironchest;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.mod_IronChest;
import net.minecraft.src.forge.Configuration;

public enum IronChestType {
	IRON(54, "Iron Chest", null, "ironchest.png", 0, Item.ingotIron, TileEntityIronChest.class, "mmmmPmmmm"),
	GOLD(81, "Gold Chest", "guiGoldChest", "goldchest.png", 1, Item.ingotGold, TileEntityGoldChest.class, "mmmmPmmmm"),
	DIAMOND(108,"Diamond Chest","guiDiamondChest", "diamondchest.png", 2, Item.diamond, TileEntityDiamondChest.class, "mmmmPmmmm");
	int size;
	String friendlyName;
	private String modelTexture;
	private String guiName;
	private int textureRow;
	private Class<? extends TileEntityIronChest> clazz;
	private Item mat;
	private String[] recipes;
	private int guiId;

	IronChestType(int size, String friendlyName, String guiName, String modelTexture, int textureRow, Item mat, Class<? extends TileEntityIronChest> clazz, String... recipes) {
		this.size = size;
		this.friendlyName = friendlyName;
		this.guiName=guiName;
		this.modelTexture = "/ic2/sprites/"+modelTexture;
		this.textureRow = textureRow;
		this.clazz = clazz;
		this.mat = mat;
		this.recipes=recipes;
	}

	public String getModelTexture() {
		return modelTexture;
	}

	public int getTextureRow() {
		return textureRow;
	}

	public static TileEntity makeEntity(int metadata) {
		//Compatibility			
		int chesttype=metadata;
		int facing=0;
	
		if (metadata>2) {
			chesttype=metadata<<2;
			facing=metadata&3;
		}
		try {
			TileEntityIronChest te=values()[chesttype].clazz.newInstance();
			if (mod_IronChest.compatibilityMode) {
				te.setFacing((byte)facing);
			}
			return te;
		} catch (InstantiationException e) {
			// unpossible
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// unpossible
			e.printStackTrace();
		}
		return null;
	}

	public static void registerTileEntities(Class<? extends TileEntitySpecialRenderer> renderer) {
		for (IronChestType typ : values()) {
			try {
				if (renderer!=null) {
					ModLoader.RegisterTileEntity(typ.clazz, typ.name(),renderer.newInstance());
				} else {
					ModLoader.RegisterTileEntity(typ.clazz, typ.name());
				}
			} catch (InstantiationException e) {
				// unpossible
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// unpossible
				e.printStackTrace();
			}
		}
	}

	public static void registerTranslations() {
		for (IronChestType typ : values()) {
			ModLoader.AddLocalization(typ.name()+".name", typ.friendlyName);
		}
	}
	public static void registerRecipes(BlockIronChest blockResult) {
		ItemStack previous=new ItemStack(Block.chest);
		for (IronChestType typ : values()) {
			for (String recipe : typ.recipes) {
				String[] recipeSplit=new String[] { recipe.substring(0,3),recipe.substring(3,6), recipe.substring(6,9) };
				addRecipe(new ItemStack(blockResult, 1, typ.ordinal()), recipeSplit, 'm', typ.mat, 'P', previous, 'G', Block.glass, 'C', Block.chest);
			}
			previous=new ItemStack(blockResult,1,typ.ordinal());
		}
	}
	private static void addRecipe(ItemStack is, Object... parts) {
		ModLoader.AddRecipe(is, parts);
	}

	public int getGUI() {
		return guiId;
	}
	
	public static void initGUIs(Configuration cfg) {
		int defGUI=51;
		for (IronChestType typ : values()) {
			if (typ.guiName!=null) {
				typ.guiId=Integer.parseInt(cfg.getOrCreateIntProperty(typ.guiName, Configuration.GENERAL_PROPERTY, defGUI++).value);
			} else {
				typ.guiId=-1;
			}
		}
	}
}
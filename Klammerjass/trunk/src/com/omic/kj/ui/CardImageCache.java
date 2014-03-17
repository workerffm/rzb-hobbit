package com.omic.kj.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.omic.kj.shared.domain.Karte;

public class CardImageCache {

	private final Logger log = Logger.getLogger("UI");
	
	private static CardImageCache instance;
	private final Map<Karte, BufferedImage> cache = new HashMap<>();
	private BufferedImage defaultImage, coverImage;
	private Dimension cardDimension;

	private CardImageCache() {
		try {
			{
				InputStream is = this.getClass().getResourceAsStream("/cover.gif");
				coverImage = ImageIO.read(is);
				is.close();
				defaultImage = coverImage;
				cardDimension = new Dimension(coverImage.getWidth(null), coverImage.getHeight(null));
			}
			{
				InputStream is = this.getClass().getResourceAsStream("/karo7.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Karo7, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/karo6.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Karo8, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/karo5.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Karo9, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/karo3.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.KaroB, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/karo2.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.KaroD, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/karo1.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.KaroK, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/karo4.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Karo10, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/karo0.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.KaroA, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/herz7.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Herz7, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/herz6.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Herz8, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/herz5.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Herz9, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/herz3.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.HerzB, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/herz2.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.HerzD, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/herz1.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Herzk, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/herz4.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Herz10, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/herz0.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.HerzA, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/pik7.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Pik7, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/pik6.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Pik8, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/pik5.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Pik9, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/pik3.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.PikB, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/pik2.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.PikD, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/pik1.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.PikK, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/pik4.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Pik10, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/pik0.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.PikA, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/kreuz7.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Kreuz7, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/kreuz6.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Kreuz8, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/kreuz5.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Kreuz9, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/kreuz3.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.KreuzB, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/kreuz2.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.KreuzD, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/kreuz1.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.KreuzK, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/kreuz4.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.Kreuz10, img);
				is.close();
			} //
			{
				InputStream is = this.getClass().getResourceAsStream("/kreuz0.gif");
				BufferedImage img;
				img = ImageIO.read(is);
				cache.put(Karte.KreuzA, img);
				is.close();
			} //
		} catch (IOException e) {
			log.log(Level.SEVERE, "Can't load card image.", e);
		}
	}

	public static BufferedImage getImage(final Karte k) {
		return getInstance().getImage2(k);
	}

	public static BufferedImage getCoverImage() {
		return getInstance().coverImage;
	}

	public static Dimension getCardDimension() {
		return getInstance().cardDimension;
	}

	private static CardImageCache getInstance() {
		if (CardImageCache.instance == null) {
			CardImageCache.instance = new CardImageCache();
		}
		return instance;
	}

	private BufferedImage getImage2(final Karte k) {
		BufferedImage img = cache.get(k);
		return img == null ? defaultImage : img;
	}

}

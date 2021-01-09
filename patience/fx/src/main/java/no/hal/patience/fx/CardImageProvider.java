package patience.fx;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class CardImageProvider {

	private final Map<String, Image> images = new HashMap<String, Image>();

	private final String suitChars = "SHDC";
	private final String[] suitNames = { "spades", "hearts", "diamonds", "clubs" };
	private final String[] faceNames = { "placeholder", "a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k"};

	private final String imageSuitFaceFormat = "/images/%s-%s-150.png";
	private final String imageSuitePlaceHolderFormat = "/images/%s-placeholder-150.png";
	private final String imagePlaceHolderFormat = "/images/placeholder-150.png";
	private final String faceDownImageName = "/images/back-blue-150-1.png";

	protected Image getImage(final int suitNum, final int face) {
		if (face <= 0) {
			if (suitNum < 0) {
				return getImageForKey(imagePlaceHolderFormat);
			} else {
				return getImageForKey(String.format(imageSuitePlaceHolderFormat, suitNames[suitNum]));
			}
		}
		return getImageForKey(String.format(imageSuitFaceFormat, suitNames[suitNum], faceNames[face]));
	}

	protected Image getImageForKey(final String key) {
		Image image = images.get(key);
		if (image == null) {
			final URL url = CardImageProvider.class.getResource(key);
			image = new Image(url.toString());
			images.put(key, image);
		}
		return image;
	}

	public Image getImage(final String card) {
		Image image = images.get(card);
		if (image == null) {
			int suitNum = -1, face = 0;
			for (int i = 0; i < card.length(); i++) {
				final char c = card.charAt(i);
				if (Character.isLetter(c)) {
					if (suitNum < 0) {
						suitNum = suitChars.indexOf(c);
					}
					if (face < 1 && i > 0) {
						try {
							face = Integer.valueOf(card.substring(0, i));
						} catch (final NumberFormatException e1) {
						}
					}
					if (face < 1 && i + 1 < card.length()) {
						try {
							face = Integer.valueOf(card.substring(i + 1));
						} catch (final NumberFormatException e2) {
						}
					}
				}
			}
			image = getImage(suitNum, face);
			if (image != null) {
				images.put(card, image);
			}
		}
		return image;
	}

	public Image getFaceDownImage() {
		return getImageForKey(faceDownImageName);
	}
}

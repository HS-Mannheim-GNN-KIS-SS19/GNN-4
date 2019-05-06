/************************************************************************
 * \brief: Main method reading in the spiral data and learning the       *
 *         mapping using the Least Mean Squares method within a neural   *
 *         network.
 *																		*
 * (c) copyright by Jörn Fischer											*
 *                                                                       *																		*
 * @autor: Prof.Dr.Jörn Fischer											*
 * @email: j.fischer@hs-mannheim.de										*
 *                                                                       *
 * @file : MainFrame.java                                                *
 *************************************************************************/

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static void main(String[] args) throws IOException {
		new MainFrame(Image.readImagePPM(new File("lena.ppm").toPath())).run();
	}

	public static final int imageWidth = 900;
	public static final int imageHeight = 600;
	public static final int NEURON_AMOUNT = 9;
	public static final int PIXEL_AMOUNT = 9;

	public final Image image;
	public final ImagePanel canvas;

	private float[][] convWeight = new float[NEURON_AMOUNT][PIXEL_AMOUNT];

	public MainFrame(Image image) {
		super("Convolutional Neural Networks");
		this.image = image;

		getContentPane().setSize(imageWidth, imageHeight);
		setSize(imageWidth + 100, imageHeight + 100);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		add(canvas = new ImagePanel(createImage(MainFrame.imageWidth, MainFrame.imageHeight)));
	}

	/**
	 * run method calls my Main and puts the results on the screen
	 */
	public void run() {
		for (int y = 0; y < image.yRes; y++) {
			for (int x = 0; x < image.xRes; x++) {
				int grayscale = Byte.toUnsignedInt(image.pixel(x, y));
				canvas.drawPixel(x, y, new Color(grayscale, grayscale, grayscale));
			}
		}

		// initialize weights
		Random random = new Random();
		for (int t = 0; t < NEURON_AMOUNT; t++) {
			for (int i = 0; i < PIXEL_AMOUNT; i++) {
				convWeight[t][i] = random.nextFloat() / 10f;
			}
		}

		for (int t = 0; t < 10; t++) {
			int stride = 1;

			displayFilters(20 + t * 50);

			for (int y = 3; y < image.yRes - 3; y += stride) {
				for (int x = 3; x < image.xRes - 3; x += stride) {
					adaptWeights(x, y, computeConvolution(x, y));
				}
			}
		}

		repaint();
		setVisible(true);
	}

	private float[][] computeConvolution(int x, int y) {

		float[][] out = new float[3][NEURON_AMOUNT];
		out[0][0] = image.pixelAsFloat(x - 1, y - 1);
		out[0][1] = image.pixelAsFloat(x, y - 1);
		out[0][2] = image.pixelAsFloat(x + 1, y - 1);

		out[0][3] = image.pixelAsFloat(x - 1, y);
		out[0][4] = image.pixelAsFloat(x, y);
		out[0][5] = image.pixelAsFloat(x + 1, y);

		out[0][6] = image.pixelAsFloat(x - 1, y + 1);
		out[0][7] = image.pixelAsFloat(x, y + 1);
		out[0][8] = image.pixelAsFloat(x + 1, y + 1);

		// --- activate first convolutional layer with ReLu output
		
		// ---------------------------------------------------------------------
		// ---------------------------------------------------------------------
		// ----------------------- INSERT CODE HERE ----------------------------
		// ---------------------------------------------------------------------
		// ---------------------------------------------------------------------
		
		return out;
	}

	private void adaptWeights(int x, int y, float[][] out) {
		// --- backwards activation
		for (int pixel = 0; pixel < PIXEL_AMOUNT; pixel++) {
			float sum = 0;
			for (int neuron = 0; neuron < NEURON_AMOUNT; neuron++) {
				sum += out[1][neuron] * convWeight[neuron][pixel];
			}
			out[2][pixel] = sum > 0 ? sum : 0;
		}
		// --- training layer with Constrastive Divergence
		for (int neuron = 0; neuron < NEURON_AMOUNT; neuron++) {
			for (int pixel = 0; pixel < PIXEL_AMOUNT; pixel++) {
				convWeight[neuron][pixel] -= 0.01 * (out[2][pixel] - out[0][pixel]) * out[1][neuron];
			}
		}
	}

	void displayFilters(int xOffset) {
		int offset = image.xRes + xOffset;
		double col;
		for (int i = 0; i < NEURON_AMOUNT; i++) {
			col = (convWeight[i][0] + 0.5) * 255 % 255 < 0 ? 0 : (convWeight[i][0] + 0.5) * 255 % 255;
			canvas.fillRect(offset, 10 + i * 40, 9, 9, new Color((int) col, (int) col, (int) col));
			col = (convWeight[i][1] + 0.5) * 255 % 255 < 0 ? 0 : (convWeight[i][1] + 0.5) * 255 % 255;
			canvas.fillRect(offset + 10, 10 + i * 40, 9, 9, new Color((int) col, (int) col, (int) col));
			col = (convWeight[i][2] + 0.5) * 255 % 255 < 0 ? 0 : (convWeight[i][2] + 0.5) * 255 % 255;
			canvas.fillRect(offset + 20, 10 + i * 40, 9, 9, new Color((int) col, (int) col, (int) col));

			col = (convWeight[i][3] + 0.5) * 255 % 255 < 0 ? 0 : (convWeight[i][3] + 0.5) * 255 % 255;
			canvas.fillRect(offset, 20 + i * 40, 9, 9, new Color((int) col, (int) col, (int) col));
			col = (convWeight[i][4] + 0.5) * 255 % 255 < 0 ? 0 : (convWeight[i][4] + 0.5) * 255 % 255;
			canvas.fillRect(offset + 10, 20 + i * 40, 9, 9, new Color((int) col, (int) col, (int) col));
			col = (convWeight[i][5] + 0.5) * 255 % 255 < 0 ? 0 : (convWeight[i][5] + 0.5) * 255 % 255;
			canvas.fillRect(offset + 20, 20 + i * 40, 9, 9, new Color((int) col, (int) col, (int) col));

			col = (convWeight[i][6] + 0.5) * 255 % 255 < 0 ? 0 : (convWeight[i][6] + 0.5) * 255 % 255;
			canvas.fillRect(offset, 30 + i * 40, 9, 9, new Color((int) col, (int) col, (int) col));
			col = (convWeight[i][7] + 0.5) * 255 % 255 < 0 ? 0 : (convWeight[i][7] + 0.5) * 255 % 255;
			canvas.fillRect(offset + 10, 30 + i * 40, 9, 9, new Color((int) col, (int) col, (int) col));
			col = (convWeight[i][8] + 0.5) * 255 % 255 < 0 ? 0 : (convWeight[i][8] + 0.5) * 255 % 255;
			canvas.fillRect(offset + 20, 30 + i * 40, 9, 9, new Color((int) col, (int) col, (int) col));
		}
	}

}

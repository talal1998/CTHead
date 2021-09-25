/**
 * Name: Muhammad Talal Thaheem
 * Student Number: 956013
 * This is entirely my own work and I am responsible for it.
 */

import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import javafx.application.Application;
import javafx.beans.value.ChangeListener; 
import javafx.beans.value.ObservableValue; 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;

public class CTHeadViewer extends Application {
	short cthead[][][]; //store the 3D volume data set
	short min;
	short max; //min/max value in the 3D volume data set

	int sliderValueTop;//values for scrolling through slices
	int sliderValueFront;
	int sliderValueSide;

	int resizeValueTop = 256;//values for resizing images
	int resizeValueFront = 256;
	int resizeValueSide = 256;

	@Override
	public void start(Stage stage) throws FileNotFoundException, IOException {
		stage.setTitle("CThead Viewer");

		ReadData();

		int width = 256;
		int height = 256;

		WritableImage medicalImageTop = new WritableImage(width, height);//creates front, side and top image views
		ImageView imageViewTop = new ImageView(medicalImageTop); 

		WritableImage medicalImageFront = new WritableImage(width, height);
		ImageView imageViewFront = new ImageView(medicalImageFront); 

		WritableImage medicalImageSide = new WritableImage(width, height);
		ImageView imageViewSide = new ImageView(medicalImageSide); 

		WritableImage histogramImageTop = new WritableImage(width, height);//histogram image views
		ImageView histogramImageViewTop = new ImageView(histogramImageTop); 

		WritableImage histogramImageFront = new WritableImage(width, height);
		ImageView histogramImageViewFront = new ImageView(histogramImageFront);

		WritableImage histogramImageSide = new WritableImage(width, height);
		ImageView histogramImageViewSide = new ImageView(histogramImageSide);



		Button mip_button=new Button("MIP");//button to switch to MIP mode
		//sliders to step through the slices 
		Slider topSlider = new Slider(0, 112, 0);
		Slider frontSlider = new Slider(0, 255, 0);
		Slider sideSlider = new Slider(0, 255, 0);

		//sliders to define min, max and default values for resizing
		Slider resizeSliderTop = new Slider (128, 300, 256);
		Slider resizeSliderFront = new Slider (128, 300, 256);
		Slider resizeSliderSide = new Slider (128, 300, 256);
		Slider resizeSliderHistogramTop = new Slider(128, 300, 256);
		Slider resizeSliderHistogramFront = new Slider(128, 300, 256);
		Slider resizeSliderHistogramSide = new Slider(128, 300, 256);

		//sliders for histogram equalisation
		Slider histogramSliderTop = new Slider(0, 112, 0);
		Slider histogramSliderFront = new Slider(0, 255, 0);
		Slider histogramSliderSide = new Slider(0, 255, 0);

		//labels for each slider
		Label topViewLabel = new Label("Top View");
		Label frontViewLabel = new Label("Front View");
		Label sideViewLabel = new Label("Side View");
		Label topResizeLabel = new Label("Resize Top View");
		Label frontResizeLabel = new Label("Resize Front View");
		Label sideResizeLabel = new Label("Resize Side View");
		Label topHistogramLabel = new Label("Histogram Top View");
		Label frontHistogramLabel = new Label("Histogram Front View");
		Label sideHistogramLabel = new Label("Histogram Side View");
		Label resizeTopHistogramLabel = new Label("Resize Histogram Top View");
		Label resizeFrontHistogramLabel = new Label("Resize Histogram Front View");
		Label resizeSideHistogramLabel = new Label("Resize Histogram Side View");

		//listeners for various sliders and mip button
		//performs mip on a normal & resized image
		mip_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				imageViewTop.setImage(resizeImage(mipTop(medicalImageTop), resizeValueTop));
				imageViewFront.setImage(resizeImage(mipFront(medicalImageFront), resizeValueFront));
				imageViewSide.setImage(resizeImage(mipSide(medicalImageSide), resizeValueSide));

			}
		});

		topSlider.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						sliderValueTop = newValue.intValue();
						System.out.println(sliderValueTop);
						imageViewTop.setImage(resizeImage(topView(medicalImageTop), resizeValueTop));//allows slices to be scrolled on resized image
					} 

				}); 

		//allows histogram equalisation on a normal & resized image
		histogramSliderTop.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						sliderValueTop = newValue.intValue();
						System.out.println(sliderValueTop);
						histogramImageViewTop.setImage(resizeImage(histogramEqualisationTop(histogramImageTop), resizeValueTop));
					} 

				}); 


		frontSlider.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						sliderValueFront = newValue.intValue();
						System.out.println(sliderValueFront);
						imageViewFront.setImage(resizeImage(frontView(medicalImageFront), resizeValueFront));
					} 
				}); 

		histogramSliderFront.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						sliderValueFront = newValue.intValue();
						System.out.println(sliderValueFront);
						histogramImageViewFront.setImage(resizeImage(histogramEqualisationFront(histogramImageFront), resizeValueFront));
					} 

				}); 


		sideSlider.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						sliderValueSide = newValue.intValue();
						System.out.println(sliderValueSide);
						imageViewSide.setImage(resizeImage(sideView(medicalImageSide), resizeValueSide));
					} 
				}); 


		histogramSliderSide.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						sliderValueSide = newValue.intValue();
						System.out.println(sliderValueSide);
						histogramImageViewSide.setImage(resizeImage(histogramEqualisationSide(histogramImageSide), resizeValueSide));
					} 

				}); 

		//resizes the imageViews
		resizeSliderTop.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						resizeValueTop = newValue.intValue();
						System.out.println(resizeValueTop);
						imageViewTop.setImage(resizeImage(topView(medicalImageTop), resizeValueTop));
					} 
				}); 

		resizeSliderFront.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						resizeValueFront = newValue.intValue();
						System.out.println(resizeValueFront);
						imageViewFront.setImage(resizeImage(frontView(medicalImageFront), resizeValueFront));
					} 
				}); 

		resizeSliderSide.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						resizeValueSide = newValue.intValue();
						System.out.println(resizeValueSide);
						imageViewSide.setImage(resizeImage(sideView(medicalImageSide), resizeValueSide));
					} 
				}); 

		//resizes the histogram imageViews
		resizeSliderHistogramTop.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						resizeValueTop = newValue.intValue();
						System.out.println(resizeValueTop);
						histogramImageViewTop.setImage(resizeImage(histogramEqualisationTop(histogramImageTop), resizeValueTop));
					} 
				}); 

		resizeSliderHistogramFront.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						resizeValueFront = newValue.intValue();
						System.out.println(resizeValueFront);
						histogramImageViewFront.setImage(resizeImage(histogramEqualisationFront(histogramImageFront), resizeValueFront));
					} 
				}); 

		resizeSliderHistogramSide.valueProperty().addListener( 
				new ChangeListener<Number>() { 
					public void changed(ObservableValue <? extends Number >  
					observable, Number oldValue, Number newValue) 
					{ 
						resizeValueSide = newValue.intValue();
						System.out.println(resizeValueSide);
						histogramImageViewSide.setImage(resizeImage(histogramEqualisationSide(histogramImageSide), resizeValueSide));
					} 
				}); 

		FlowPane root = new FlowPane();
		root.setOrientation(Orientation.VERTICAL);
		root.setVgap(8);
		root.setHgap(4);

		//various sliders and imageViews added to window
		root.getChildren().addAll(imageViewTop, imageViewFront, imageViewSide);
		root.getChildren().addAll(histogramImageViewTop, histogramImageViewFront, histogramImageViewSide);
		root.getChildren().addAll(mip_button, topViewLabel, topSlider, frontViewLabel, frontSlider, sideViewLabel, sideSlider);
		root.getChildren().addAll(topResizeLabel, resizeSliderTop, frontResizeLabel, resizeSliderFront, sideResizeLabel, resizeSliderSide); 
		root.getChildren().addAll(topHistogramLabel, histogramSliderTop, frontHistogramLabel, histogramSliderFront, sideHistogramLabel, histogramSliderSide);
		root.getChildren().addAll(resizeTopHistogramLabel, resizeSliderHistogramTop, resizeFrontHistogramLabel, resizeSliderHistogramFront, resizeSideHistogramLabel, resizeSliderHistogramSide);

		Scene scene = new Scene(root, 1366, 800);
		stage.setScene(scene);
		stage.show();
	}


	//Function to read in the cthead data set
	public void ReadData() throws IOException {
		File file = new File("CThead");
		//Read the data quickly via a buffer 
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

		int i, j, k; //loop through the 3D data set

		min = Short.MAX_VALUE; 
		max = Short.MIN_VALUE; //set to extreme values
		short read; //value read in
		int b1;
		int b2; //data is wrong Endian (check wikipedia) for Java so we need to swap the bytes around

		cthead = new short[113][256][256]; //allocate the memory - note this is fixed for this data set
		//loop through the data reading it in
		for (k = 0; k < 113; k++) {
			for (j = 0; j < 256; j++) {
				for (i = 0; i < 256; i++) {
					//because the Endianess is wrong, it needs to be read byte at a time and swapped
					b1 = ((int) in.readByte()) & 0xff; //the 0xff is because Java does not have unsigned types
					b2 = ((int) in.readByte()) & 0xff; //the 0xff is because Java does not have unsigned types
					read = (short) ((b2 << 8) | b1); //and swizzle the bytes around
					if (read < min) min = read; //update the minimum
					if (read > max) max = read; //update the maximum
					cthead[k][j][i] = read; //put the short into memory (in C++ you can replace all this code with one fread)
				}
			}
		}
		System.out.println(min + " " + max); //diagnostic - for CThead this should be -1117, 2248
		//(i.e. there are 3366 levels of grey (we are trying to display on 256 levels of grey)
		//therefore histogram equalization would be a good thing
	}



	/**
	 * Function that performs histogram equalisation on the top view.
	 **/
	public WritableImage histogramEqualisationTop(WritableImage image) {
		int min = -1117;
		int max = 2248;
		int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, k, index;
		int[] histogram = new int[3366];
		float t_i = 0;
		float size = (256 * 256 * 113);
		float[] mapping = new float[3366];
		float col;
		short datum;

		PixelWriter imageWriter = image.getPixelWriter();

		for (j = 0; j < h; j++) {
			for (i = 0; i < w; i++) {
				for (k = 0; k < 113; k++) {
					index = cthead[k][j][i] - min;
					histogram[index]++;
				}
			}
		}

		for (index = 0; index < (max - min + 1); index++) {
			t_i = t_i + histogram[index];
			mapping[index] = (t_i / size);
		}

		for (j = 0; j < h; j++) {
			for(i = 0; i < w; i++) {
				datum = cthead[sliderValueTop][j][i];
				col = mapping[datum - min];
				imageWriter.setColor(i, j, Color.color(col,col,col, 1.0));
			}
		}
		return image;
	}

	public WritableImage histogramEqualisationFront(WritableImage image) {
		int min = -1117;
		int max = 2248;
		int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, k, index;
		int[] histogram = new int[3366];
		float t_i = 0;
		float size = (256 * 256 * 113);
		float[] mapping = new float[3366];
		float col;
		short datum;

		PixelWriter imageWriter = image.getPixelWriter();


		for (j = 0; j < h; j++) {
			for (i = 0; i < w; i++) {
				for (k = 0; k < 113; k++) {
					index = cthead[k][j][i] - min;
					histogram[index]++;
				}
			}
		}

		for (index = 0; index < (max - min + 1); index++) {
			t_i = t_i + histogram[index];
			mapping[index] = (t_i / size);
		}

		for (j = 0; j < 113; j++) {
			for(i = 0; i < w; i++) {
				datum = cthead[j][sliderValueFront][i];
				col = mapping[datum - min];
				imageWriter.setColor(i, j, Color.color(col,col,col, 1.0));
			}
		}
		return image;
	}

	public WritableImage histogramEqualisationSide(WritableImage image) {
		int min = -1117;
		int max = 2248;
		int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, k, index;
		int[] histogram = new int[3366];
		float t_i = 0;
		float size = (256 * 256 * 113);
		float[] mapping = new float[3366];
		float col;
		short datum;

		PixelWriter imageWriter = image.getPixelWriter();

		for (j = 0; j < h; j++) {
			for (i = 0; i < w; i++) {
				for (k = 0; k < 113; k++) {
					index = cthead[k][j][i] - min;
					histogram[index]++;
				}
			}
		}

		for (index = 0; index < (max - min + 1); index++) {
			t_i = t_i + histogram[index];
			mapping[index] = (t_i / size);
		}

		for (j = 0; j < 113; j++) {
			for(i = 0; i < w; i++) {
				datum = cthead[j][i][sliderValueSide];
				col = mapping[datum - min];
				imageWriter.setColor(i, j, Color.color(col,col,col, 1.0));
			}
		}
		return image;
	}

	/** 
	 * Function that resizes the image. Obtains new width and height of the image
	 * then calculates the colour for the new image using nearest neighbour sampling.
	 **/
	public WritableImage resizeImage(WritableImage image, int resizeValue) {
		int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j;

		Color newColour;

		float y;
		float x;

		int yB = resizeValue;
		int xB = resizeValue;

		PixelReader imageReader = image.getPixelReader();
		WritableImage imageResized = new WritableImage(xB, yB);
		PixelWriter imageWriter = imageResized.getPixelWriter();

		for(j = 0; j < yB; j++){
			for(i = 0; i < xB; i++){
				y =  (j * ((float) h / (float) yB));
				x =  (i * ((float) w / (float) xB));
				newColour = imageReader.getColor((int) Math.floor(x), (int) Math.floor(y));
				imageWriter.setColor(i, j, newColour);
			}
		}
		return imageResized;
	}

	/**
    This function shows how to carry out an operation on an image.
    It obtains the dimensions of the image, and then loops through
    the image carrying out the copying of a slice of data into the
	image. 
	 **/
	public WritableImage topView(WritableImage image) {
		//Get image dimensions, and declare loop variables
		int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j;
		PixelWriter imageWriter = image.getPixelWriter();

		float col;
		short datum;

		for (j = 0; j < h; j++) {
			for (i = 0; i < w; i++) {
				datum = cthead[sliderValueTop][j][i];
				col = (((float) datum - (float) min) / ((float) (max - min)));
				imageWriter.setColor(i, j, Color.color(col,col,col, 1.0));
			} 
		}
		return image;	
	}


	/**
    This function shows how to carry out an operation on an image.
    It obtains the dimensions of the image, and then loops through
    the image carrying out the copying of a slice of data into the
	image. 
	 **/
	public WritableImage frontView(WritableImage image) {
		//Get image dimensions, and declare loop variables
		int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j;
		PixelWriter imageWriter = image.getPixelWriter();

		float col;
		short datum;

		for (j = 0; j < 113; j++) {
			for (i = 0; i < w; i++) {
				datum = cthead[j][sliderValueFront][i];
				col = (((float) datum - (float) min) / ((float) (max - min)));
				imageWriter.setColor(i, j, Color.color(col,col,col, 1.0));
			} 
		} 
		return image;
	}

	/**
    This function shows how to carry out an operation on an image.
    It obtains the dimensions of the image, and then loops through
    the image carrying out the copying of a slice of data into the
	image. 
	 **/
	public WritableImage sideView(WritableImage image) {
		//Get image dimensions, and declare loop variables
		int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j;
		PixelWriter imageWriter = image.getPixelWriter();

		float col;
		short datum;

		for (j = 0; j < 113; j++) {
			for (i = 0; i < w; i++) {
				datum = cthead[j][i][sliderValueSide];
				col = (((float) datum - (float) min) / ((float) (max - min)));
				imageWriter.setColor(i, j, Color.color(col,col,col, 1.0));
			} 
		} 
		return image;
	}

	/**
    This function shows how to carry out an maximum intensity projection on an image.
    It finds the maximum for each ray traced through the dataset.
	 **/
	public WritableImage mipTop(WritableImage image) {
		int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, k;
		PixelWriter imageWriter = image.getPixelWriter();

		float col;
		short datum;

		for (j = 0; j < h; j++) {
			for (i = 0; i < w; i++) {
				short maximum = Short.MIN_VALUE;
				for (k = 0; k < 113; k++) {
					datum = cthead[k][j][i];
					if (datum > maximum) {
						maximum = datum;
					}
				} col = (((float) maximum - (float) min) / ((float) (max - min)));
				imageWriter.setColor(i, j, Color.color(col,col,col, 1.0));
			} 
		}
		return image;
	}	

	public WritableImage mipFront(WritableImage image) {
		int w = (int) image.getWidth(), h = (int) image.getHeight(), i, j, k;
		PixelWriter imageWriter = image.getPixelWriter();

		float col;
		short datum;

		for (j = 0; j < 113; j++) {
			for (i = 0; i < w; i++) {
				short maximum = Short.MIN_VALUE;
				for (k = 0; k < 256; k++) {
					datum = cthead[j][k][i];
					if (datum > maximum) {
						maximum = datum;  
					}
				}
				col = (((float) maximum - (float) min) / ((float) (max - min)));
				imageWriter.setColor(i, j, Color.color(col,col,col, 1.0));
			} 
		}
		return image;
	}

	public WritableImage mipSide(WritableImage image) {
		int w = (int) image.getWidth(), h =(int) image.getHeight(), i, j, k;
		PixelWriter imageWriter = image.getPixelWriter();

		float col;
		short datum;

		for (j = 0; j < 113; j++) {
			for (i = 0; i < w; i++) {
				short maximum = Short.MIN_VALUE;
				for (k = 0; k < 256; k++) {
					datum = cthead[j][i][k];
					if (datum > maximum) {
						maximum = datum;  
					}
				}
				col = (((float) maximum - (float) min)/((float) (max - min)));
				imageWriter.setColor(i, j, Color.color(col,col,col, 1.0));
			} 
		} 
		return image;
	}

	public static void main(String[] args) {
		launch();
	}

}
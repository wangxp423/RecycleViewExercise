package com.commonlib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore.Images;

public class BitmapUtil {

    /**
     * 判断文件是否为图片jpg/gif/bmp/png/jpeg
     * @param filename
     * @return
     */
    public static boolean isImageFile(String filename) {
        String ext = null;
        if (filename != null && filename.trim().length() > 0) {
        	int dot = filename.lastIndexOf(".");
        	
        	if (dot < 0) return false;
            ext = filename.substring(dot + 1).toLowerCase();
            if (!"gif".equals(ext)
            		&& !"jpg".equals(ext)
            		&& !"jpeg".equals(ext)
            		&& !"bmp".equals(ext)
            		&& !"png".equals(ext)){
                return false;
            } else if (checkImageFileFormat(filename) != IMAGE_FORMAT_NONE) {
                return true;
            }
        }
        return false;
    }

    private static final byte IMAGE_FORMAT_NONE	= 0;	//	不是正常的图像文件格式
    private static final byte IMAGE_FORMAT_BMP	= 1;
    private static final byte IMAGE_FORMAT_JPG	= 2;
    private static final byte IMAGE_FORMAT_PNG	= 3;
    private static final byte IMAGE_FORMAT_GIF	= 4;
    public static int checkImageFileFormat(String filename) {
    	FileInputStream fis = null;
        try {
            if (filename != null && filename.trim().length() > 0) {
            	fis = new FileInputStream(new File(filename));
            	
            	byte[] data = new byte[4];
            	
            	int read = fis.read(data);
            	
            	if (read != 4) return IMAGE_FORMAT_NONE;
            	
    			if (data[0] == (byte) 0x47 && data[1] == (byte) 0x49 && data[2] == (byte) 0x46) // GIF
    				return IMAGE_FORMAT_GIF;
    			else if (data[0] == (byte) 0x42 && data[1] == (byte) 0x4d) // BMP
    				return IMAGE_FORMAT_BMP;
    			else if (data[0] == (byte) 0xff && data[1] == (byte) 0xd8) // JPG
    				return IMAGE_FORMAT_JPG;
    			else if (data[0] == (byte) 0x89 && data[1] == (byte) 0x50
    					&& data[2] == (byte) 0x4e && data[3] == (byte) 0x47) // PNG
    				return IMAGE_FORMAT_PNG;
            }
        } catch (Exception e) {
        } finally {
        	try {
            	if (fis != null) {
            		fis.close();
            	}
        	} catch (Exception e) {}
        }
        return IMAGE_FORMAT_NONE;
    }

	public static Bitmap decodeBitmap (String imagePath, BitmapFactory.Options options) {
		//读取图像文件 
	    if (options == null) {
	        options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(imagePath, options);
	        int width = options.outWidth;
            int height = options.outHeight;
            int inSampleSize = 1;
            while (width > 720 || height > 1500) {
                inSampleSize *= 2;
                width /= 2;
                height /= 2;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
	    }
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;

        try {
        	Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
//        	Bitmap bitmap = BitmapFactory.decodeFileDescriptor(new FileInputStream(imagePath).getFD(), null, options);

            try {
                ExifInterface exif = new ExifInterface(imagePath);
                final int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

                final int rotation;
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:rotation = 90;break;
                case ExifInterface.ORIENTATION_ROTATE_180:rotation = 180;break;
                case ExifInterface.ORIENTATION_ROTATE_270:rotation = 270;break;
                default:rotation = 0;break;
                }
//                System.out.println("orientation = " + orientation + "; rotation = " + rotation);
                if (rotation != 0) {
                    Bitmap temp = bitmap;
                    bitmap = createCaptureBitmap(temp, rotation, false);
                    temp.recycle();
                    temp = null;
                }
            } catch (Exception e) {e.printStackTrace();}
        	return bitmap;
        } catch (Exception e) {
        	System.gc();
        } catch (OutOfMemoryError error) {
        	System.gc();
        	error.printStackTrace();
        }
    	
    	return null;
    }
    public static Bitmap decodeBitmap (String imagePath) {
        return decodeBitmap(imagePath, null);
    }

	public static Bitmap decodeBitmap (InputStream is, BitmapFactory.Options options) {
		//读取图像文件 
        if (options == null) options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        try {
        	Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        	return bitmap;
        } catch (OutOfMemoryError error) {
        	System.gc();
        	error.printStackTrace();
        }
    	
    	return null;
    }
    public static Bitmap decodeBitmap (InputStream is) {
        return decodeBitmap(is, null);
    }
	
	public static Bitmap decodeBitmap (Context context, int resid) {
		return decodeBitmap(context, resid, 0, 0);
	}
	
	public static Bitmap decodeBitmap (Context context, int resid, int width, int height) {
        InputStream is = null;

        Bitmap bitmap = null;
        try {
            is = context.getResources().openRawResource(resid);
        	BitmapFactory.Options options = new BitmapFactory.Options();
        	options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);
			final int w = options.outWidth;
			final int h = options.outHeight;
			int inSampleSize = 1;
			if (width > 0 && height > 0) {
				inSampleSize = Math.min(w / width, h / height);
			}
			if (inSampleSize <= 0) inSampleSize = 1;
			options.inJustDecodeBounds = false;
			options.inSampleSize = inSampleSize;
            bitmap = BitmapFactory.decodeStream(is, null, options);
//            System.out.println(bitmap.getWidth() + "x" + bitmap.getHeight());
        } catch (Exception e) {
        	e.printStackTrace();
        } catch (OutOfMemoryError error) {
        	System.gc();
        	error.printStackTrace();
        } finally { 
            try { 
                if (is != null) is.close(); 
            } catch(IOException e) { 
            } 
        }
        return bitmap;
	}

	/**
	 * 根据URI得到真实的图片路径
	 * 
	 * @param uri
	 *            图片URI
	 * @return SD卡中图片的路径
	 */
	public static String getFilePathFromUri(Activity activity, Uri uri) {
		String filepath = null;
		try {
			String as[] = new String[1];
			as[0] = "_data";
			Uri uri1 = uri;

			Cursor cursor = activity.managedQuery(uri1, as, null, null, null);
			if (cursor != null) {
				int i = cursor.getColumnIndexOrThrow("_data");	//	_data字段存储文件路径
				cursor.moveToFirst();
				filepath = cursor.getString(i);
			} else {
				String uriString = uri.toString();
				String pStr = "file://";
				int pos = uriString.lastIndexOf(pStr);
				filepath = uriString.substring(pStr.length() + pos,
						uriString.length());
			}

			if (filepath != null) {
				File file = new File(filepath);
				if (file.exists()) {
					return filepath;
				} else {
					return Uri.decode(filepath);
				}
			}

		} catch (Exception e) {
			return null;
		}
		return filepath;
	}

	/**
	 * 生成拍照的图片，图片旋转，创建一个旋转后的图片位图
	 * @param source Bitmap源
	 * @param rotation 旋转的角度
	 * @param facingFront 是否前置摄像头
	 * @return
	 */
	public static Bitmap createCaptureBitmap(Bitmap source, int rotation, boolean facingFront) {
		try {
			Matrix localMatrix = new Matrix();
			localMatrix.setRotate(rotation);
			int width = source.getWidth();
			int height = source.getHeight();
			RectF dstR = new RectF(0, 0, width, height);
			if (rotation == 90 || rotation == 270) {
				int temp = width;
				width = height;
				height = temp;
			}
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			RectF deviceR = new RectF();
			if (facingFront) {
				if (rotation == 90 || rotation == 270) {
					localMatrix.postScale(-1.0f, 1.0f);
				} else if (rotation == 0 || rotation == 180) {
					localMatrix.postScale(1.0f, -1.0f);
				}
			}
			localMatrix.mapRect(deviceR, dstR);
			localMatrix.postTranslate(-deviceR.left, -deviceR.top);
			canvas.drawBitmap(source, localMatrix, null);
			return bitmap;
		} catch (OutOfMemoryError ex) {
			return null;
		}
	}
	
	public static boolean createUploadJpgFile (String originFileFullpath, String uploadFileFullPath, int maxSideLength) {
        boolean success = false;
        OutputStream outputStream = null;
        File file = new File(uploadFileFullPath);
        try {
            File dir = file.getParentFile();
            if (!dir.exists()) dir.mkdirs();
            if (file.exists()) return true;
            
            Bitmap bitmap = decodeBitmap(originFileFullpath);
            if (bitmap == null) return false;
            
            success = saveJpgImage(uploadFileFullPath, bitmap, null);
        } catch (Exception ex) {
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (Throwable t) {
            }
        }
	    return success;
	}
	
	public static boolean saveJpgImage (String fullpathToSave, Bitmap bitmap, byte[] jpegData) {
	    boolean success = false;
        OutputStream outputStream = null;
        File file = new File(fullpathToSave);
        try {
            File dir = file.getParentFile();
            if (!dir.exists()) dir.mkdirs();
            outputStream = new FileOutputStream(file);
            if (bitmap != null) {
                success = bitmap.compress(CompressFormat.JPEG, 65, outputStream);
            } else {
                outputStream.write(jpegData);
                success = true;
            }
        } catch (Exception ex) {
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (Throwable t) {
            }
        }
        return success;
	}

	/**
	 * 保存图片 返回新图片的URI
	 * 
	 * @param cr ContentResolver对象
	 * @param title Images.Media.TITLE
	 * @param filepath 保存路径
	 * @param bimap Bitmap对象
	 * @param jpegData 图片二进制
	 * @return 新图片的Uri
	 */
	public static Uri saveImage(ContentResolver cr, String title,
			String filepath, Bitmap bitmap, byte[] jpegData) {
		OutputStream outputStream = null;
		File file = new File(filepath);
		String filename = file.getName();
		try {
			File dir = file.getParentFile();
			if (!dir.exists()) dir.mkdirs();
			outputStream = new FileOutputStream(file);
			if (bitmap != null) {
				bitmap.compress(CompressFormat.JPEG, 65, outputStream);
			} else {
				outputStream.write(jpegData);
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (Throwable t) {
			}
		}

		ContentValues values = new ContentValues(7);
		values.put(Images.Media.TITLE, title);
		values.put(Images.Media.DISPLAY_NAME, filename);
		values.put(Images.Media.DESCRIPTION, "capture by jiaoyou");
		values.put(Images.Media.MIME_TYPE, "image/jpeg");
		values.put(Images.Media.DATA, filepath);
		values.put(Images.Media.SIZE, file.length());

		return cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
	}

	/**
	 * 从文件Uri获取该文件的容量大小
	 * 
	 * @param context
	 *            设备上下文
	 * @param fileUri
	 *            文件Uri
	 * @return 对应Uri的文件大小，如果文件不存在，size为-1
	 */
	public static long getFileSize(Context context, Uri fileUri) {
		long fileSizeinByte = -1;
		String mediaFilePath = null;
		
		String scheme = fileUri.getScheme();
		
		if ("content".equals(scheme)) {
			mediaFilePath = getFilePathFromUri((Activity) context, fileUri);
		} else if ("file".equals(scheme)) {
			mediaFilePath = fileUri.getEncodedPath();
		}
		try {
			if (mediaFilePath != null) {
				File mediaFile = new File(mediaFilePath);
				fileSizeinByte = mediaFile.length();
			}
		} catch (Exception e) {
		}

		return fileSizeinByte;
	}

	/**
	 * 压缩图片, 但不裁剪图片为正方形
	 * 
	 * @param imageFilePath
	 *            图片的本地地址
	 * @param minSideLength
	 *            小边长，单位为像素，如果为-1，则不按照边来压缩图片
	 * @param maxNumOfPixels
	 *            接受的图片最大像素点个数
	 * @return 如果出错，则为null
	 */
	public static Bitmap makeSmallerBitmap(String imageFilePath,
			int minSideLength, int maxNumOfPixels) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imageFilePath, options);
			if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
				return null;
			}
			options.inSampleSize = computeSampleSize(options, minSideLength, maxNumOfPixels);
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			return BitmapFactory.decodeFile(imageFilePath, options);
		} catch (OutOfMemoryError ex) {
			System.gc();
			return null;
		}
	}

	/**
	 * 图片压缩比例计算
	 * 
	 * @param options  BitmapFactory.Options
	 * @param minSideLength 小边长，单位为像素，如果为-1，则不按照边来压缩图片
	 * @param maxNumOfPixels 这张片图片最大像素值，单位为byte，如100*1024
	 * @return 压缩比例 
	 */
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	/**
	 * 计算图片的压缩比例，用于图片压缩
	 * 
	 * @param options BitmapFactory.Options
	 * @param minSideLength 小边长，单位为像素，如果为-1，则不按照边来压缩图片
	 * @param maxNumOfPixels 这张片图片最大像素值，单位为byte，如100*1024
	 * @return 压缩比例
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}

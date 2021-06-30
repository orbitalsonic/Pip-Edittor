package com.apna.pip.camera.photo.editor.collage.maker.helper.filters;

import android.graphics.Bitmap;

import net.alhazmy13.imagefilter.ImageFilter;

public class PipFilters {

    public Bitmap applyFilter(final int whichFilter, Bitmap bitmap) {
        switch (whichFilter) {
            case 0:
                return null;
            case 1:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.HDR);
            case 2:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.LOMO);
            case 3:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SOFT_GLOW);
            case 4:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.SKETCH);
            case 5:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.GRAY);
            case 6:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.BLOCK);
            case 7:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.RELIEF);
            case 8:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.TV);
            case 9:
                return ImageFilter.applyFilter(bitmap, ImageFilter.Filter.OLD);
            default:
                return null;
        }
    }
}

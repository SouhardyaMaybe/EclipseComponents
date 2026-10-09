package android.content.res;

import android.graphics.*;

public class CompatibilityInfo
{
    /**
     * Application's scale.
     */
    public final float applicationScale = 1.0f;
    /**
     * Application's inverted scale.
     */
    public final float applicationInvertedScale = 1.0f;
	
    /**
     * A helper object to translate the screen and window coordinates back and forth.
     * @hide
     */
    public class Translator {
        final public float applicationScale;
        final public float applicationInvertedScale;

        private Rect mContentInsetsBuffer = null;
        private Rect mVisibleInsetsBuffer = null;
        private Region mTouchableAreaBuffer = null;

        Translator(float applicationScale, float applicationInvertedScale) {
            this.applicationScale = applicationScale;
            this.applicationInvertedScale = applicationInvertedScale;
        }
        Translator() {
            this(CompatibilityInfo.this.applicationScale,
				 CompatibilityInfo.this.applicationInvertedScale);
        }
        /**
         * Translate the screen rect to the application frame.
         */
        public void translateRectInScreenToAppWinFrame(Rect rect) {
            rect.scale(applicationInvertedScale);
        }
        /**
         * Translate the region in window to screen. 
         */
        public void translateRegionInWindowToScreen(Region transparentRegion) {
            transparentRegion.scale(applicationScale);
        }
        /**
         * Apply translation to the canvas that is necessary to draw the content.
         */
        public void translateCanvas(Canvas canvas) {
            if (applicationScale == 1.5f) {
                /*  When we scale for compatibility, we can put our stretched
				 bitmaps and ninepatches on exacty 1/2 pixel boundaries,
				 which can give us inconsistent drawing due to imperfect
				 float precision in the graphics engine's inverse matrix.

				 As a work-around, we translate by a tiny amount to avoid
				 landing on exact pixel centers and boundaries, giving us
				 the slop we need to draw consistently.

				 This constant is meant to resolve to 1/255 after it is
				 scaled by 1.5 (applicationScale). Note, this is just a guess
				 as to what is small enough not to create its own artifacts,
				 and big enough to avoid the precision problems. Feel free
				 to experiment with smaller values as you choose.
                 */
                final float tinyOffset = 2.0f / (3 * 255);
                canvas.translate(tinyOffset, tinyOffset);
            }
            canvas.scale(applicationScale, applicationScale);
        }
		
        /**
         * Translate a Rect in application's window to screen.
         */
        public void translateRectInAppWindowToScreen(Rect rect) {
            rect.scale(applicationScale);
        }

        /**
         * Translate a Rect in screen coordinates into the app window's coordinates.
         */
        public void translateRectInScreenToAppWindow(Rect rect) {
            rect.scale(applicationInvertedScale);
        }
        /**
         * Translate a Point in screen coordinates into the app window's coordinates.
         */
        public void translatePointInScreenToAppWindow(PointF point) {
            final float scale = applicationInvertedScale;
            if (scale != 1.0f) {
                point.x *= scale;
                point.y *= scale;
            }
        }
        /**
         * Translate the content insets in application window to Screen. This uses
         * the internal buffer for content insets to avoid extra object allocation.
         */
        public Rect getTranslatedContentInsets(Rect contentInsets) {
            if (mContentInsetsBuffer == null) mContentInsetsBuffer = new Rect();
            mContentInsetsBuffer.set(contentInsets);
            translateRectInAppWindowToScreen(mContentInsetsBuffer);
            return mContentInsetsBuffer;
        }
        /**
         * Translate the visible insets in application window to Screen. This uses
         * the internal buffer for visible insets to avoid extra object allocation.
         */
        public Rect getTranslatedVisibleInsets(Rect visibleInsets) {
            if (mVisibleInsetsBuffer == null) mVisibleInsetsBuffer = new Rect();
            mVisibleInsetsBuffer.set(visibleInsets);
            translateRectInAppWindowToScreen(mVisibleInsetsBuffer);
            return mVisibleInsetsBuffer;
        }
        /**
         * Translate the touchable area in application window to Screen. This uses
         * the internal buffer for touchable area to avoid extra object allocation.
         */
        public Region getTranslatedTouchableArea(Region touchableArea) {
            if (mTouchableAreaBuffer == null) mTouchableAreaBuffer = new Region();
            mTouchableAreaBuffer.set(touchableArea);
            mTouchableAreaBuffer.scale(applicationScale);
            return mTouchableAreaBuffer;
        }
    }
}

package com.progela.crmprogela.transfer.ui.view.visita;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VistaDeFirma extends View {
    private Paint paint;
    private Path path;
    private Bitmap bitmap;
    private Canvas canvas;

    public VistaDeFirma(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeVariables();
    }

    private void initializeVariables() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public Bitmap getSignatureBitmap() {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawPath(path, paint);
        return mutableBitmap;
    }


    public void clear() {
        path.reset();
        canvas.drawColor(Color.WHITE);
        invalidate();
    }
}

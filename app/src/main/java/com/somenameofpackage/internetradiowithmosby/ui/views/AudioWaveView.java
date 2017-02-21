package com.somenameofpackage.internetradiowithmosby.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.somenameofpackage.internetradiowithmosby.R;

public class AudioWaveView extends View {
    private byte[] bytes;
    private float[] points;
    private Rect rect;
    private Paint forePaint;

    public AudioWaveView(Context context) {
        super(context);
        init(Color.RED);
    }

    public AudioWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ColorWave);
        int color = attributes.getColor(R.styleable.ColorWave_wave_color, Color.RED);
        init(color);

        attributes.recycle();
    }

    public AudioWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ColorWave);
        int color = attributes.getColor(R.styleable.ColorWave_wave_color, Color.RED);
        init(color);

        attributes.recycle();
    }

    private void init(int color) {
        rect = new Rect();
        forePaint = new Paint();

        bytes = null;
        forePaint.setStrokeWidth(2f);
        forePaint.setAntiAlias(false);
        forePaint.setColor(color);
    }

    public void updateVisualizer(byte[] bytes) {
        this.bytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bytes == null || rect == null) return;
        if (points == null || points.length < bytes.length * 4) {
            points = new float[bytes.length * 4];
        }
        rect.set(0, 0, getWidth(), getHeight());

        for (int i = 0; i < bytes.length - 1; i++) {
            points[i * 4] = rect.width() * i / (bytes.length - 1);
            points[i * 4 + 1] = rect.height() / 2 + ((byte) (bytes[i] + 128)) * (rect.height() / 2) / 128;
            points[i * 4 + 2] = rect.width() * (i + 1) / (bytes.length - 1);
            points[i * 4 + 3] = rect.height() / 2 + ((byte) (bytes[i + 1] + 128)) * (rect.height() / 2) / 128;
        }
        canvas.drawLines(points, forePaint);
    }
}

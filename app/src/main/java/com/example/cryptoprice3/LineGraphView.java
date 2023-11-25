package com.example.cryptoprice3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LineGraphView extends View {

    private List<DataPoint> dataPoints;
    private Paint linePaint;
    private Paint textPaint;
    private Paint axisPaint;
    private double minPrice;
    private double maxPrice;

    public LineGraphView(Context context) {
        super(context);
        init();
    }

    public LineGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(10);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20);

        axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(3);
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        calculateMinMaxPrices();
        invalidate(); // Trigger redraw when data changes
    }

    private void calculateMinMaxPrices() {
        if (dataPoints != null && dataPoints.size() > 0) {
            minPrice = maxPrice = dataPoints.get(0).getValue();
            for (DataPoint dataPoint : dataPoints) {
                double price = dataPoint.getValue();
                minPrice = Math.min(minPrice, price);
                maxPrice = Math.max(maxPrice, price);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints == null || dataPoints.size() < 2) {
            return; // Not enough points to draw a line
        }

        float width = getWidth();
        float height = getHeight();

        float xInterval = width / (dataPoints.size() - 1);
        float yInterval = (float) (height / (maxPrice - minPrice));

        Path path = new Path();
        path.moveTo(0, height - (float) ((dataPoints.get(0).getValue() - minPrice) * yInterval));

        for (int i = 1; i < dataPoints.size(); i++) {
            float x = i * xInterval;
            float y = height - (float) ((dataPoints.get(i).getValue() - minPrice) * yInterval);

            // Smooth the line using cubic Bézier curve
            float prevX = (i - 1) * xInterval;
            float prevY = height - (float) ((dataPoints.get(i - 1).getValue() - minPrice) * yInterval);
            float c1x = prevX + xInterval / 2;
            float c1y = prevY;
            float c2x = x - xInterval / 2;
            float c2y = y;

            path.cubicTo(c1x, c1y, c2x, c2y, x, y);
        }

        // Draw the smoothed line graph
        canvas.drawPath(path, linePaint);

        // Draw X-axis
        canvas.drawLine(0, height, width, height, axisPaint);

        // Draw Y-axis
        canvas.drawLine(0, 0, 0, height, axisPaint);

        // Draw labels on Y-axis
        for (int i = 0; i <= 5; i++) {
            float priceLabel = (float) (minPrice + i * ((maxPrice - minPrice) / 5));
            float yLabel = height - i * (height / 5);
            canvas.drawText(String.format(Locale.US,"%.2f", priceLabel), -textPaint.measureText(String.format(Locale.US,"%.2f", priceLabel)) - 10, yLabel, textPaint);
        }

        // Draw labels on X-axis
        for (int i = 1; i < dataPoints.size(); i++) {
            float x = i * xInterval;
            float y = height + 40;
            canvas.drawText(dataPoints.get(i).getLabel(), x, y, textPaint);
        }
    }


    public static class DataPoint {
        private String label;
        private double value;

        public DataPoint(String label, double value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public double getValue() {
            return value;
        }
    }
}
package com.example.cryptoprice3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.Locale;

public class LineGraphView extends View {

    private List<DataPoint> dataPoints;
    private Paint linePaint;
    private Paint textPaint;

    private Paint titlePaint;
    private Paint axisPaint;
    private double minPrice;
    private double maxPrice;
    private String coinName;

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
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTextSize(40);

        titlePaint = new Paint();
        titlePaint.setColor(Color.LTGRAY);
        titlePaint.setAntiAlias(true);
        titlePaint.setFakeBoldText(true);
        titlePaint.setTextSize(80);

        axisPaint = new Paint();
        axisPaint.setColor(Color.LTGRAY);
        axisPaint.setStrokeWidth(3);
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        calculateMinMaxPrices();
        invalidate(); // Trigger redraw when data changes
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
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

            // Draw labels on X-axis inside the chart
            String label = dataPoints.get(i).getLabel();
            float labelWidth = textPaint.measureText(label);
            canvas.drawText(label, x - labelWidth / 2, height + 40, textPaint);
        }

        // Draw the smoothed line graph
        canvas.drawPath(path, linePaint);

        // Draw X-axis inside the view
        canvas.drawLine(0, height, width, height, axisPaint);

        // Draw Y-axis inside the view
        canvas.drawLine(0, 0, 0, height, axisPaint);

        // Draw X-axis with grid lines and labels inside the chart
        for (int i = 1; i <= 5; i++) {
            float xGrid = i * (width / 6);
            float yGrid = height;

            // Draw grid line
            canvas.drawLine(xGrid, 0, xGrid, height, axisPaint);

            // Draw label on X-axis inside the chart
            String xLabel = dataPoints.get(i * dataPoints.size() / 6).getLabel();
            float labelWidth = textPaint.measureText(xLabel);
            float labelX = xGrid - labelWidth / 2;
            float labelY = height + 40;
            canvas.drawText(xLabel, labelX, labelY, textPaint);
        }

        // Draw Y-axis with grid lines and labels inside the chart
        for (int i = 1; i <= 5; i++) {
            float xGrid = 0;
            float yGrid = i * (height / 6);

            // Draw grid line
            canvas.drawLine(xGrid, yGrid, width, yGrid, axisPaint);

            // Draw label on Y-axis inside the chart
            String yLabel = String.format(Locale.US, "%.2f", minPrice + i * ((maxPrice - minPrice) / 5));
            float labelWidth = textPaint.measureText(yLabel);
            float labelX = -labelWidth - 10;
            float labelY = yGrid + textPaint.getTextSize() / 2;
            canvas.drawText(yLabel, labelX, labelY, textPaint);
        }

        // Draw coin name on top of the chart
        float coinNameWidth = titlePaint.measureText(coinName);
        float coinNameX = (width - coinNameWidth) / 2;
        float coinNameY = 80; // Adjust the Y-coordinate as needed
        canvas.drawText(coinName, coinNameX, coinNameY, titlePaint);
    }

    public static class DataPoint {
        public final String label;
        public final double value;

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

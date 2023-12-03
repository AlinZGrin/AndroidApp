package com.alingrin.cryptoprice3;
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

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(7);
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        calculateMinMaxPrices();
        determineLineColor(); // Determine line color based on coin prices
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

    private void determineLineColor() {
        if (dataPoints != null && dataPoints.size() >= 2) {
            double lastPrice = dataPoints.get(dataPoints.size() - 1).getValue();
            double secondLastPrice = dataPoints.get(dataPoints.size() - 2).getValue();

            if (lastPrice > secondLastPrice) {
                linePaint.setColor(Color.GREEN);
            } else if (lastPrice < secondLastPrice) {
                linePaint.setColor(Color.RED);
            } else {
                // Use default color (Blue) or set another color if needed
                linePaint.setColor(Color.BLUE);
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
            if (i % (dataPoints.size() / 5) == 0) {
                String label = dataPoints.get(i).getLabel();
                float labelWidth = textPaint.measureText(label);
                float labelX = (float) (x - labelWidth / 2 );
                float labelY = height + 40;
                canvas.drawText(label, labelX, labelY, textPaint);
            }
        }

        // Draw the smoothed line graph with the determined line color
        canvas.drawPath(path, linePaint);

        // Draw X-axis inside the view
        canvas.drawLine(0, height, width, height, axisPaint);
        canvas.drawLine(width, height, width, 0, axisPaint);

        // Draw Y-axis inside the view
        canvas.drawLine(0, 0, 0, height, axisPaint);
        canvas.drawLine(1, 1, width, 1, axisPaint);

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
            canvas.drawText(xLabel.substring(0, 5), labelX-5, labelY-64, textPaint);


        }
        String xLabel = dataPoints.get(dataPoints.size()-1).getLabel();
        canvas.drawText(xLabel.substring(0, 5), width-110, height-22, textPaint);
        // Draw Y-axis with grid lines and labels inside the chart
        int j=5;
        for (int i = 1; i <= 5; i++) {
            float xGrid = 0;
            float yGrid = i * (height / 6);

            // Draw grid line
            canvas.drawLine(xGrid, yGrid, width, yGrid, axisPaint);

            // Draw label on Y-axis inside the chart
            String yLabel = String.format(Locale.US, "%.2f", minPrice + (j) * ((maxPrice - minPrice) / 5));
            j=j-1;
            float labelWidth = textPaint.measureText(yLabel);
            float labelX = -labelWidth - 10;
            float labelY = yGrid + textPaint.getTextSize() / 2;
            canvas.drawText(yLabel, 0, labelY+15, textPaint);
        }
        String yLabel = String.format(Locale.US, "%.2f", maxPrice);
        canvas.drawText(yLabel, 4, 0+40, textPaint);
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

package com.alingrin.cryptoprice3;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class LineGraphView extends View {

    private List<DataPoint> dataPoints;
    private Paint linePaint;
    private Paint dotPaint;
    private Paint tooltipPaint;
    private Paint textPaint;
    private Paint titlePaint;
    private Paint axisPaint;
    private double minPrice;
    private double maxPrice;
    private String coinName;

    private DataPoint selectedDataPoint;
    private float dotRadius = 15; // Adjust the dot size as needed
    private float dotX = -1;
    private float dotY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Track touch position
                dotX = touchX;
                dotY = touchY;

                // Find the closest data point to the touch position
                selectedDataPoint = findClosestDataPoint(touchX);
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Reset dot position
                dotX = -1;
                dotY = -1;
                selectedDataPoint = null;
                invalidate();
                return true;
        }

        return super.onTouchEvent(event);
    }

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

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(7);

        dotPaint = new Paint();
        dotPaint.setStyle(Paint.Style.STROKE);
        dotPaint.setStrokeWidth(11);

        tooltipPaint = new Paint();
        tooltipPaint.setColor(Color.TRANSPARENT);
        tooltipPaint.setStyle(Paint.Style.FILL);
        tooltipPaint.setAlpha(70);

        axisPaint = new Paint();
        axisPaint.setColor(Color.LTGRAY);
        axisPaint.setStrokeWidth(3);

        selectedDataPoint = null;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
        calculateMinMaxPrices();
        determineLineColor(); // Determine line color based on coin prices
        invalidate(); // Trigger redraw when data changes
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
        selectedDataPoint = null; // Reset the selected data point when coin name changes
        invalidate(); // Trigger redraw when coin name changes
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
                float labelX = (float) (x - labelWidth / 2);
                float labelY = height + 40;
                canvas.drawText(label, labelX, labelY, textPaint);
            }
        }

        // Draw the smoothed line graph with the determined line color
        canvas.drawPath(path, linePaint);

        // Draw blue dot at the selected data point
        if (selectedDataPoint != null) {
            float dotRadius = 10;
            float dotX = getXCoordinate(selectedDataPoint, xInterval);
            float dotY = getHeight() - (float) ((selectedDataPoint.getValue() - minPrice) * (getHeight() / (maxPrice - minPrice)));
            dotPaint.setColor(Color.BLUE);
            canvas.drawCircle(dotX, dotY, dotRadius, dotPaint);
        }


        // Draw coin name on top of the chart
        float coinNameWidth = titlePaint.measureText(coinName);
        float coinNameX = (width - coinNameWidth) / 2;
        float coinNameY = 80; // Adjust the Y-coordinate as needed
        canvas.drawText(coinName, coinNameX, coinNameY, titlePaint);

        // Draw selected data point tooltip
        if (selectedDataPoint != null) {
            drawTooltip(canvas, selectedDataPoint, xInterval);
        }
    }

    private void drawTooltip(Canvas canvas, DataPoint dataPoint, float xInterval) {
        float x = getXCoordinate(dataPoint, xInterval);
        float y = getHeight() - (float) ((dataPoint.getValue() - minPrice) * (getHeight() / (maxPrice - minPrice)));

        float tooltipWidth = 285;
        float tooltipHeight = 90;
        float cornerRadius = 16;

        // Ensure tooltip stays inside the chart
        if (x < 0) {
            x = 0;
        } else if (x > getWidth() - tooltipWidth) {
            x = getWidth() - tooltipWidth;
        }

        if (y < 0) {
            y = 0;
        } else if (y > getHeight() - tooltipHeight) {
            y = getHeight() - tooltipHeight;
        }
        if (x>getWidth()) { x = getWidth()-285;}
        if (y - tooltipHeight<0) {y=90;}

        RectF tooltipRect = new RectF(x, y - tooltipHeight, x + tooltipWidth, y);
        canvas.drawRoundRect(tooltipRect, cornerRadius, cornerRadius, tooltipPaint);

        // Draw text inside the tooltip
        String dateLabel = dataPoint.getLabel();
        String priceLabel = String.format("Price: %.2f", dataPoint.getValue());

        float textX = x + 10;
        float textY = y - tooltipHeight + 40;
        canvas.drawText(dateLabel, textX, textY, textPaint);
        canvas.drawText(priceLabel, textX, textY + 40, textPaint);
    }

    private float getXCoordinate(DataPoint dataPoint, float xInterval) {
        for (int i = 0; i < dataPoints.size(); i++) {
            if (dataPoints.get(i).equals(dataPoint)) {
                return i * xInterval;
            }
        }
        return 0;
    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectedDataPoint = findClosestDataPoint(touchX);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                return true;
            case MotionEvent.ACTION_CANCEL:
                selectedDataPoint = null;
                invalidate();
                return true;
        }

        return super.onTouchEvent(event);
    }
*/
    private DataPoint findClosestDataPoint(float touchX) {
        float xInterval = getWidth() / (dataPoints.size() - 1);
        int closestIndex = (int) (touchX / xInterval + 0.5f); // Round to the nearest index
        closestIndex = Math.max(0, Math.min(closestIndex, dataPoints.size() - 1)); // Ensure it's within bounds
        return dataPoints.get(closestIndex);
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

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            DataPoint dataPoint = (DataPoint) obj;

            return Double.compare(dataPoint.value, value) == 0 &&
                    label.equals(dataPoint.label);
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = label.hashCode();
            temp = Double.doubleToLongBits(value);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}

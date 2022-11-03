package com.apache.flink.jointestcase.watermark;


import static org.apache.flink.util.Preconditions.checkArgument;

import static org.apache.flink.util.Preconditions.checkNotNull;

import java.time.Duration;



import org.apache.flink.api.common.eventtime.BoundedOutOfOrdernessWatermarks;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apache.flink.jointestcase.DateUtil;

public class EDWBoundedOutOfOrdernessWatermarks <T> implements WatermarkGenerator<T> {

    /** The maximum timestamp encountered so far. */
    private long maxTimestamp;

    /** The maximum out-of-orderness that this watermark generator assumes. */
    private final long outOfOrdernessMillis;

    /**
     * Creates a new watermark generator with the given out-of-orderness bound.
     *
     * @param maxOutOfOrderness The bound for the out-of-orderness of the event timestamps.
     */
    public EDWBoundedOutOfOrdernessWatermarks(Duration maxOutOfOrderness) {
        checkNotNull(maxOutOfOrderness, "maxOutOfOrderness");
        checkArgument(!maxOutOfOrderness.isNegative(), "maxOutOfOrderness cannot be negative");

        this.outOfOrdernessMillis = maxOutOfOrderness.toMillis();

        // start so that our lowest watermark would be Long.MIN_VALUE.
        this.maxTimestamp = Long.MIN_VALUE + outOfOrdernessMillis + 1;
    }

    // ------------------------------------------------------------------------

    @Override
    public void onEvent(T event, long eventTimestamp, WatermarkOutput output) {
        maxTimestamp = Math.max(maxTimestamp, eventTimestamp);
 
    }

    @Override
    public void onPeriodicEmit(WatermarkOutput output) {
    	long waterMark = maxTimestamp - outOfOrdernessMillis - 1;
//    	System.out.println("maxTimestamp  >>> "+maxTimestamp + "       "+"outOfOrdernessMillis  "+outOfOrdernessMillis);
    	if(waterMark > 0) {
    		String s = DateUtil.convertLongToTimeStamp(waterMark);
    		System.out.println("Emitting WaterMark "+s);
    	}
        output.emitWatermark(new Watermark(maxTimestamp - outOfOrdernessMillis - 1));
    }
}
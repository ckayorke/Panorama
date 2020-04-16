/**
 * 
 */
package com.bosch.mtprotocol.glm100C;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author AndrejsC
 *
 */
public class MtTimer  {
	
	private int period;
	private int delay;
	
	private MtTimerTask mtTimerTask;
	private Timer timer;
	private MtTimerListener listener;
	
	/**
	 * @param period
	 * @param delay
	 */
	public MtTimer(int period, int delay) {
		super();
		this.period = period;
		this.delay = delay;
	}

	/**
	 * @param delay
	 */
	public MtTimer(int delay) {
		super();
		this.delay = delay;
	}

	private class MtTimerTask extends TimerTask {

        public void run() {
            if(listener != null){
            	listener.onTimerTick();
            }
        }
    }

	public void start(){
		
		stop();
		mtTimerTask = new MtTimerTask();
		timer = new Timer();
		if(period == 0){
			timer.schedule(mtTimerTask, delay);
		}else{
			timer.schedule(mtTimerTask, delay, period);
		}
	}
	
	public void stop(){
		if(mtTimerTask != null){
			mtTimerTask.cancel();
			mtTimerTask = null;
		}
		
		if(timer != null){
			timer.cancel();
			timer = null;
		}

	}
	
	/**
	 * @return the listener
	 */
	public MtTimerListener getListener() {
		return listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(MtTimerListener listener) {
		this.listener = listener;
	}
	
	public interface MtTimerListener{
		public void onTimerTick();
	}
}

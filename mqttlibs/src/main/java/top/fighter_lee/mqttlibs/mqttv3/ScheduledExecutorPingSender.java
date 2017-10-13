/*******************************************************************************
 * Copyright (c) 2014 IBM Corp.
 * Copyright (c) 2017 BMW Car IT GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 */

package top.fighter_lee.mqttlibs.mqttv3;


import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import top.fighter_lee.mqttlibs.mqttv3.internal.ClientComms;


/**
 * Default ping sender implementation
 *
 * <p>This class implements the {@link IMqttPingSender} pinger interface
 * allowing applications to send ping packet to server every keep alive interval.
 * </p>
 *
 * @see MqttPingSender
 */
public class ScheduledExecutorPingSender implements MqttPingSender {
	private static final String CLASS_NAME = ScheduledExecutorPingSender.class.getName();

	private ClientComms comms;
	private ScheduledExecutorService executorService;
	private ScheduledFuture scheduledFuture;
	private String clientid;

	public ScheduledExecutorPingSender(ScheduledExecutorService executorService) {
		if (executorService == null) {
			throw new IllegalArgumentException("ExecutorService cannot be null.");
		}
		this.executorService = executorService;
	}

	public void init(ClientComms comms) {
		if (comms == null) {
			throw new IllegalArgumentException("ClientComms cannot be null.");
		}
		this.comms = comms;
		clientid = comms.getClient().getClientId();
	}

	public void start() {
		final String methodName = "start";

		//@Trace 659=start timer for client:{0}
		//Check ping after first keep alive interval.
		schedule(comms.getKeepAlive());
	}

	public void stop() {
		final String methodName = "stop";
		//@Trace 661=stop
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);
		}
	}

	public void schedule(long delayInMilliseconds) {
		scheduledFuture = executorService.schedule(new PingRunnable(), delayInMilliseconds, TimeUnit.MILLISECONDS);
	}

	private class PingRunnable implements Runnable {
		private static final String methodName = "PingTask.run";

		public void run() {
			String originalThreadName = Thread.currentThread().getName();
			Thread.currentThread().setName("MQTT Ping: " + clientid);
			//@Trace 660=Check schedule at {0}
			comms.checkForActivity();
			Thread.currentThread().setName(originalThreadName);
		}
	}
}

package com.bosch.mtprotocol.glm100C.message;

import java.util.HashMap;
import java.util.Map;

import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtFrameFactory;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.general.message.echo.DoEchoFrameFactory;
import com.bosch.mtprotocol.general.message.echo.DoEchoMessage;
import com.bosch.mtprotocol.general.message.keypad.KeypadPatternFrameFactory;
import com.bosch.mtprotocol.general.message.keypad.KeypadPatternMessage;
import com.bosch.mtprotocol.general.message.peer.SetActivePeerFrameFactory;
import com.bosch.mtprotocol.general.message.peer.SetActivePeerOutputMessage;
import com.bosch.mtprotocol.general.message.rtc.RTCTimestampFrameFactory;
import com.bosch.mtprotocol.general.message.rtc.RTCTimestampMessage;
import com.bosch.mtprotocol.glm100C.message.edc.EDCDoRemoteFrameFactory;
import com.bosch.mtprotocol.glm100C.message.edc.EDCDoRemoteTriggerButtonMessage;
import com.bosch.mtprotocol.glm100C.message.edc.EDCFrameFactory;
import com.bosch.mtprotocol.glm100C.message.edc.EDCOutputMessage;
import com.bosch.mtprotocol.glm100C.message.settings.SettingsFrameFactory;
import com.bosch.mtprotocol.glm100C.message.settings.SettingsMessage;
import com.bosch.mtprotocol.glm100C.message.single.SingleDistFrameFactory;
import com.bosch.mtprotocol.glm100C.message.single.SingleDistOutputMessage;
import com.bosch.mtprotocol.glm100C.message.sync.SyncFrameFactory;
import com.bosch.mtprotocol.glm100C.message.sync.SyncOutputMessage;
import com.bosch.mtprotocol.glm100C.message.sync.list.SyncListFrameFactory;
import com.bosch.mtprotocol.glm100C.message.sync.list.SyncListOutputMessage;
import com.bosch.mtprotocol.linelaser.message.ClearCalibrationEvents.ClearCalibrationEventsFrameFactory;
import com.bosch.mtprotocol.linelaser.message.ClearCalibrationEvents.ClearCalibrationEventsMessage;
import com.bosch.mtprotocol.linelaser.message.GCLDevInfo.GCLDevInfoFrameFactory;
import com.bosch.mtprotocol.linelaser.message.GCLDevInfo.GCLDevInfoOutputMessage;
import com.bosch.mtprotocol.linelaser.message.GetPowerProfile.PowerProfileFrameFactory;
import com.bosch.mtprotocol.linelaser.message.GetPowerProfile.PowerProfileOutputMessage;
import com.bosch.mtprotocol.linelaser.message.MotorOperations.MotorOperationsFrameFactory;
import com.bosch.mtprotocol.linelaser.message.MotorOperations.MotorOperationsOutputMessage;
import com.bosch.mtprotocol.linelaser.message.RemoteControlKey.RemoteControlKeyFrameFactory;
import com.bosch.mtprotocol.linelaser.message.RemoteControlKey.RemoteControlKeyOutputMessage;
import com.bosch.mtprotocol.linelaser.message.SetLasers.SetLasersFrameFactory;
import com.bosch.mtprotocol.linelaser.message.SetLasers.SetLasersOutputMessage;
import com.bosch.mtprotocol.linelaser.message.SyncCalibrationData.SyncCalibrationDataFrameFactory;
import com.bosch.mtprotocol.linelaser.message.SyncCalibrationData.SyncCalibrationDataOutputMessage;
import com.bosch.mtprotocol.rotation.message.access_lock.AccessLockFrameFactory;
import com.bosch.mtprotocol.rotation.message.access_lock.AccessLockOutputMessage;
import com.bosch.mtprotocol.rotation.message.calibration.SyncCalibrationFrameFactory;
import com.bosch.mtprotocol.rotation.message.calibration.SyncCalibrationOutputMessage;
import com.bosch.mtprotocol.rotation.message.eeprom_data.EEPROMDATAFrameFactory;
import com.bosch.mtprotocol.rotation.message.eeprom_data.EEPROMDataOutputMessage;
import com.bosch.mtprotocol.rotation.message.log.LogPacketFrameFactory;
import com.bosch.mtprotocol.rotation.message.log.LogPacketOutputMessage;
import com.bosch.mtprotocol.rotation.message.log.LogSizeFrameFactory;
import com.bosch.mtprotocol.rotation.message.log.LogSizeOutputMessage;
import com.bosch.mtprotocol.rotation.message.slope.LaserSlopeFrameFactory;
import com.bosch.mtprotocol.rotation.message.slope.LaserSlopeMessage;
import com.bosch.mtprotocol.rotation.message.status.SyncStatusFrameFactory;
import com.bosch.mtprotocol.rotation.message.status.SyncStatusOutputMessage;
import com.bosch.mtprotocol.thermo.message.edct.EDCTFrameFactory;
import com.bosch.mtprotocol.thermo.message.edct.EDCTOutputMessage;
// import MT protocol general classes
// import LRF specific classes
// import Thermo specific classes

public class FrameFactoryImpl implements MtFrameFactory{

	// A list of supported factories
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> factories = new HashMap<String, Class>();

	static{
		synchronized (factories) {
			// register simple message factory
			registerFactory(SimpleMessage.class, SimpleFrameFactoryImpl.class);
			registerFactory(SimpleResponse.class, SimpleResponseFactoryImpl.class);
			
			// register general factories
			registerFactory(RTCTimestampMessage.class, RTCTimestampFrameFactory.class);
			registerFactory(KeypadPatternMessage.class, KeypadPatternFrameFactory.class);
			registerFactory(SetActivePeerOutputMessage.class, SetActivePeerFrameFactory.class);
			registerFactory(DoEchoMessage.class, DoEchoFrameFactory.class);
			
			// register LRF factories
			registerFactory(SingleDistOutputMessage.class, SingleDistFrameFactory.class);
			registerFactory(SettingsMessage.class, SettingsFrameFactory.class);
			registerFactory(SyncOutputMessage.class, SyncFrameFactory.class);
			registerFactory(SyncListOutputMessage.class, SyncListFrameFactory.class);
			registerFactory(EDCOutputMessage.class, EDCFrameFactory.class);
			registerFactory(EDCDoRemoteTriggerButtonMessage.class, EDCDoRemoteFrameFactory.class);
			
			// register Thermo factories
			registerFactory(EDCTOutputMessage.class, EDCTFrameFactory.class);
			
			// register Rotation factories
			registerFactory(SyncStatusOutputMessage.class, SyncStatusFrameFactory.class);
			registerFactory(LaserSlopeMessage.class, LaserSlopeFrameFactory.class);
			registerFactory(AccessLockOutputMessage.class, AccessLockFrameFactory.class);
			registerFactory(LogSizeOutputMessage.class, LogSizeFrameFactory.class);
			registerFactory(LogPacketOutputMessage.class, LogPacketFrameFactory.class);
			registerFactory(SyncCalibrationOutputMessage.class, SyncCalibrationFrameFactory.class);

			registerFactory(EEPROMDataOutputMessage.class, EEPROMDATAFrameFactory.class);

			// register Line Laser factories
			registerFactory(GCLDevInfoOutputMessage.class, GCLDevInfoFrameFactory.class);
			registerFactory(SetLasersOutputMessage.class, SetLasersFrameFactory.class);
			registerFactory(MotorOperationsOutputMessage.class, MotorOperationsFrameFactory.class);
			registerFactory(RemoteControlKeyOutputMessage.class, RemoteControlKeyFrameFactory.class);
			registerFactory(PowerProfileOutputMessage.class, PowerProfileFrameFactory.class);
			registerFactory(SyncCalibrationDataOutputMessage.class, SyncCalibrationDataFrameFactory.class);
			registerFactory(ClearCalibrationEventsMessage.class, ClearCalibrationEventsFrameFactory.class);

		}
	}	


	/* (non-Javadoc)
	 * @see de.bode.bosch.mtprotocolmeFactory#createFrame(de.bosde.bosch.mtprotocolage)
	 */
	@Override
	public MtFrame createFrame(MtMessage message) {
		if(message == null){
			throw new IllegalArgumentException("MtMessage can't be null");
		}

		@SuppressWarnings("unchecked")
		Class<? extends MtFrameFactory> factoryClass = factories.get(message.getClass().getSimpleName());
		if(factoryClass == null){
			factoryClass = SimpleFrameFactoryImpl.class;
		}

		MtFrameFactory factory;
		try {
			factory = factoryClass.newInstance();
		} catch (Exception e) {
			// fatal error
			throw new RuntimeException(e);
		}
		
		return factory.createFrame(message);
	}

	private static void registerFactory(Class<? extends MtMessage> messageClass, 
			Class<? extends MtFrameFactory> factoryClass){
		factories.put(messageClass.getSimpleName(), factoryClass);
	}
}

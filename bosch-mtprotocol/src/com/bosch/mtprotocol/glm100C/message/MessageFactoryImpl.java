package com.bosch.mtprotocol.glm100C.message;

import java.util.HashMap;
import java.util.Map;

// import MT protocol general classes
import com.bosch.mtprotocol.MtFrame;
import com.bosch.mtprotocol.MtMessage;
import com.bosch.mtprotocol.MtMessageFactory;
import com.bosch.mtprotocol.general.message.dev_info.DevInfoMessageFactory;
import com.bosch.mtprotocol.general.message.echo.DoEchoMessageFactory;
import com.bosch.mtprotocol.general.message.keypad.KeypadPatternMessageFactory;
import com.bosch.mtprotocol.general.message.rtc.RTCTimestampMessageFactory;
// import LRF specific classes
import com.bosch.mtprotocol.glm100C.frame.MtBaseFrame;
import com.bosch.mtprotocol.glm100C.message.edc.EDCMessageFactory;
import com.bosch.mtprotocol.glm100C.message.settings.SettingsMessageFactory;
import com.bosch.mtprotocol.glm100C.message.single.SingleDistMessageFactory;
import com.bosch.mtprotocol.glm100C.message.sync.SyncMessageFactory;
import com.bosch.mtprotocol.glm100C.message.sync.list.SyncListMessageFactory;
import com.bosch.mtprotocol.linelaser.message.GCLDevInfo.GCLDevInfoMessageFactory;
import com.bosch.mtprotocol.linelaser.message.GetPowerProfile.PowerProfileMessageFactory;
import com.bosch.mtprotocol.linelaser.message.LastCalibrationData.LastCalibrationDataMessageFactory;
import com.bosch.mtprotocol.linelaser.message.MotorOperations.MotorOperationsMessageFactory;
import com.bosch.mtprotocol.linelaser.message.RemoteControlKey.RemoteControlKeyMessageFactory;
import com.bosch.mtprotocol.linelaser.message.SetLasers.SetLasersMessageFactory;
import com.bosch.mtprotocol.linelaser.message.SyncCalibrationData.SyncCalibrationDataMessageFactory;
import com.bosch.mtprotocol.linelaser.message.SystemStates.SystemStateMessageFactory;
import com.bosch.mtprotocol.rotation.message.calibration.CalibrationGuardDataMessagFactory;
import com.bosch.mtprotocol.rotation.message.calibration.SyncCalibrationMessageFactory;
import com.bosch.mtprotocol.rotation.message.eeprom_data.EEPROMDataMessageFactory;
import com.bosch.mtprotocol.rotation.message.grl_info.GRLDevInfoMessageFactory;
import com.bosch.mtprotocol.rotation.message.log.LogPacketMessageFactory;
import com.bosch.mtprotocol.rotation.message.log.LogSizeMessageFactory;
import com.bosch.mtprotocol.rotation.message.slope.LaserSlopeMessageFactory;
import com.bosch.mtprotocol.rotation.message.status.SyncStatusMessageFactory;
// import Thermo specific classes
import com.bosch.mtprotocol.thermo.message.edct.EDCTMessageFactory;
import com.bosch.mtprotocol.thermo.message.imgdata.ImgDataMessageFactory;
import com.bosch.mtprotocol.thermo.message.imginfo.ImgInfoMessageFactory;

public class MessageFactoryImpl implements MtMessageFactory {

	// A list of supported factories
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> factories = new HashMap<String, Class>();

	static{
		synchronized (factories) {
			// register General factories
			factories.put("6", DevInfoMessageFactory.class);
			factories.put("15", RTCTimestampMessageFactory.class);
			factories.put("27", KeypadPatternMessageFactory.class);
			factories.put("62", DoEchoMessageFactory.class);

			// register LRF factories
			factories.put("64", SingleDistMessageFactory.class);
			factories.put("80", SyncMessageFactory.class);
			factories.put("81", SyncListMessageFactory.class);
			factories.put("83", SettingsMessageFactory.class);
			factories.put("85", EDCMessageFactory.class);
			
			// register Thermo factories
			factories.put("94", EDCTMessageFactory.class);
			factories.put("95", ImgInfoMessageFactory.class);
			factories.put("96", ImgDataMessageFactory.class);
			
			// register Rotation factories
			factories.put("100", SyncStatusMessageFactory.class);
			factories.put("101", GRLDevInfoMessageFactory.class);
			factories.put("102", LaserSlopeMessageFactory.class);
			factories.put("105", LogSizeMessageFactory.class);
			factories.put("106", LogPacketMessageFactory.class);
			factories.put("107", CalibrationGuardDataMessagFactory.class);
			factories.put("108", SyncCalibrationMessageFactory.class);

			factories.put("109", EEPROMDataMessageFactory.class);

			// register Line Laser factories
			factories.put("70", GCLDevInfoMessageFactory.class);
			factories.put("71", SystemStateMessageFactory.class);
			factories.put("72", MotorOperationsMessageFactory.class);
			factories.put("73", SetLasersMessageFactory.class);
            factories.put("74", RemoteControlKeyMessageFactory.class);
			factories.put("75", PowerProfileMessageFactory.class);
			factories.put("76", LastCalibrationDataMessageFactory.class);
			factories.put("77", SyncCalibrationDataMessageFactory.class);
		}
	}	

	@Override
	public MtMessage createMessage(MtFrame frame) {

		if(frame == null){
			throw new IllegalArgumentException("MtFrame can't be null");
		}
		
		if(frame instanceof MtBaseFrame){
			MtBaseFrame f = (MtBaseFrame) frame;
			int command = f.getCommand();
			@SuppressWarnings("unchecked")
			Class<? extends MtMessageFactory> factoryClass = factories.get(Integer.toString(command));
			if(factoryClass == null){
				factoryClass = SimpleMessageFactory.class;
			}

			MtMessageFactory factory;
			try {
				factory = factoryClass.newInstance();
			} catch (Exception e) {
				// fatal error
				throw new RuntimeException(e);
			}
			
			return factory.createMessage(frame);
		}
		
		throw new IllegalArgumentException("Can't create MtMessage from " + frame + "; Unknown frame!");
	}

}

package network.Protocol.Packet;

import Container.SerialVersionContainer;
import Enums.AccommodationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccomRecognizeInfo implements Serializable {

	@Serial
	private static final long serialVersionUID = SerialVersionContainer.getSerialVersionUID();
	private Integer accomID;
	private AccommodationStatus status;
}

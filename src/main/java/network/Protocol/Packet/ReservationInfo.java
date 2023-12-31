package network.Protocol.Packet;

import Container.SerialVersionContainer;
import lombok.*;
import persistence.dto.AccommodationDTO;
import persistence.dto.ReservationDTO;
import persistence.dto.UserDTO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationInfo implements Serializable {
	@Serial
	private static final long serialVersionUID = SerialVersionContainer.getSerialVersionUID();
	private List<ReservationDTO> reservationDTOS;
	private List<AccommodationDTO> accommodationDTOS;
	private List<UserDTO> userDTOS;
}

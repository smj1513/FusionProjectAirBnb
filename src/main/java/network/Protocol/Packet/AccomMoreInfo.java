package network.Protocol.Packet;

import Container.SerialVersionContainer;
import lombok.*;
import persistence.dto.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccomMoreInfo implements Serializable {

	@Serial
	private static final long serialVersionUID = SerialVersionContainer.getSerialVersionUID();

	private AccommodationDTO curAccom;
	private RatePolicyDTO accomRate;
	private List<AmenityDTO> amenityList;
	private List<ReviewDTO> reviewList;
	private List<ReservationDTO> reservationList;
}

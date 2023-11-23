package service;

import lombok.Builder;
import persistence.dao.AccommodationDAO;
import persistence.dao.ReservationDAO;
import persistence.dao.ReviewDAO;
import persistence.dao.UserDAO;
import persistence.dto.ReservationDTO;
import persistence.dto.ReviewDTO;
import persistence.dto.UserDTO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Builder
public class UserService {
	private UserDAO userDAO;
	private AccommodationDAO acDAO;
	private ReservationDAO rDAO;
	private ReviewDAO reviewDAO;

	public UserService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}


	public UserService(UserDAO userDAO, AccommodationDAO accommodationDAO, ReservationDAO rDAO, ReviewDAO reviewDAO) {
		this.userDAO = userDAO;
		acDAO = accommodationDAO;
		this.rDAO = rDAO;
		this.reviewDAO = reviewDAO;
	}

	private boolean login(String id, String pw) {
		return pw.equals(userDAO.selectPwById(id));
	}

	public Optional<UserDTO> loginUser(String id, String pw) {
		if (login(id, pw)) {
			return Optional.of(userDAO.selectById(id));
		} else {
			return Optional.empty();
		}
	}

	public UserDTO changePrivacy(UserDTO userDTO, String name, Date birth, String phone) {
		String newName, newPhone;
		Date newBirth;
		if (name.equalsIgnoreCase("x")) {
			newName = userDTO.getName();
		} else {
			newName = name;
		}
		if (birth == null) {
			newBirth = userDTO.getBirth();
		} else {
			newBirth = birth;
		}
		if (phone.equalsIgnoreCase("x")) {
			newPhone = userDTO.getPhone();
		} else {
			newPhone = phone;
		}
		userDTO.setName(newName);
		userDTO.setPhone(newPhone);
		userDTO.setBirth(newBirth);
		userDAO.updateUser(userDTO);
		return userDTO;
	}

	synchronized public boolean reserveRequest(ReservationDTO userInputReserve) { //TODO : 구현요
		int accomId = userInputReserve.getReservationID();
		int roomID = userInputReserve.getRoomID();
		LocalDate checkIn = userInputReserve.getCheckIn();
		LocalDate checkOut = userInputReserve.getCheckOut();

		List<ReservationDTO> reserves = rDAO.reservationCheck(accomId, roomID, checkIn, checkOut);
		if (reserves.isEmpty()) { //해당 날짜에 예약된 방이 없음을 의미.
			rDAO.insertReservation(userInputReserve);
			return true;
		} else {
			return false;
		}
	}

	public void insertReview(ReviewDTO reviewDTO) {

	}

}

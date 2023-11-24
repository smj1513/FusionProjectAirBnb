package Controller;

import Container.IocContainer;
import network.Protocol.Enums.Method;
import network.Protocol.Enums.RoleType;
import network.Protocol.Request;
import network.Protocol.Response;
import persistence.dto.AccommodationDTO;
import service.AccommodationService;

import java.util.List;
import java.util.Map;

public class AccommodationController implements MethodController {
	private final AccommodationService accomService;

	public AccommodationController(AccommodationService accomService) {
		this.accomService = accomService;
	}
	public AccommodationController(IocContainer iocContainer){
		this.accomService = iocContainer.accommodationService();
	}
	/*
	public void accomMenu() {
        int order = accomView.displayAccomMenu();
        switch (order) {
            case 1 -> accomListMenu();
            case 2 -> insertAccom();
            case 3 -> setAccomPolicy();
            case 4 -> setAccomDiscount();
            default -> System.out.println("잘못 입력 하셨습니다.");
        }
	}
*/

	public Response selectAccomList() {
		Response response = new Response();
		response.setIsSuccess(true);

		List<AccommodationDTO> curAccomList = accomService.selectAccom("승인됨");
		response.setPayload(curAccomList);
		return response;
	}

/*	public void getAccomInfo() {
		int accomID = accomView.getAccomNumberFromUser();
		AccommodationDTO curAccom = accomService.getAccom(accomID);
		accomView.displayAccomInfo(curAccom, accomService.getRate(accomID));
		accomView.displayAmenity(accomService.getAmenityList(accomID));
		accomView.displayReviews(accomService.getReviews(accomID));
		LocalDate date = accomView.getReservationDate();
		List<ReservationDTO> reservationDTOS = accomService.getReservationList(accomID, date);
		accomView.displayReservationCalendar(date, curAccom.getCapacity(), reservationDTOS);
	}*/

/*	public List<AccommodationDTO> setSearchFilters() {
		Map<String, Object> filters = new HashMap<>(); //Request의 Payload에 담겨서 온다.
		filters.put("status", "승인됨");
		while (true) {
			int order = accomView.displayFilterList(); // Client로 가야됨.
			switch (order) {
				case 1 -> filters.put("accomName", accomView.getAccomNameFromUser());
				case 2 -> filters.put("period", accomView.getPeriodFromUser());
				case 3 -> filters.put("capacity", accomView.getCapacityFromUser());
				case 4 -> filters.put("accomType", accomView.getAccomTypeFromUser());
				default -> {
					return accomService.selectAccom(filters);
				}
			}
		}
	}

	public void insertAccom() {
		accomService.insertAccom(accomView.getAccomInfoFromUser());
	}*/
/*
	private void setAccomPolicy() {
		List<AccommodationDTO> curAccomList = accomService.selectAccom("승인됨");
		while (true) {
			accomView.displayAccomList(curAccomList);
			accomView.Return();
			int order = accomView.getAccomNumberFromUser();
			if (order == 0) {
				accomMenu();
				return;
			} else {
				//TODO:  accomService.setAccomPolicy(accomView.getRatePolicyFromUser(order));
			}
		}
	}*/
/*

	public void setAccomDiscount() {
		List<AccommodationDTO> curAccomList = accomService.selectAccom("승인됨");
		while (true) {
			accomView.displayAccomList(curAccomList);
			accomView.Return();
			int order = accomView.getDailyOrDiscount();
			switch (order) {
				case 1 -> getAccomInfo();
				case 2 -> curAccomList = setSearchFilters();
				default -> {
					System.out.println("<<종료>>");
					return;
				}
			}
		}
	}
*/

	@Override
	public Response handle(Request req) {
		Method method = req.getMethod();
		Response res = null;
		switch (method) {
			case GET -> {
				res = getHandle(req);
			}
			case PUT -> {
				res = putHandle(req);
			}
			case POST -> {
				res = postHandle(req);
			}
			case DELETE -> {
				res = deleteHandle(req);
			}
		}
		return res;
	}

	@Override
	public Response getHandle(Request req) {
		RoleType roleType = req.getRoleType();
		Response res = null;
		switch (roleType) {
			case COMMON -> {

			}
			case ADMIN -> {
			}
			case HOST -> {
			}
			case GUEST -> {
				res = selectAccomList();
			}
		}
		return res;

	}

	@Override
	public Response putHandle(Request req) {
		RoleType roleType = req.getRoleType();
		Response res = null;
		switch (roleType) {
			case COMMON -> {

			}
			case ADMIN -> {

			}
			case HOST -> {

			}
			case GUEST -> { //숙소 필터링
				res = accomFiltering(req);
			}
		}
		return res;
	}


	@Override
	public Response postHandle(Request req) {
		RoleType roleType = req.getRoleType();
		Response res = null;
		switch (roleType) {
			case COMMON -> {
			}
			case ADMIN -> {
			}
			case HOST -> {
			}
			case GUEST -> {
			}

		}

		return res;
	}

	@Override
	public Response deleteHandle(Request req) {
		RoleType roleType = req.getRoleType();
		Response res = null;
		switch (roleType) {
			case COMMON -> {
			}
			case ADMIN -> {
			}
			case HOST -> {
			}
			case GUEST -> {
			}
		}

		return res;
	}

	public Response accomFiltering(Request req) {
		Map<String, Object> filters = (Map<String, Object>) req.getPayload();
		filters.put("status", "승인됨");
		List<AccommodationDTO> acList = accomService.selectAccom(filters);
		Response rs = new Response();
		rs.setIsSuccess(true);
		rs.setPayload(acList);
		return rs;
	}
}



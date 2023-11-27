package network.Client.Handler;

import Container.ViewContainer;
import Enums.AccommodationStatus;
import network.Client.ActorHandler;
import network.Protocol.Enums.Method;
import network.Protocol.Enums.PayloadType;
import network.Protocol.Enums.RoleType;
import network.Protocol.Packet.AccomRecognizeInfo;
import network.Protocol.Request;
import network.Protocol.Response;
import persistence.dto.AccommodationDTO;
import persistence.dto.ReservationDTO;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.YearMonth;
import java.util.List;

public class AdminHandler extends ActorHandler {

	public AdminHandler(ViewContainer viewContainer, ObjectOutputStream oos, ObjectInputStream ois) {
		super(viewContainer, oos, ois);
	}

	@Override
	public void run() {
		int jobOption = -1;
		while (jobOption != 0) {
			jobOption = userView.selectAdminJob();
			selectAdminJob(jobOption);
		}
	}

	private void selectAdminJob(int select) {
		switch (select) {
			case 0 -> {
				System.out.println("이전 페이지로");
			}
			case 1 -> { // TODO:숙소 등록 승인 거절
				accomRecognizeChange();
			}
			case 2 -> { //TODO : 숙소별 월별 예약 현황확인
				showReservationByAccom();
			}
			case 3 -> { //TODO : 숙소별 월별 총매출 확인

			}
		}
	}

	private void showReservationByAccom() {
		Request request = Request.builder().roleType(RoleType.GUEST).payloadType(PayloadType.ACCOMMODATION).method(Method.GET).build();
		Response response = requestToServer(request);
		if (response.getIsSuccess()) {
			List<AccommodationDTO> accomList = (List<AccommodationDTO>) response.getPayload();
			accomView.displayAccomList(accomList);
			int select = accomView.readAccomIndex(accomList);
			AccommodationDTO selectAccom = accomList.get(select);
			System.out.println("조회할 년도를 입력하세요 : ");
			int year = Integer.parseInt(sc.nextLine());
			System.out.println("조회할 월을 입력하세요 : ");
			int month = Integer.parseInt(sc.nextLine());
			Request request2 = Request.builder().roleType(RoleType.ADMIN).payloadType(PayloadType.RESERVATION).method(Method.GET).build();
			YearMonth yearMonth = YearMonth.of(year, month);
			Object[] payload = {selectAccom, yearMonth};
			request2.setPayload(payload);
			Response response2 = requestToServer(request2);
			if (response2.getIsSuccess()) {
				List<ReservationDTO> reservationList = (List<ReservationDTO>) response2.getPayload();
				System.out.println(selectAccom.getAccomName() + "의 " + year + "년 " + month + "월 예약 현황");
				reservationView.displayReservations(reservationList);
			}
		}else{
			System.out.println("숙소 정보가 없습니다.");
		}
	}

	private List<AccommodationDTO> viewReadyAccomList() {
		Request request = Request.builder().roleType(RoleType.ADMIN).payloadType(PayloadType.ACCOMMODATION).method(Method.GET).build();
		Response response = requestToServer(request);
		List<AccommodationDTO> readyAccomList = null;
		if (response.getIsSuccess()) {
			readyAccomList = (List<AccommodationDTO>) response.getPayload();
			System.out.println("=============승인 대기 중인 숙소 목록===============");
			accomView.displayAccomList(readyAccomList);
		}
		return readyAccomList;
	}

	private void accomRecognizeChange() {
		List<AccommodationDTO> accomList = viewReadyAccomList();
		if (!accomList.isEmpty()) {
			int select = accomView.readAccomIndex(accomList);
			AccommodationDTO selectAccom = accomList.get(select);
			System.out.println("1.승인 2.거절 : ");
			int statusNum = Integer.parseInt(sc.nextLine());
			AccommodationStatus status = switch (statusNum) {
				case 1 -> AccommodationStatus.Confirmed;
				case 2 -> AccommodationStatus.Refused;
				default -> throw new IllegalArgumentException();
			};
			Request request = Request.builder().roleType(RoleType.ADMIN).method(Method.PUT).payloadType(PayloadType.USER).build();
			AccomRecognizeInfo recognizeInfo = new AccomRecognizeInfo(selectAccom.getAccomID(), status);
			request.setPayload(recognizeInfo);
			Response response = requestToServer(request);
			if (response.getIsSuccess()) {
				System.out.println("갱신이 완료되었습니다.");
			}
		}else{
			System.out.println("숙소 정보가 없습니다.");
		}
	}


}

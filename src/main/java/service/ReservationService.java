package service;

import Container.IocContainer;
import persistence.dao.DiscountPolicyDAO;
import persistence.dao.RatePolicyDAO;
import persistence.dao.ReservationDAO;
import persistence.dto.*;

import java.time.LocalDate;
import java.util.*;

public class ReservationService {
    private ReservationDAO reservationDAO;
    private RatePolicyDAO ratePolicyDAO;
    private DiscountPolicyDAO discountDAO;


    public ReservationService(IocContainer iocContainer){
        this.reservationDAO = iocContainer.reservationDAO();
        this.ratePolicyDAO = iocContainer.ratePolicyDAO();
        this.discountDAO = iocContainer.discountPolicyDAO();
    }

    // 4.2 숙박 예약 현황 보기(달력 화면 구성으로)
    // 13.4 숙소의 예약 가능 일자 확인
    public List<ReservationDTO> getReservationList(AccommodationDTO accomDTO, LocalDate date){
        Map<String, Object> filters = new HashMap<>();
        filters.put("accomID", accomDTO.getAccomID());
        filters.put("checkIn", date);
        filters.put("checkOut", date.plusMonths(1));

        return reservationDAO.getReservations(filters);
    }


    // 5. 게스트의 숙박 예약 승인/거절
    // 15. (MyPage)예약 취소
    public int updateReservation(ReservationDTO rDTO){
        return reservationDAO.updateReservation(rDTO);
    }

    // 16. (MyPage)예약 현황 조회(완료된 숙박, 예약 대기 숙소, 예약된 숙소)
    public List<ReservationDTO> getReservationListByUserID(UserDTO user, String status){
        Map<String, Object> filters = new HashMap<>();
        filters.put("userID", user.getUserId());
        filters.put("status", status);

        return reservationDAO.getReservations(filters);
    }

    public int calculateReservationCharge(ReservationDTO rDTO){
        LocalDate startDate = rDTO.getCheckIn();
        LocalDate endDate = rDTO.getCheckOut();
        RatePolicyDTO rateDTO = ratePolicyDAO.getRate(rDTO.getAccommodationID());
        List<DiscountPolicyDTO> discountDTOS = discountDAO.getDiscount(rDTO.getAccommodationID());

        List<LocalDate> datesInRange = getDatesBetween(startDate, endDate);

        int sumCharge = 0;

        for (LocalDate date : datesInRange) {
            int charge = 0;

            if(isWeekday(date))
                charge = rateDTO.getWeekday();
            else
                charge = rateDTO.getWeekend();

            if (discountDTOS != null){
                for(DiscountPolicyDTO discountDTO : discountDTOS){
                    if(date.isAfter(discountDTO.getStartDate()) && date.isBefore(discountDTO.getEndDate())){
                        if(Objects.equals(discountDTO.getDiscountType(), "정량")){
                            charge -= discountDTO.getValue();
                        }
                        else {
                            charge *= (100 - discountDTO.getValue()) * 0.01;
                        }
                    }
                }
            }

            sumCharge += charge;

        }
        return sumCharge;
    }

    private static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> datesInRange = new ArrayList<>();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            datesInRange.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return datesInRange;
    }

    private static boolean isWeekday(LocalDate date) {
        // 월요일(1)부터 금요일(5)까지가 주중
        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek >= 1 && dayOfWeek <= 5;
    }

}

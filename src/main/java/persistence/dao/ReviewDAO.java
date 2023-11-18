package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.ReviewDTO;
import persistence.mapper.ReviewMapper;

import java.util.List;

public class ReviewDAO {
    SqlSessionFactory sqlSessionFactory;
    public ReviewDAO(SqlSessionFactory sessionFactory) {
        this.sqlSessionFactory = sessionFactory;
    }
    public List<ReviewDTO> selectReviews(int accomID) {
        List<ReviewDTO> DTOS = null;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            ReviewMapper reservationMapper = session.getMapper(ReviewMapper.class);
            DTOS = reservationMapper.selectReviews(accomID);
        }
        return DTOS;
    }
}